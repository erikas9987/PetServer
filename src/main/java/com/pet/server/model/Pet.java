package com.pet.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private User user;

    @ManyToOne
    private Species species;

    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private double height;
    private double weight;
}
