package ru.itsyga.telegramticketbot.service.method;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.Request;
import ru.itsyga.telegramticketbot.entity.State;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.service.chatmessageupdate.ChatMessageUpdater;
import ru.itsyga.telegramticketbot.util.StateAction;

@Service
@RequiredArgsConstructor
public class CallbackService implements MethodService {
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final SendMessageDirector sendMessageDirector;
    @Qualifier("callbackChatMessageUpdater")
    private final ChatMessageUpdater chatMessageUpdater;

    @Override
    public void serve(BotApiObject apiObject) {
        CallbackQuery callbackQuery = (CallbackQuery) apiObject;
        Long chatId = callbackQuery.getMessage().getChatId();
        Chat chat = repositoryService.findOrCreateChat(chatId);
        updateDepartureOrArrivalLocationId(chat, callbackQuery);
    }

    private void updateDepartureOrArrivalLocationId(Chat chat, CallbackQuery callbackQuery) {
        Long chatId = chat.getId();
        Request request = chat.getRequest();
        State currentState = chat.getState();
        String callbackData = callbackQuery.getData();
        if (currentState.getName().equals("departure location search")) {
            request.setDepartureId(callbackData);
        } else {
            request.setArrivalId(callbackData);
        }
        repositoryService.updateRequest(request);
        String reply = repositoryService.updateChatState(chat, StateAction.NEXT_STATE)
                .getState()
                .getPhrase()
                .getText();
        chatMessageUpdater.update(chatId, callbackQuery);
        botClient.sendMethod(sendMessageDirector.buildWithBaseKeyboard(chatId, reply));
    }
}
