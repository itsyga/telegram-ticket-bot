package ru.itsyga.telegramticketbot.statecommand;

import ru.itsyga.telegramticketbot.entity.Chat;

import java.util.Set;

public interface StateCommand {
    void execute(Chat chat, String msgText);

    Set<String> getStateNames();
}
