package ru.itsyga.telegramticketbot.textcommand;

import ru.itsyga.telegramticketbot.entity.Chat;

public interface TextCommand {
    void execute(Chat chat);

    String getCommandText();
}
