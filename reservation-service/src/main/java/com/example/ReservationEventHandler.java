package com.example;

import com.example.domain.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * @author Kj Nam
 * @since 2017-01-13
 */
@Component
@RepositoryEventHandler
public class ReservationEventHandler {

    @Autowired
    CounterService service;

    @HandleAfterCreate
    public void create(Reservation reservation) {
        count("reservations.create", reservation);
    }

    @HandleAfterSave
    public void save(Reservation reservation) {
        count("reservations.save", reservation);
        count("reservations." + reservation.getId() + ".save", reservation);
    }

    @HandleAfterDelete
    public void delete(Reservation reservation) {
        count("reservations.delete", reservation);
    }

    protected void count(String event, Reservation reservation) {
        this.service.increment(event);
        this.service.increment("meter." + event);
    }
}
