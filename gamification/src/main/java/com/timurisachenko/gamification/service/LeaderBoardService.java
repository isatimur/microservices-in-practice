package com.timurisachenko.gamification.service;

import com.timurisachenko.gamification.domain.LeaderBoardRow;

import java.util.List;

public interface LeaderBoardService {

    /**
     * Retrieves the current leader board with the top score users
     * @return the users with the highest score
     */
    List<LeaderBoardRow> getCurrentLeaderBoard();
}
