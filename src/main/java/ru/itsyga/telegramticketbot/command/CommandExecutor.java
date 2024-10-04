package ru.itsyga.telegramticketbot.command;

import ru.itsyga.telegramticketbot.entity.Chat;

public interface CommandExecutor {
    void execute(Chat chat);

    String getCommandName();
}
