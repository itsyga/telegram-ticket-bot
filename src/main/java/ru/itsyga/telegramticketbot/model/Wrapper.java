package ru.itsyga.telegramticketbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Wrapper {
    @JsonProperty("rides")
    List<Ride> rides;
}
