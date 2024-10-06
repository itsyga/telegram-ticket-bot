package ru.itsyga.telegramticketbot.statecommand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.Request;
import ru.itsyga.telegramticketbot.service.RepositoryService;
import ru.itsyga.telegramticketbot.util.Reply;
import ru.itsyga.telegramticketbot.util.StateAction;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatePickStateCommand implements StateCommand {
    private final TelegramBotClient botClient;
    private final RepositoryService repositoryService;
    private final SendMessageDirector sendMessageDirector;

    @Override
    public void execute(Chat chat, String msgText) {
        Long chatId = chat.getId();
        Request request = chat.getRequest();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate tripDate;
        try {
            tripDate = LocalDate.parse(msgText, formatter);
            if (tripDate.isBefore(LocalDate.now())) {
                throw new DateTimeException("An elapsed date has been entered");
            }
        } catch (DateTimeParseException e) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.UNSUPPORTED_DATE_FORMAT.getText()));
            return;
        } catch (DateTimeException e) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.INCORRECT_TRIP_DATE.getText()));
            return;
        }
        request.setDate(tripDate);
        repositoryService.updateRequest(request);
        String reply = repositoryService.updateChatState(chat, StateAction.NEXT_STATE)
                .getState()
                .getPhrase()
                .getText();
        botClient.sendMethod(sendMessageDirector.build(chatId, reply));
    }

    @Override
    public Set<String> getStateNames() {
        return Set.of("date pick");
    }
}
