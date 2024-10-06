package ru.itsyga.telegramticketbot.statecommand;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.Request;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.util.Reply;
import ru.itsyga.telegramticketbot.util.StateAction;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PassengersCountPickStateCommand implements StateCommand {
    private StateProcessor stateProcessor;
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final SendMessageDirector sendMessageDirector;

    @Override
    public void execute(Chat chat, String msgText) {
        Long chatId = chat.getId();
        Request request = chat.getRequest();
        int passengersCount;
        try {
            passengersCount = Integer.parseInt(msgText);
        } catch (NumberFormatException e) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.UNSUPPORTED_PASSENGERS_COUNT_FORMAT.getText()));
            return;
        }
        request.setPassengersCount(passengersCount);
        repositoryService.updateRequest(request);
        chat = repositoryService.updateChatState(chat, StateAction.NEXT_STATE);
        stateProcessor.distribute(chat, null);
    }

    @Override
    public Set<String> getStateNames() {
        return Set.of("passengers count pick");
    }

    @Autowired
    public void setStateProcessor(@Lazy StateProcessor stateProcessor) {
        this.stateProcessor = stateProcessor;
    }
}
