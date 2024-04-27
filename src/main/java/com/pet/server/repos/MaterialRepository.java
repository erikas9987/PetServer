package com.pet.server.repos;

import com.pet.server.model.Food;
import com.pet.server.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Optional<Material> findByName(String name);
    List<Material> findAllByNameIn(List<String> name);
    List<Material> findAllByFoodsContaining(Food food);
}
