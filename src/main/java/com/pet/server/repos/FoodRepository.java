package com.pet.server.repos;

import com.pet.server.model.Food;
import com.pet.server.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findAllByNameIn(Collection<String> name);
    List<Food> findAllByMaterialsContaining(Material material);
}