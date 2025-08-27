package com.batching.backend.controller;

import com.batching.backend.entity.HeaderData;
import com.batching.backend.repository.HeaderDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/headers")
@CrossOrigin
public class HeaderController {

    @Autowired
    private HeaderDataRepository headerRepo;

    @GetMapping
    public List<HeaderData> getAll() {
        return headerRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeaderData> getById(@PathVariable int id) {
    return headerRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HeaderData create(@RequestBody HeaderData header) {
        return headerRepo.save(header);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeaderData> update(@PathVariable int id, @RequestBody HeaderData header) {
        if (!headerRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        header.setId(id);
        return ResponseEntity.ok(headerRepo.save(header));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!headerRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        headerRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
