package com.example;

import com.example.jooq.domain.JooqReservation;
import com.example.jooq.service.ReservationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Kj Nam
 * @since 2017-01-09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JooqReservationServiceTest {
    private Logger logger = LoggerFactory.getLogger(JooqReservationServiceTest.class);

    @Autowired
    ReservationService service;

    @Test
    public void 모든_예약을_조회한다() {
        //given
        List<JooqReservation> result;

        //when
        result = service.getAllReservations();

        //then
        assertThat(result.size(), is(7));
    }

    @Test
    public void id를_생략해도_테이블_제약조건에_따라_id를_자동생성한다() {
        //given
        String name = "unique";
        JooqReservation reservation = new JooqReservation(null, name);

        //when
        JooqReservation result = service.createReservation(reservation);

        //then
        assertNotNull(result.getId());
    }

    @Test(expected = NullPointerException.class)
    public void 예약을_취소한다() {
        //given
        JooqReservation reservation = service.getReservation(1);
        assertNotNull(reservation);

        //when
        service.cancelReservation(reservation.getId());
        service.getReservation(reservation.getId());

        //then
        //exception
    }
}
