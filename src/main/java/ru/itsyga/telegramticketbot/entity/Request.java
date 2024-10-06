package ru.itsyga.telegramticketbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "request")
public class Request implements Persistable<Long> {
    @Id
    @Column(name = "request_id")
    private Long id;

    @Column(name = "departure_id", length = 15)
    private String departureId;

    @Column(name = "arrival_id", length = 15)
    private String arrivalId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "passengers_count")
    private Integer passengersCount;

    public Request(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return departureId == null;
    }
}
