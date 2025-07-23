package com.batching.backend.controller;

import com.batching.backend.entity.CustomerData;
import com.batching.backend.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerRepository repo;

    public CustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<CustomerData> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerData> getById(@PathVariable int id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check")
    public boolean exists(@RequestParam int id) {
        return repo.existsByCustomerId(id);
    }

    @PostMapping
    public CustomerData save(@RequestBody CustomerData data) {
        return repo.save(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerData> update(@PathVariable int id, @RequestBody CustomerData data) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        data.setCustomerId(id); // ensure ID is set
        return ResponseEntity.ok(repo.save(data));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        repo.deleteById(id);
    }
}
