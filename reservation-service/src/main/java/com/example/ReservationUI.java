package com.example;

import com.example.domain.repository.ReservationRepository;
import com.example.domain.Reservation;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Kj Nam
 * @since 2016-12-31
 */
@SpringUI(path = "ui")
@Theme("valo")
public class ReservationUI extends UI {

    @Autowired
    private ReservationRepository repository;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Table components = new Table();
        components.setContainerDataSource(new BeanItemContainer<>(
                Reservation.class,
                this.repository.findAll()
        ));
        components.setSizeFull();
        setContent(components);
    }
}
