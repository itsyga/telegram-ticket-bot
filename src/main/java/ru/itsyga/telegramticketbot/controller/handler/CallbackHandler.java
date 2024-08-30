package ru.itsyga.telegramticketbot.controller.handler;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Order(2)
@Component
public class CallbackHandler implements UpdateHandler {
    private UpdateHandler nextHandler;

    @Override
    public void handleUpdate(Update update) {
        CallbackQuery callbackQuery;
        if ((callbackQuery = update.getCallbackQuery()) == null) {
            nextHandler.handleUpdate(update);
            return;
        }
    }

    @Override
    public void setNext(UpdateHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
