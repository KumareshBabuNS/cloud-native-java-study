package com;

import com.example.domain.Reservation;
import com.example.domain.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(ReservationRepository repository) {
        return args ->
                Arrays.asList("A,B,C,D,E".split(","))
                        .forEach(x -> repository.save(new Reservation(x)));
    }
}
