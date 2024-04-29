package com.pet.server.controllers;

import com.pet.server.errors.MaterialNotFoundException;
import com.pet.server.model.Food;
import com.pet.server.model.Material;
import com.pet.server.repos.FoodRepository;
import com.pet.server.repos.MaterialRepository;
import com.pet.server.requests.CreateMaterialRequest;
import com.pet.server.requests.GetMaterialRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Validated
public class MaterialRest {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping(value = "/materials")
    public ResponseEntity<List<Material>> getAllMaterials() {
        return ResponseEntity.ok(materialRepository.findAll());
    }

    @GetMapping(value = "/material")
    public ResponseEntity<Material> getMaterialByName(@Valid @RequestBody GetMaterialRequest request) {
        Material material = materialRepository.findByName(request.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(material);
    }

    @PostMapping(value = "/material")
    public ResponseEntity<Material> createMaterial(@Valid @RequestBody CreateMaterialRequest request) {
        Material material = Material.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .foods(foodRepository.findAllByNameIn(request.getFoods()))
                .build();
        materialRepository.saveAndFlush(material);
        return ResponseEntity.ok(material);
    }

    @PatchMapping(value = "/material/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable int id, @Valid @RequestBody CreateMaterialRequest request) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException(id));
        material.setName(request.getName());
        material.setQuantity(request.getQuantity());
        material.setFoods(foodRepository.findAllByNameIn(request.getFoods()));
        materialRepository.saveAndFlush(material);
        return ResponseEntity.ok(material);
    }

    /*
    @DeleteMapping(value = "/material/{id}")
    public ResponseEntity<Material> deleteMaterial(@PathVariable int id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException(id));
        List<Food> foods = foodRepository.findAllByMaterialsContaining(material);
        for (Food food : foods) {
            food.getMaterials().remove(material);
        }
        material.getFoods().clear();
        foodRepository.saveAll(foods);
        materialRepository.save(material);
        materialRepository.delete(material);
        return ResponseEntity.ok(material);
    }
*/
}
