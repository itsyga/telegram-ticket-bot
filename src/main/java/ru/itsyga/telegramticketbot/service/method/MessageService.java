package ru.itsyga.telegramticketbot.service.method;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.client.TicketApiClient;
import ru.itsyga.telegramticketbot.command.CommandExecutor;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.ChatMessage;
import ru.itsyga.telegramticketbot.entity.Request;
import ru.itsyga.telegramticketbot.entity.State;
import ru.itsyga.telegramticketbot.model.Location;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.service.chatmessageupdate.ChatMessageUpdater;
import ru.itsyga.telegramticketbot.util.Reply;
import ru.itsyga.telegramticketbot.util.StateAction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessageService implements MethodService {
    private final TicketApiClient apiClient;
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final SendMessageDirector sendMessageDirector;
    private final ChatMessageUpdater chatMessageUpdater;
    private final Map<String, CommandExecutor> commandExecutorMap;

    public MessageService(TicketApiClient apiClient, TelegramBotClient botClient, RepositoryService repositoryService,
                          SendMessageDirector sendMessageDirector, List<CommandExecutor> commandExecutors,
                          @Qualifier("defaultChatMessageUpdater") ChatMessageUpdater chatMessageUpdater) {
        this.apiClient = apiClient;
        this.botClient = botClient;
        this.repositoryService = repositoryService;
        this.sendMessageDirector = sendMessageDirector;
        this.commandExecutorMap = commandExecutors.stream()
                .collect(Collectors.toMap(CommandExecutor::getCommandName, commandExecutor -> commandExecutor));
        this.chatMessageUpdater = chatMessageUpdater;
    }

    @Override
    public void serve(BotApiObject apiObject) {
        Message message = (Message) apiObject;
        Long chatId = message.getChatId();
        String msgText = message.getText();
        Chat chat = repositoryService.findOrCreateChat(chatId);
        State currentState = chat.getState();

        CommandExecutor executor;
        if ((executor = commandExecutorMap.get(msgText)) != null) {
            executor.execute(chat);
            return;
        }

        handleMessageBasedOnChatState(chat, currentState, msgText);
    }

    private void handleMessageBasedOnChatState(Chat chat, State currentState, String msgText) {
        switch (currentState.getName()) {
            case "departure location search":
            case "arrival location search":
                chatMessageUpdater.update(chat.getId(), null);
                searchLocations(chat, msgText);
                break;
            case "date pick":
                setTripDate(chat, msgText);
                break;
            case "passengers count pick":
                setPassengersCount(chat, msgText);
                break;
        }
    }

    private void searchLocations(Chat chat, String userRequest) {
        Long chatId = chat.getId();
        List<Location> locations;
        if ((locations = apiClient.findLocations(userRequest)) == null) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.SERVICE_UNAVAILABLE.getText()));
            return;
        }
        if (locations.isEmpty()) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.UNSUCCESSFUL_SEARCH.getText()));
            return;
        }
        List<ChatMessage> chatMessages = locations.stream()
                .map(location -> sendMessageDirector.buildWithBaseInlineKeyboard(
                        chatId, location.getName(), location.getId()))
                .map(botClient::sendMethod)
                .filter(Objects::nonNull)
                .map(serializable -> (Message) serializable)
                .map(message -> new ChatMessage(message.getMessageId(), message.getText(), chat))
                .toList();
        repositoryService.saveChatMessages(chatMessages);
    }

    private void setTripDate(Chat chat, String msgText) {
        Long chatId = chat.getId();
        Request request = chat.getRequest();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate tripDate;
        try {
            tripDate = LocalDate.parse(msgText, formatter);
        } catch (DateTimeParseException e) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.UNSUPPORTED_DATE_FORMAT.getText()));
            return;
        }
        request.setDate(tripDate);
        repositoryService.updateRequest(request);
        String reply = repositoryService.updateChatState(chat, StateAction.NEXT_STATE)
                .getState()
                .getPhrase()
                .getText();
        botClient.sendMethod(sendMessageDirector.build(chatId, reply));
    }

    private void setPassengersCount(Chat chat, String msgText) {
        Long chatId = chat.getId();
        Request request = chat.getRequest();
        int passengersCount;
        try {
            passengersCount = Integer.parseInt(msgText);
        } catch (NumberFormatException e) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.UNSUPPORTED_PASSENGERS_COUNT_FORMAT.getText()));
            return;
        }
        request.setPassengersCount(passengersCount);
        repositoryService.updateRequest(request);
    }
}
