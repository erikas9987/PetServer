package com.pet.server.controllers;

import com.pet.server.errors.FoodNotFoundException;
import com.pet.server.model.Food;
import com.pet.server.model.Material;
import com.pet.server.repos.FoodRepository;
import com.pet.server.repos.MaterialRepository;
import com.pet.server.requests.CreateFoodRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class FoodRest {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @GetMapping(value = "/foods")
    public ResponseEntity<List<Food>> getAllFoods() {
        return ResponseEntity.ok(foodRepository.findAll());
    }

    @PostMapping(value = "/food")
    @Validated
    public ResponseEntity<Food> createFood(@Valid @RequestBody CreateFoodRequest request) {
        List<Material> materials = materialRepository.findAllByNameIn(request.getMaterials());
        Food food = Food.builder()
                .name(request.getName())
                .manufacturer(request.getManufacturer())
                .weight(request.getWeight())
                .materials(materials)
                .build();
        foodRepository.saveAndFlush(food);
        return ResponseEntity.ok(food);
    }

    @PatchMapping(value = "/food/{id}")
    @Validated
    public ResponseEntity<Food> updateFood(@PathVariable int id, @Valid @RequestBody CreateFoodRequest request) {
        List<Material> materials = materialRepository.findAllByNameIn(request.getMaterials());
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
        food.setName(request.getName());
        food.setManufacturer(request.getManufacturer());
        food.setWeight(request.getWeight());
        food.setMaterials(materials);
        foodRepository.saveAndFlush(food);
        return ResponseEntity.ok(food);
    }

    @DeleteMapping(value = "/food/{id}")
    public ResponseEntity<Food> deleteFood(@PathVariable int id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
        List<Material> materials = materialRepository.findAllByFoodsContaining(food);
        for (Material material : materials) {
            material.getFoods().remove(food);
        }
        food.getMaterials().clear();
        materialRepository.saveAll(materials);
        foodRepository.save(food);
        foodRepository.delete(food);
        return ResponseEntity.ok(food);
    }
}
