package com.timurisachenko.gamification.event;

import com.timurisachenko.gamification.domain.GameStats;
import com.timurisachenko.gamification.service.GameService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class EventHandlerTest {
    private EventHandler eventHandler;

    @Mock
    private GameService gameService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventHandler = new EventHandler(gameService);
    }

    @Test
    public void multiplicationAttemptReceivedTest() {
        // given
        Long userId = 1L;
        Long attemptId = 8L;
        boolean correct = true;
        GameStats gameStatsExpected = new GameStats();
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attemptId, userId, correct);
        given(gameService.newAttemptForUser(userId, attemptId, correct)).willReturn(gameStatsExpected);

        // when
        eventHandler.receive(event);

        // then
        verify(gameService).newAttemptForUser(userId, attemptId, correct);
    }

}

