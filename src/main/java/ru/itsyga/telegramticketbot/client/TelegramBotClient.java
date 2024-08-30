package ru.itsyga.telegramticketbot.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;

@Slf4j
@Component
public class TelegramBotClient {
    private final TelegramClient client;

    public TelegramBotClient(@Value("${bot.token}") String botToken) {
        this.client = new OkHttpTelegramClient(botToken);
    }

    public Serializable sendMethod(BotApiMethod<?> method) {
        Serializable reply = null;
        try {
            reply = client.execute(method);
        } catch (TelegramApiException e) {
            log.atError()
                    .setMessage("An exception occurred while executing the Telegram method {}")
                    .addArgument(method.getMethod())
                    .setCause(e)
                    .log();
        }
        return reply;
    }
}
