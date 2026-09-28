package com.shortfilm.service;

import com.shortfilm.entity.Criterion;
import com.shortfilm.repository.CriterionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CriterionService {

    private final CriterionRepository criterionRepository;

    public CriterionService(CriterionRepository criterionRepository) {
        this.criterionRepository = criterionRepository;
    }

    public List<Criterion> getAllCriteria() {
        return criterionRepository.findAll();
    }

    public Criterion getCriterionById(Long id) {
        return criterionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Criterion not found: " + id));
    }

    public Criterion createCriterion(Criterion criterion) {
        return criterionRepository.save(criterion);
    }

    public Criterion updateCriterion(Long id, Criterion updatedCriterion) {
        Criterion existing = getCriterionById(id);
        existing.setName(updatedCriterion.getName());
        existing.setDescription(updatedCriterion.getDescription());
        existing.setMaxScore(updatedCriterion.getMaxScore());
        return criterionRepository.save(existing);
    }

    public void deleteCriterion(Long id) {
        Criterion existing = getCriterionById(id);
        criterionRepository.delete(existing);
    }
}