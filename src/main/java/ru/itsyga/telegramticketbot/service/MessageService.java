package ru.itsyga.telegramticketbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.client.TicketApiClient;
import ru.itsyga.telegramticketbot.director.DeleteMessageDirector;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.ChatMessage;
import ru.itsyga.telegramticketbot.entity.State;
import ru.itsyga.telegramticketbot.model.Location;
import ru.itsyga.telegramticketbot.util.Reply;
import ru.itsyga.telegramticketbot.util.StateAction;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService implements MethodService {
    private final TicketApiClient apiClient;
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final SendMessageDirector sendMessageDirector;
    private final DeleteMessageDirector deleteMessageDirector;

    @Override
    public void serve(BotApiObject apiObject) {
        Message message = (Message) apiObject;
        Long chatId = message.getChatId();
        String msgText = message.getText();
        Chat chat = repositoryService.findOrCreateChat(chatId);
        State currentState = chat.getState();

        if (msgText.equals("/start")
                && currentState.getName().equals("departure location search")) {
            String reply = currentState.getPhrase().getText();
            botClient.sendMethod(sendMessageDirector.build(chatId, reply));
            return;
        } else if (msgText.equals("Вернуться назад")) {
            chat = repositoryService.updateChatState(chat, StateAction.PREVIOUS_STATE);
            currentState = chat.getState();
            String reply = currentState.getPhrase().getText();
            SendMessage sendMessage = currentState.getName().equals("departure location search") ?
                    sendMessageDirector.buildWithRemoveKeyboard(chatId, reply) :
                    sendMessageDirector.buildWithBaseKeyboard(chatId, reply);
            botClient.sendMethod(sendMessage);
            updateChatMessages(chatId, null);
            return;
        } else if (msgText.equals("Начать заново")) {
            chat = repositoryService.updateChatState(chat, StateAction.RESET_STATE);
            currentState = chat.getState();
            String reply = currentState.getPhrase().getText();
            botClient.sendMethod(sendMessageDirector.buildWithRemoveKeyboard(chatId, reply));
            updateChatMessages(chatId, null);
            return;
        }

        handleMessageBasedOnChatState(chat, currentState, msgText);
    }

    private void handleMessageBasedOnChatState(Chat chat, State currentState, String msgText) {
        switch (currentState.getName()) {
            case "departure location search":
            case "arrival location search":
                updateChatMessages(chat.getId(), null);
                searchLocations(chat, msgText);
                break;
        }
    }

    @Override
    public void updateChatMessages(Long chatId, BotApiObject apiObject) {
        List<ChatMessage> messages;
        if (!(messages = repositoryService.findChatMessages(chatId)).isEmpty()) {
            repositoryService.deleteChatMessages(chatId);
            Set<Integer> messageIds = messages.stream()
                    .map(ChatMessage::getMessageId)
                    .collect(Collectors.toSet());
            botClient.sendMethod(deleteMessageDirector.build(chatId, messageIds));
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
}
