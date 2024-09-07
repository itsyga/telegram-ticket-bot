package ru.itsyga.telegramticketbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.DeleteMessageDirector;
import ru.itsyga.telegramticketbot.director.EditMessageDirector;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.ChatMessage;
import ru.itsyga.telegramticketbot.entity.Request;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallbackService implements MethodService {
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final EditMessageDirector editMessageDirector;
    private final SendMessageDirector sendMessageDirector;
    private final DeleteMessageDirector deleteMessageDirector;

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
        String callbackData = callbackQuery.getData();
        if (request.getDepartureId() == null) {
            request.setDepartureId(callbackData);
        } else {
            request.setArrivalId(callbackData);
        }
        repositoryService.updateRequest(request);
        chat = repositoryService.updateChatState(chat);
        String reply = chat.getState().getPhrase().getText();
        updateChatMessages(chatId, callbackQuery);
        botClient.sendMethod(sendMessageDirector.buildWithBaseKeyboard(chatId, reply));
    }

    @Override
    public void updateChatMessages(Long chatId, BotApiObject apiObject) {
        CallbackQuery callbackQuery = (CallbackQuery) apiObject;
        Integer msgId = callbackQuery.getMessage().getMessageId();
        List<ChatMessage> messages = repositoryService.findChatMessages(chatId);
        if (messages.size() == 1) {
            repositoryService.deleteChatMessages(chatId);
            var msg = messages.getFirst();
            botClient.sendMethod(editMessageDirector.build(chatId, msgId, msg.getMessageText()));
        } else {
            repositoryService.deleteChatMessages(chatId);
            var msg = messages.stream()
                    .filter(chatMessage -> chatMessage.getMessageId().equals(msgId))
                    .findAny()
                    .orElseThrow();
            Set<Integer> messageIds = messages.stream()
                    .map(ChatMessage::getMessageId)
                    .filter(integer -> !integer.equals(msgId))
                    .collect(Collectors.toSet());
            botClient.sendMethod(deleteMessageDirector.build(chatId, messageIds));
            botClient.sendMethod(editMessageDirector.build(chatId, msgId, msg.getMessageText()));
        }
    }
}
