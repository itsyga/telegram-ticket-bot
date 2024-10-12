package ru.itsyga.telegramticketbot.statecommand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itsyga.telegramticketbot.client.TelegramBotClient;
import ru.itsyga.telegramticketbot.client.TicketApiClient;
import ru.itsyga.telegramticketbot.director.SendMessageDirector;
import ru.itsyga.telegramticketbot.entity.Chat;
import ru.itsyga.telegramticketbot.entity.Request;
import ru.itsyga.telegramticketbot.model.Ride;
import ru.itsyga.telegramticketbot.util.Reply;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SearchResultStateCommand implements StateCommand {
    private final TicketApiClient apiClient;
    private final TelegramBotClient botClient;
    private final SendMessageDirector sendMessageDirector;

    @Override
    public void execute(Chat chat, String msgText) {
        Long chatId = chat.getId();
        Request request = chat.getRequest();
        String rideInfoTemplate = chat.getState().getPhrase().getText();
        List<Ride> rides;
        if ((rides = apiClient.findRides(request.getDepartureId(), request.getArrivalId(), request.getDate(), request.getPassengersCount())) == null) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.SERVICE_UNAVAILABLE.getText()));
            return;
        } else if (rides.isEmpty()) {
            botClient.sendMethod(sendMessageDirector.build(chatId, Reply.UNSUCCESSFUL_SEARCH.getText()));
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        rides.stream()
                .filter(ride -> Integer.parseInt(ride.getFreeSeats()) > 0)
                .map(ride -> String.format(rideInfoTemplate, ride.getRideName(),
                        ride.getDepartureDefaultStop().getStopNameWithDescription(),
                        ride.getDepartureDefaultStop().getDateTime().format(formatter),
                        ride.getArrivalDefaultStop().getStopNameWithDescription(),
                        ride.getArrivalDefaultStop().getDateTime().format(formatter),
                        ride.getPrice() + " " + ride.getCurrency(),
                        ride.getFreeSeats()))
                .forEach(reply -> botClient.sendMethod(sendMessageDirector.build(chatId, reply)));
    }

    @Override
    public Set<String> getStateNames() {
        return Set.of("search results");
    }
}
