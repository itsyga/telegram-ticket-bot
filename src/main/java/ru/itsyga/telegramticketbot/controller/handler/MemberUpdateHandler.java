package ru.itsyga.telegramticketbot.controller.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;
import ru.itsyga.telegramticketbot.service.method.MethodService;

@Component
@RequiredArgsConstructor
public class MemberUpdateHandler implements UpdateHandler {
    private UpdateHandler nextHandler;
    @Qualifier("memberUpdateService")
    private final MethodService memberUpdateService;

    @Override
    public void handleUpdate(Update update) {
        ChatMemberUpdated memberUpdated;
        if ((memberUpdated = update.getMyChatMember()) == null
                || !memberUpdated.getNewChatMember().getStatus().equals("kicked")) {
            nextHandler.handleUpdate(update);
            return;
        }
        memberUpdateService.serve(memberUpdated);
    }

    @Override
    public void setNext(UpdateHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
