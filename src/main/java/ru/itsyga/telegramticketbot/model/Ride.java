package ru.itsyga.telegramticketbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ride {
    @JsonProperty("from")
    private Location departureLocation;

    @JsonProperty("to")
    private Location arrivalLocation;

    @JsonProperty("bus")
    private Transport transport;

    @JsonProperty("pickupStops")
    private List<Stop> departureStops;

    @JsonProperty("dischargeStops")
    private List<Stop> arrivalStops;

    @JsonProperty("price")
    private String price;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("freeSeats")
    private String freeSeats;

    public String getRideName() {
        return departureLocation.getName() + " -> " + arrivalLocation.getName();
    }

    public Stop getDepartureDefaultStop() {
        return departureStops.stream()
                .filter(Stop::isDefault)
                .findFirst()
                .orElse(departureStops.getFirst());
    }

    public Stop getArrivalDefaultStop() {
        return arrivalStops.stream()
                .filter(Stop::isDefault)
                .findFirst()
                .orElse(arrivalStops.getFirst());
    }
}
