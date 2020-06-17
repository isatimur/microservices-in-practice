package com.timurisachenko.gamification.service;

import com.timurisachenko.gamification.client.MultiplicationResultAttemptClient;
import com.timurisachenko.gamification.client.dto.MultiplicationResultAttempt;
import com.timurisachenko.gamification.domain.Badge;
import com.timurisachenko.gamification.domain.BadgeCard;
import com.timurisachenko.gamification.domain.GameStats;
import com.timurisachenko.gamification.domain.ScoreCard;
import com.timurisachenko.gamification.repository.BadgeCardRepository;
import com.timurisachenko.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class GameServiceTest {

    @MockBean
    private ScoreCardRepository scoreCardRepository;
    @MockBean
    private BadgeCardRepository badgeCardRepository;
    @MockBean
    private MultiplicationResultAttemptClient attemptClient;

    private GameServiceImpl gameService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(
                scoreCardRepository,
                badgeCardRepository,
                attemptClient);

        // Common given - attempt does not contain a lucky number by default
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 20, 70, 1400, true);
        given(attemptClient.retrieveMultiplicationResultAttemptbyId(anyLong()))
                .willReturn(attempt);
    }

    @Test
    public void process_first_badge() {
        // given
        Long userId = 1L;
        Long attemptId = 8L;
        long totalScore = 10l;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(Collections.emptyList());

        GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, true);


        // assert - should score one card and win the badge FIRST_WON
        assertThat(gameStats.getScore()).isEqualTo(scoreCard.getScore());
        assertThat(gameStats.getBadges()).isEqualTo(Collections.singletonList(Badge.FIRST_WON));

    }

    @Test
    public void process_correct_gold_badge() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        long totalScore = 999l;
//        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        BadgeCard firstWonBadge = new BadgeCard(Badge.FIRST_WON, userId);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(createNScoreCards(99, userId));
        given(badgeCardRepository
                .findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(firstWonBadge));

        GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, true);

        // assert - should score one card and win the badge FIRST_WON
        assertThat(gameStats.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(gameStats.getBadges()).containsAnyOf(Badge.GOLD_MULTIPLICATOR);

    }

    @Test
    public void process_correct_for_lucky_number() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        long totalScore = 10l;
//        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        BadgeCard firstWonBadge = new BadgeCard(Badge.FIRST_WON, userId);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(createNScoreCards(1, userId));
        given(badgeCardRepository
                .findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(firstWonBadge));

        // Common given - attempt does not contain a lucky number by default
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 42, 20, 840, true);
        given(attemptClient.retrieveMultiplicationResultAttemptbyId(anyLong()))
                .willReturn(attempt);

        GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, true);

        // assert - should score one card and win the badge LUCKY_NUMBER
        assertThat(gameStats.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(gameStats.getBadges()).containsOnly(Badge.LUCKY_NUMBER);

    }

    @Test
    public void process_wrong_attempt() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        long totalScore = 10l;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        // this repository will return the just-won score card
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.emptyList());

        GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, false);

        // assert - should score one card and win the badge zero
        assertThat(gameStats.getScore()).isEqualTo(0l);
        assertThat(gameStats.getBadges()).isEmpty();

    }

    private List<ScoreCard> createNScoreCards(final long n, Long userId) {
        return LongStream.of(1, n)
                .mapToObj(i -> new ScoreCard(userId, i))
                .collect(toList());
    }

}
