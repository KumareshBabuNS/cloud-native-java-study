package com.example.jooq.domain;

/**
 * @author Kj Nam
 * @since 2017-01-09
 */
public class JooqReservation {
    private Integer id;

    private String reservationName;

    protected JooqReservation() {
    }

    public JooqReservation(Integer id, String reservationName) {
        this.id = id;
        this.reservationName = reservationName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public Integer getId() {
        return id;
    }

    public String getReservationName() {
        return reservationName;
    }
}
