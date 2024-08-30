package ru.itsyga.telegramticketbot.util;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

@Getter
public enum Button {
    BACK(KeyboardButton.builder()
            .text("Вернуться назад")
            .build()),
    RESTART(KeyboardButton.builder()
            .text("Начать заново")
            .build());

    private final KeyboardButton button;

    Button(KeyboardButton button) {
        this.button = button;
    }
}
