package ru.itsyga.telegramticketbot.controller.handler;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Order(1)
@Component
public class MessageHandler implements UpdateHandler {
    private UpdateHandler nextHandler;

    @Override
    public void handleUpdate(Update update) {
        if (!update.hasMessage()) {
            nextHandler.handleUpdate(update);
            return;
        }
    }

    @Override
    public void setNext(UpdateHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
