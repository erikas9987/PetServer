package com.pet.server.repos;

import com.pet.server.model.Illness;
import com.pet.server.model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SymptomRepository extends JpaRepository<Symptom, Integer> {
    Optional<Symptom> findByName(String name);
    List<Symptom> findAllByNameIn(List<String> name);
    List<Symptom> findAllByIllnessesContaining(Illness illness);
}
