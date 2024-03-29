package com.pet.server.repos;

import com.pet.server.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    List<Pet> findPetsByUser_Id(int id);
}
