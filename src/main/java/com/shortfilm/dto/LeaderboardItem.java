package com.shortfilm.dto;

public record LeaderboardItem(
        int rank,
        Long entryId,
        String title,
        String directorName,
        int totalScore,
        double averageScore,
        long numberOfScores,
        long numberOfJudges
) {}