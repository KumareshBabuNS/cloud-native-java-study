package com;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.example.domain.Reservation;
import com.example.domain.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ReservationServiceApplication {
    static final int ERROR_CODE = 777;

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner run(ReservationRepository repository) {
        return args ->
                Arrays.asList("Marten,Josh,Dave,Mark,Mark,Juergen".split(","))
                        .forEach(x -> repository.save(new Reservation(x)));
    }

    @RefreshScope
    @RestController
    class MessageRestController {

        @Value("${message}")
        private String message;

        @RequestMapping("/message")
        String message() {
            return this.message;
        }
    }

    @Bean
    HealthIndicator healthIndicator() {
        int errorCode = check();
        if (errorCode != 0) {
            return () -> Health.down().withDetail("Error Code", ERROR_CODE).build();
        }

        return () -> Health.up().build();
    }

    @Bean
    HealthIndicator message() {
        return () -> Health.status("I <3 Spring!")
                .build();
    }

    private int check() {
        //FIXME
        return 0;
    }

    @Bean
    GraphiteReporter graphiteReporter(MetricRegistry registry,
                                      @Value("${graphite.host:192.168.99.100}") String host,
                                      @Value("${graphite.port:2003}") int port) {
        GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                .prefixedWith("reservations")
                .build(new Graphite(host, port));
        reporter.start(2, TimeUnit.SECONDS);
        return reporter;
    }
}
