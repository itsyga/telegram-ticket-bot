package ru.itsyga.telegramticketbot.controller.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;

@Component
public class MemberUpdateHandler implements UpdateHandler {
    private UpdateHandler nextHandler;

    @Override
    public void handleUpdate(Update update) {
        ChatMemberUpdated memberUpdated;
        if ((memberUpdated = update.getMyChatMember()) == null
                || !memberUpdated.getNewChatMember().getStatus().equals("kicked")) {
            nextHandler.handleUpdate(update);
            return;
        }
    }

    @Override
    public void setNext(UpdateHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
