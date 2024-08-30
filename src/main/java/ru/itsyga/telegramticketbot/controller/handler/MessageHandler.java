package ru.itsyga.telegramticketbot.controller.handler;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Order(1)
@Component
public class MessageHandler implements UpdateHandler {
    private UpdateHandler nextHandler;

    @Override
    public void handleUpdate(Update update) {
        Message msg;
        if ((msg = update.getMessage()) == null || !msg.hasText()) {
            nextHandler.handleUpdate(update);
            return;
        }
    }

    @Override
    public void setNext(UpdateHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
