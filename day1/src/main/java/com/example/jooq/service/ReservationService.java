package com.example.jooq.service;

import com.example.jooq.domain.JooqReservation;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.jooq.generated.Jooqreservation.JOOQRESERVATION;

/**
 * @author Kj Nam
 * @since 2017-01-09
 */
@Service
@Transactional
public class ReservationService {

    @Autowired
    private DSLContext dsl;

    public JooqReservation createReservation(JooqReservation reservation) {
        Record record = this.dsl.insertInto(JOOQRESERVATION)
                .set(JOOQRESERVATION.ID, reservation.getId())
                .set(JOOQRESERVATION.NAME, reservation.getReservationName())
                .returning(JOOQRESERVATION.ID)
                .fetchOne();

        reservation.setId(record.getValue(JOOQRESERVATION.ID));
        return reservation;
    }

    public List<JooqReservation> getAllReservations() {
        List<JooqReservation> reservations = new ArrayList<>();
        Result<Record> result = this.dsl.select().from(JOOQRESERVATION).fetch();
        for (Record each : result) {
            reservations.add(getReservationEntity(each));
        }

        return reservations;
    }

    public JooqReservation getReservation(Integer reservationId) {
        Record r = this.dsl.select()
                .from(JOOQRESERVATION)
                .where(JOOQRESERVATION.ID.equal(reservationId)).fetchOne();
        return new JooqReservation(r.getValue(JOOQRESERVATION.ID), r.getValue(JOOQRESERVATION.NAME));
    }

    public void cancelReservation(Integer reservationId) {
        this.dsl.deleteFrom(JOOQRESERVATION)
                .where(JOOQRESERVATION.ID.equal(reservationId))
                .execute();
    }

    private JooqReservation getReservationEntity(Record r){
        Integer id = r.getValue(JOOQRESERVATION.ID, Integer.class);
        String title = r.getValue(JOOQRESERVATION.NAME, String.class);
        return new JooqReservation(id, title);
    }

}
