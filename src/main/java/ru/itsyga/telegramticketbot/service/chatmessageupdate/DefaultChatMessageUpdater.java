package ru.itsyga.telegramticketbot.service.chatmessageupdate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.DeleteMessageDirector;
import ru.itsyga.telegramticketbot.entity.ChatMessage;
import ru.itsyga.telegramticketbot.service.RepositoryService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultChatMessageUpdater implements ChatMessageUpdater {
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final DeleteMessageDirector deleteMessageDirector;

    @Override
    public void update(Long chatId, BotApiObject apiObject) {
        List<ChatMessage> messages;
        if (!(messages = repositoryService.findChatMessages(chatId)).isEmpty()) {
            Set<Integer> messageIds = messages.stream()
                    .map(ChatMessage::getMessageId)
                    .collect(Collectors.toSet());
            botClient.sendMethod(deleteMessageDirector.build(chatId, messageIds));
        }
    }
}
