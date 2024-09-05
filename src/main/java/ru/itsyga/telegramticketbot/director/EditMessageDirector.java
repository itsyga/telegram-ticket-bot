package ru.itsyga.telegramticketbot.director;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class EditMessageDirector {
    public EditMessageText build(Long chatId, Integer msgId, String selectedData) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(msgId)
                .text("Выбран " + selectedData)
                .build();
    }
}
