package com.shortfilm.service;

import com.shortfilm.entity.Score;
import com.shortfilm.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    public Score getScoreById(Long id) {
        return scoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Score not found: " + id));
    }

    public Score createScore(Score score) {
        validateScore(score);
        return scoreRepository.save(score);
    }

    public Score updateScore(Long id, Score updatedScore) {
        Score existing = getScoreById(id);

        existing.setEntry(updatedScore.getEntry());
        existing.setJudge(updatedScore.getJudge());
        existing.setCriterion(updatedScore.getCriterion());
        existing.setScoreValue(updatedScore.getScoreValue());
        existing.setComments(updatedScore.getComments());

        validateScore(existing);
        return scoreRepository.save(existing);
    }

    public void deleteScore(Long id) {
        Score existing = getScoreById(id);
        scoreRepository.delete(existing);
    }

    private void validateScore(Score score) {
        if (score.getScoreValue() == null || score.getScoreValue() < 0) {
            throw new IllegalArgumentException("Score must be zero or greater");
        }

        if (score.getCriterion() == null ||
                score.getCriterion().getMaxScore() == null) {
            throw new IllegalArgumentException("A valid criterion is required");
        }

        if (score.getScoreValue() > score.getCriterion().getMaxScore()) {
            throw new IllegalArgumentException(
                    "Score cannot exceed the criterion's maximum score"
            );
        }
    }
}