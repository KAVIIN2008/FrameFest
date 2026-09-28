
package com.shortfilm;

import com.shortfilm.entity.Criterion;
import com.shortfilm.entity.Score;
import com.shortfilm.repository.ScoreRepository;
import com.shortfilm.service.ScoreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoreService scoreService;

    private Score score;
    private Criterion criterion;

    @BeforeEach
    void setUp() {
        criterion = new Criterion();
        criterion.setMaxScore(10);

        score = new Score();
        score.setScoreValue(8);
        score.setCriterion(criterion);
    }

    @Test
    void shouldCreateValidScore() {
        when(scoreRepository.save(score)).thenReturn(score);

        Score result = scoreService.createScore(score);

        assertNotNull(result);
        assertEquals(8, result.getScoreValue());
        verify(scoreRepository).save(score);
    }

    @Test
    void shouldRejectNegativeScore() {
        score.setScoreValue(-1);

        assertThrows(
                IllegalArgumentException.class,
                () -> scoreService.createScore(score)
        );

        verify(scoreRepository, never()).save(any());
    }

    @Test
    void shouldRejectScoreAboveMaximum() {
        score.setScoreValue(11);

        assertThrows(
                IllegalArgumentException.class,
                () -> scoreService.createScore(score)
        );

        verify(scoreRepository, never()).save(any());
    }

    @Test
    void shouldAcceptScoreEqualToMaximum() {
        score.setScoreValue(10);

        when(scoreRepository.save(score)).thenReturn(score);

        Score result = scoreService.createScore(score);

        assertEquals(10, result.getScoreValue());
        verify(scoreRepository).save(score);
    }

    @Test
    void shouldRejectMissingCriterion() {
        score.setCriterion(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> scoreService.createScore(score)
        );

        verify(scoreRepository, never()).save(any());
    }
}