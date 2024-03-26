package com.pet.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    private int id;
    private String name;
    private String manufacturer;
    private double weight;
    @ManyToMany
    private List<Material> materials;
}
