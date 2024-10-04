package ru.itsyga.telegramticketbot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.State;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.service.chatmessageupdate.ChatMessageUpdater;
import ru.itsyga.telegramticketbot.util.StateAction;

@Component
@RequiredArgsConstructor
public class PreviousStepCommandExecutor implements CommandExecutor {
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final SendMessageDirector sendMessageDirector;
    @Qualifier("defaultChatMessageUpdater")
    private final ChatMessageUpdater chatMessageUpdater;

    @Override
    public void execute(Chat chat) {
        Long chatId = chat.getId();
        chat = repositoryService.updateChatState(chat, StateAction.PREVIOUS_STATE);
        State currentState = chat.getState();
        String reply = currentState.getPhrase().getText();
        SendMessage sendMessage = currentState.getName().equals("departure location search") ?
                sendMessageDirector.buildWithRemoveKeyboard(chatId, reply) :
                sendMessageDirector.buildWithBaseKeyboard(chatId, reply);
        botClient.sendMethod(sendMessage);
        chatMessageUpdater.update(chatId, null);
    }

    @Override
    public String getCommandName() {
        return "Вернуться назад";
    }
}
