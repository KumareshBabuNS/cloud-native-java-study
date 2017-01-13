package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Kj Nam
 * @since 2017-01-02
 */
@Entity
public class Baby {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Baby() {
    }

    public Baby(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
