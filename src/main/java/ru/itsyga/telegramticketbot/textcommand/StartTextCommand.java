package ru.itsyga.telegramticketbot.textcommand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.State;

@Component
@RequiredArgsConstructor
public class StartTextCommand implements TextCommand {
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
    public String getCommandText() {
        return "/start";
    }
}
