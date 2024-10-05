package ru.itsyga.telegramticketbot.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.itsyga.telegramticketbot.model.Location;
import ru.itsyga.telegramticketbot.model.Ride;
import ru.itsyga.telegramticketbot.model.Wrapper;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class TicketApiClient {
    @Value("${ticket.service.api.location.search}")
    private String locationSearchUrl;
    @Value("${ticket.service.api.rides.search}")
    private String ridesSearchUrl;
    private final RestClient client;

    public TicketApiClient(@Value("${ticket.service.api.baseurl}") String baseUrl) {
        var httpFactory = new SimpleClientHttpRequestFactory();
        httpFactory.setConnectTimeout(100);
        httpFactory.setReadTimeout(5_000);
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(httpFactory)
                .build();
    }

    public List<Location> findLocations(String location) {
        List<Location> locations = null;
        try {
            locations = client.get()
                    .uri(locationSearchUrl, location)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<Location>>() {})
                    .getBody();
        } catch (RestClientException e) {
            log.atError()
                    .setMessage("An exception occurred while executing findLocations method, input value: location {}")
                    .addArgument(location)
                    .setCause(e)
                    .log();
        }
        return locations;
    }

    public List<Ride> findRides(String departureId, String arrivalId, LocalDate date, Integer passengersCount) {
        Wrapper response = null;
        try {
            response = client.get()
                    .uri(ridesSearchUrl, departureId, arrivalId, date, passengersCount)
                    .retrieve()
                    .body(Wrapper.class);
        } catch (RestClientException e) {
            log.atError()
                    .setMessage("An exception occurred while executing findRides method, " +
                            "input values: departureId {}, arrivalId {}, date {}, passengersCount {}")
                    .addArgument(departureId)
                    .addArgument(arrivalId)
                    .addArgument(date)
                    .addArgument(passengersCount)
                    .setCause(e)
                    .log();
        }
        return response != null ? response.getRides() : null;
    }
}
