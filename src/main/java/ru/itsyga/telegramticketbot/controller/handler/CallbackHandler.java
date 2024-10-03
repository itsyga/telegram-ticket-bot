package ru.itsyga.telegramticketbot.controller.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.itsyga.telegramticketbot.service.method.MethodService;

@Order(2)
@Component
@RequiredArgsConstructor
public class CallbackHandler implements UpdateHandler {
    private UpdateHandler nextHandler;
    @Qualifier("callbackService")
    private final MethodService methodService;

    @Override
    public void handleUpdate(Update update) {
        CallbackQuery callbackQuery;
        if ((callbackQuery = update.getCallbackQuery()) == null) {
            nextHandler.handleUpdate(update);
            return;
        }
        methodService.serve(callbackQuery);
    }

    @Override
    public void setNext(UpdateHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
