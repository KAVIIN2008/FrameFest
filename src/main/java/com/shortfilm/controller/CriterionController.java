package com.shortfilm.controller;

import com.shortfilm.entity.Criterion;
import com.shortfilm.service.CriterionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criteria")
public class CriterionController {

    private final CriterionService criterionService;

    public CriterionController(CriterionService criterionService) {
        this.criterionService = criterionService;
    }

    @GetMapping
    public List<Criterion> getAllCriteria() {
        return criterionService.getAllCriteria();
    }

    @GetMapping("/{id}")
    public Criterion getCriterionById(@PathVariable Long id) {
        return criterionService.getCriterionById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Criterion createCriterion(@Valid @RequestBody Criterion criterion) {
        return criterionService.createCriterion(criterion);
    }

    @PutMapping("/{id}")
    public Criterion updateCriterion(
            @PathVariable Long id,
            @Valid @RequestBody Criterion criterion) {
        return criterionService.updateCriterion(id, criterion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCriterion(@PathVariable Long id) {
        criterionService.deleteCriterion(id);
    }
}