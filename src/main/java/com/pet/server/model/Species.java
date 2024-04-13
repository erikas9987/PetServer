package com.pet.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private PetType type;
    private double heightFrom;
    private double heightTo;
    private double weight;
    private int feedingRate;
    private int furColor;
    private int lifeExpectancy;
    @OneToMany
    private List<Illness> illnesses;

}
