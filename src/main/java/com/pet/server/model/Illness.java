package com.pet.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Illness {

    @Id
    private int id;
    private String name;
    private String description;
    @OneToMany
    private List<Symptom> symptoms;

}
