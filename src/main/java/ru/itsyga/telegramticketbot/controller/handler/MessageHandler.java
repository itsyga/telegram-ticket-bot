package ru.itsyga.telegramticketbot.controller.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.itsyga.telegramticketbot.service.MethodService;

@Order(1)
@Component
@RequiredArgsConstructor
public class MessageHandler implements UpdateHandler {
    private UpdateHandler nextHandler;
    @Qualifier("messageService")
    private final MethodService messageService;

    @Override
    public void handleUpdate(Update update) {
        Message msg;
        if ((msg = update.getMessage()) == null || !msg.hasText()) {
            nextHandler.handleUpdate(update);
            return;
        }
        messageService.serve(msg);
    }

    @Override
    public void setNext(UpdateHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
