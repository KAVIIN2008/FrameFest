package com.shortfilm.service;

import com.shortfilm.entity.Judge;
import com.shortfilm.repository.JudgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudgeService {

    private final JudgeRepository judgeRepository;

    public JudgeService(JudgeRepository judgeRepository) {
        this.judgeRepository = judgeRepository;
    }

    public List<Judge> getAllJudges() {
        return judgeRepository.findAll();
    }

    public Judge getJudgeById(Long id) {
        return judgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Judge not found with ID: " + id));
    }

    public Judge createJudge(Judge judge) {
        return judgeRepository.save(judge);
    }

    public Judge updateJudge(Long id, Judge updatedJudge) {
        Judge existingJudge = getJudgeById(id);

        existingJudge.setName(updatedJudge.getName());
        existingJudge.setEmail(updatedJudge.getEmail());
        existingJudge.setSpecialization(updatedJudge.getSpecialization());

        return judgeRepository.save(existingJudge);
    }

    public void deleteJudge(Long id) {
        Judge judge = getJudgeById(id);
        judgeRepository.delete(judge);
    }
}
