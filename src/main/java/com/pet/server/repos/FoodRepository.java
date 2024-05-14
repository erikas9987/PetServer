package com.pet.server.repos;

import com.pet.server.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findAllByNameIn(Collection<String> name);
    Optional<Food> findFoodByName(String name);
}