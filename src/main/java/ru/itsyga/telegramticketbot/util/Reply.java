package ru.itsyga.telegramticketbot.util;

import lombok.Getter;

@Getter
public enum Reply {
    SERVICE_UNAVAILABLE("Сервис временно недоступен. Попробуйте позже"),
    UNSUCCESSFUL_SEARCH("Мне не удалось найти населённый пункт по Вашему запросу.\nПопробуйте ещё раз!");

    private final String text;

    Reply(String text) {
        this.text = text;
    }
}
