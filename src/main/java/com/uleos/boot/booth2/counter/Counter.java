package com.uleos.boot.booth2.counter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long counter;
    private LocalDate created;
    private String username;

}
