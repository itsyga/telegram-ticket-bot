package ru.itsyga.telegramticketbot.util;

import lombok.Getter;

@Getter
public enum Reply {
    SERVICE_UNAVAILABLE("Сервис временно недоступен. Попробуйте позже"),
    UNSUCCESSFUL_SEARCH("Мне не удалось найти информацию по Вашему запросу.\nПопробуйте ещё раз!"),
    UNSUPPORTED_DATE_FORMAT("Дата поездки введена в некорректном формате.\nПопробуйте ещё раз!"),
    INCORRECT_TRIP_DATE("Вы ввели некорректную дату поездки.\nПопробуйте ещё раз!"),
    UNSUPPORTED_PASSENGERS_COUNT_FORMAT("Количество пассажиров введено в некорректном формате.\nПопробуйте ещё раз!");

    private final String text;

    Reply(String text) {
        this.text = text;
    }
}
