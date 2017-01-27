package com.example;

import com.example.domain.Reservation;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Kj Nam
 * @since 2016-12-31
 */
@Component
public class ReservationResourceProcessor implements ResourceProcessor<Resource<Reservation>> {
    @Override
    public Resource<Reservation> process(Resource<Reservation> reservationResource) {
        Reservation reservation = reservationResource.getContent();
        Long id = reservation.getId();
        String url = "https://www.google.co.kr/?gfe_rd=cr&ei=v0lnWPGzFPTZ8AeV06Fo#newwindow=1&q=" + id;
        reservationResource.add(new Link(url, "Search Google"));

//        System.out.println("=====================================Resouerce Processor");

        return reservationResource;
    }
}
