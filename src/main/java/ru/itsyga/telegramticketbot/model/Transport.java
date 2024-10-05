package ru.itsyga.telegramticketbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transport {
    @JsonProperty("mark")
    private String brand;

    @JsonProperty("model")
    private String model;

    @JsonProperty("reg")
    private String licencePlate;

    @JsonProperty("color")
    private Color color;
}
