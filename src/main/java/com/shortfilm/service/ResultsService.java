package com.shortfilm.service;

import com.shortfilm.dto.LeaderboardItem;
import com.shortfilm.entity.Score;
import com.shortfilm.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultsService {

    private final ScoreRepository scoreRepository;

    public ResultsService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public List<LeaderboardItem> getLeaderboard() {
        Map<Long, List<Score>> grouped = scoreRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        score -> score.getEntry().getId()
                ));

        List<LeaderboardItem> sorted = grouped.entrySet()
                .stream()
                .map(group -> {
                    List<Score> scores = group.getValue();

                    int total = scores.stream()
                            .mapToInt(Score::getScoreValue)
                            .sum();

                    double average = BigDecimal.valueOf(
                            scores.stream()
                                    .mapToInt(Score::getScoreValue)
                                    .average()
                                    .orElse(0.0)
                    ).setScale(2, RoundingMode.HALF_UP).doubleValue();

                    Score first = scores.get(0);

                    long judges = scores.stream()
                            .map(score -> score.getJudge().getId())
                            .distinct()
                            .count();

                    return new LeaderboardItem(
                            0,
                            group.getKey(),
                            first.getEntry().getTitle(),
                            first.getEntry().getDirectorName(),
                            total,
                            average,
                            scores.size(),
                            judges
                    );
                })
                .sorted(
                        Comparator.comparingInt(
                                LeaderboardItem::totalScore
                        ).reversed()
                        .thenComparing(
                                Comparator.comparingDouble(
                                        LeaderboardItem::averageScore
                                ).reversed()
                        )
                        .thenComparing(LeaderboardItem::title)
                )
                .toList();

        java.util.ArrayList<LeaderboardItem> ranked =
                new java.util.ArrayList<>();

        for (int i = 0; i < sorted.size(); i++) {
            LeaderboardItem item = sorted.get(i);
            ranked.add(new LeaderboardItem(
                    i + 1,
                    item.entryId(),
                    item.title(),
                    item.directorName(),
                    item.totalScore(),
                    item.averageScore(),
                    item.numberOfScores(),
                    item.numberOfJudges()
            ));
        }

        return ranked;
    }
}