package ru.itsyga.telegramticketbot.director;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
public class EditMessageDirector {
    public EditMessageText build(Long chatId, Integer msgId, String selectedData) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(msgId)
                .text("Вы выбрали " + selectedData)
                .build();
    }
}
