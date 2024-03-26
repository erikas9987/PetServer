package com.pet.server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pet {

    @Id
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne
    private Species species;

    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private double height;
    private double weight;
}
