package ru.itsyga.telegramticketbot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.service.chatmessageupdate.ChatMessageUpdater;
import ru.itsyga.telegramticketbot.util.StateAction;

@Component
@RequiredArgsConstructor
public class ResetChatStateCommandExecutor implements CommandExecutor {
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final SendMessageDirector sendMessageDirector;
    @Qualifier("defaultChatMessageUpdater")
    private final ChatMessageUpdater chatMessageUpdater;

    @Override
    public void execute(Chat chat) {
        Long chatId = chat.getId();
        String reply = repositoryService.updateChatState(chat, StateAction.RESET_STATE)
                .getState()
                .getPhrase()
                .getText();
        botClient.sendMethod(sendMessageDirector.buildWithRemoveKeyboard(chatId, reply));
        chatMessageUpdater.update(chatId, null);
    }

    @Override
    public String getCommandName() {
        return "Начать заново";
    }
}
