package com.shortfilm.controller;

import com.shortfilm.entity.Score;
import com.shortfilm.service.ScoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping
    public List<Score> getAllScores() {
        return scoreService.getAllScores();
    }

    @GetMapping("/{id}")
    public Score getScoreById(@PathVariable Long id) {
        return scoreService.getScoreById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Score createScore(@Valid @RequestBody Score score) {
        return scoreService.createScore(score);
    }

    @PutMapping("/{id}")
    public Score updateScore(
            @PathVariable Long id,
            @Valid @RequestBody Score score) {
        return scoreService.updateScore(id, score);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScore(@PathVariable Long id) {
        scoreService.deleteScore(id);
    }
}