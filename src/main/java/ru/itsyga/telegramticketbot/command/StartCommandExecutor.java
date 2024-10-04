package ru.itsyga.telegramticketbot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.State;

@Component
@RequiredArgsConstructor
public class StartCommandExecutor implements CommandExecutor {
    private final TelegramBotClient botClient;
    private final SendMessageDirector sendMessageDirector;

    @Override
    public void execute(Chat chat) {
        Long chatId = chat.getId();
        State currentState = chat.getState();
        if (!currentState.getName().equals("departure location search")) return;
        String reply = currentState.getPhrase().getText();
        botClient.sendMethod(sendMessageDirector.build(chatId, reply));
    }

    @Override
    public String getCommandName() {
        return "/start";
    }
}
