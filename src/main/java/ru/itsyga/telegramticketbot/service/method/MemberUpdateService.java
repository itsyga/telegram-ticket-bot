package ru.itsyga.telegramticketbot.service.method;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.service.chatmessageupdate.ChatMessageUpdater;

@Service
@RequiredArgsConstructor
public class MemberUpdateService implements MethodService {
    private final RepositoryService repositoryService;
    @Qualifier("defaultChatMessageUpdater")
    private final ChatMessageUpdater chatMessageUpdater;

    @Override
    public void serve(BotApiObject apiObject) {
        ChatMemberUpdated chatMemberUpdated = (ChatMemberUpdated) apiObject;
        Long chatId = chatMemberUpdated.getChat().getId();
        chatMessageUpdater.update(chatId, null);
        repositoryService.deleteChat(chatId);
    }
}
