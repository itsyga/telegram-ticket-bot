package ru.itsyga.telegramticketbot.service.method;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.DeleteMessageDirector;
import ru.itsyga.telegramticketbot.entity.ChatMessage;
import ru.itsyga.telegramticketbot.service.RepositoryService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberUpdateService implements MethodService {
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final DeleteMessageDirector deleteMessageDirector;

    @Override
    public void serve(BotApiObject apiObject) {
        ChatMemberUpdated chatMemberUpdated = (ChatMemberUpdated) apiObject;
        Long chatId = chatMemberUpdated.getChat().getId();
        updateChatMessages(chatId, null);
        repositoryService.deleteChat(chatId);
    }

    @Override
    public void updateChatMessages(Long chatId, BotApiObject apiObject) {
        List<ChatMessage> messages;
        if (!(messages = repositoryService.findChatMessages(chatId)).isEmpty()) {
            Set<Integer> messageIds = messages.stream()
                    .map(ChatMessage::getMessageId)
                    .collect(Collectors.toSet());
            botClient.sendMethod(deleteMessageDirector.build(chatId, messageIds));
        }
    }
}
