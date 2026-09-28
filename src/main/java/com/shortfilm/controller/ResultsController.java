package com.shortfilm.controller;

import com.shortfilm.dto.LeaderboardItem;
import com.shortfilm.service.ResultsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultsController {

    private final ResultsService resultsService;

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardItem> getLeaderboard() {
        return resultsService.getLeaderboard();
    }
}