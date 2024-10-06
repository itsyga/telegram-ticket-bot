package ru.itsyga.telegramticketbot.statecommand;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.client.TicketApiClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.ChatMessage;
import ru.itsyga.telegramticketbot.model.Location;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.service.chatmessageupdate.ChatMessageUpdater;
import ru.itsyga.telegramticketbot.util.Reply;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LocationSearchStateCommand implements StateCommand {
    private final TicketApiClient apiClient;
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final SendMessageDirector sendMessageDirector;
    @Qualifier("defaultChatMessageUpdater")
    private final ChatMessageUpdater chatMessageUpdater;

    @Override
    public void execute(Chat chat, String msgText) {
        Long chatId = chat.getId();
        List<Location> locations;
        chatMessageUpdater.update(chatId, null);
        if ((locations = apiClient.findLocations(msgText)) == null) {
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

    @Override
    public Set<String> getStateNames() {
        return Set.of("departure location search", "arrival location search");
    }
}
