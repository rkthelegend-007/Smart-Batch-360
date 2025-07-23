package com.batching.backend.controller;

import com.batching.backend.entity.SiteData;
import com.batching.backend.repository.SiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
@CrossOrigin
public class SiteController {

    private final SiteRepository siteRepository;

    public SiteController(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @GetMapping
    public List<SiteData> getAllSites() {
        return siteRepository.findAll();
    }

    @GetMapping("/check")
    public boolean checkSiteExists(@RequestParam int id) {
        return siteRepository.existsBySiteId(id);
    }

    @PostMapping
    public SiteData createSite(@RequestBody SiteData site) {
        return siteRepository.save(site);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiteData> updateSite(@PathVariable int id, @RequestBody SiteData updated) {
        return siteRepository.findById(id)
                .map(existing -> {
                    existing.setSiteName(updated.getSiteName());
                    existing.setSiteAddress(updated.getSiteAddress());
                    existing.setContactInfo(updated.getContactInfo());
                    existing.setGstNumber(updated.getGstNumber());
                    return ResponseEntity.ok(siteRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable int id) {
        siteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
