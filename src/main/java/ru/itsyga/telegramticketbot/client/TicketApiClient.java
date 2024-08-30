package ru.itsyga.telegramticketbot.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.itsyga.telegramticketbot.model.Location;

import java.util.List;

@Slf4j
@Component
public class TicketApiClient {
    @Value("${ticket.service.api.search}")
    private String searchUrl;
    private final RestClient client;

    public TicketApiClient(@Value("${ticket.service.api.baseurl}") String baseUrl) {
        var httpFactory = new SimpleClientHttpRequestFactory();
        httpFactory.setConnectTimeout(100);
        httpFactory.setReadTimeout(1000);
        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(httpFactory)
                .build();
    }

    public List<Location> findLocations(String location) {
        List<Location> locations = null;
        try {
            locations = client.get()
                    .uri(searchUrl, location)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<Location>>() {})
                    .getBody();
        } catch (RestClientException e) {
            log.atError()
                    .setMessage("An exception occurred while executing findLocations method, input value = {}")
                    .addArgument(location)
                    .setCause(e)
                    .log();
        }
        return locations;
    }
}
