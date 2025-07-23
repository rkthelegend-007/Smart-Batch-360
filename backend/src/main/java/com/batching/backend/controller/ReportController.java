package com.batching.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.batching.backend.entity.Report;
import com.batching.backend.repository.ReportRepository;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*") // 🔄 Allow calls from desktop app or frontend
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    // ✅ Create a new report
    @PostMapping
    public Report createReport(@RequestBody Report report) {
        return reportRepository.save(report);
    }

    // ✅ Get all reports
    @GetMapping
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // ✅ Get a report by ID
    @GetMapping("/{id}")
    public Report getReportById(@PathVariable Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    // ✅ Delete a report
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportRepository.deleteById(id);
    }

    // ✅ Update a report (optional for now)
    @PutMapping("/{id}")
    public Report updateReport(@PathVariable Long id, @RequestBody Report updated) {
        return reportRepository.findById(id).map(report -> {
            report.setOperatorName(updated.getOperatorName());
            report.setCementWeight(updated.getCementWeight());
            report.setSandWeight(updated.getSandWeight());
            report.setAggregateWeight(updated.getAggregateWeight());
            return reportRepository.save(report);
        }).orElse(null);
    }
}
