package com.shortfilm.controller;

import com.shortfilm.entity.Judge;
import com.shortfilm.service.JudgeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judges")
public class JudgeController {

    private final JudgeService judgeService;

    public JudgeController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @GetMapping
    public List<Judge> getAllJudges() {
        return judgeService.getAllJudges();
    }

    @GetMapping("/{id}")
    public Judge getJudgeById(@PathVariable Long id) {
        return judgeService.getJudgeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Judge createJudge(@Valid @RequestBody Judge judge) {
        return judgeService.createJudge(judge);
    }

    @PutMapping("/{id}")
    public Judge updateJudge(
            @PathVariable Long id,
            @Valid @RequestBody Judge judge) {
        return judgeService.updateJudge(id, judge);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJudge(@PathVariable Long id) {
        judgeService.deleteJudge(id);
    }
}
