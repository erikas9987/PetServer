package com.pet.server.repos;

import com.pet.server.model.Illness;
import com.pet.server.model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface IllnessRepository extends JpaRepository<Illness, Integer> {
    List<Illness> findAllByNameIn(Collection<String> name);
    List<Illness> findAllBySymptomsContaining(Symptom symptom);

}
