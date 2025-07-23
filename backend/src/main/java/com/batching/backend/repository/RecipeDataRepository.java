package com.batching.backend.repository;
import com.batching.backend.entity.RecipeData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeDataRepository extends JpaRepository<RecipeData, Integer> {

}