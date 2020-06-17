package com.timurisachenko.gamification.service;

import com.timurisachenko.gamification.domain.LeaderBoardRow;
import com.timurisachenko.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class LeaderBoardServiceTest {
    private LeaderBoardServiceImpl leaderBoardService;

    @MockBean
    ScoreCardRepository scoreCardRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
    }

    @Test
    public void getCurrentLeaderBoard() {
        LeaderBoardRow leaderBoard1 = new LeaderBoardRow(0l, 100l);
        List<LeaderBoardRow> expectedLeaderBoard = Stream.of(leaderBoard1).collect(Collectors.toList());
        given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoard);
        List<LeaderBoardRow> resultList = leaderBoardService.getCurrentLeaderBoard();

        // then
        assertThat(resultList).isEqualTo(expectedLeaderBoard);
    }
}
