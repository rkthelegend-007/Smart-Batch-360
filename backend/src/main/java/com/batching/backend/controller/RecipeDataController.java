package com.batching.backend.controller;

import com.batching.backend.entity.RecipeData;
import com.batching.backend.repository.RecipeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeDataController {

    @Autowired
    private RecipeDataRepository recipeRepo;

    @GetMapping
    public List<RecipeData> getAllRecipes() {
        return recipeRepo.findAll();
    }

    @GetMapping("/{id}")
    public RecipeData getRecipeById(@PathVariable int id) {
        return recipeRepo.findById(id).orElse(null);
    }

    @PostMapping
    public RecipeData saveRecipe(@RequestBody RecipeData recipe) {
        return recipeRepo.save(recipe);
    }

    @PutMapping("/{id}")
    public RecipeData updateRecipe(@PathVariable int id, @RequestBody RecipeData updatedRecipe) {
        updatedRecipe.setRecipeId(id);
        return recipeRepo.save(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable int id) {
        recipeRepo.deleteById(id);
    }
}
