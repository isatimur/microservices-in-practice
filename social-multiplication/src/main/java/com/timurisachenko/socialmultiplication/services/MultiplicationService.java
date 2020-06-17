package com.timurisachenko.socialmultiplication.services;

import com.timurisachenko.socialmultiplication.domain.Multiplication;
import com.timurisachenko.socialmultiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {
    Multiplication createRandomMultiplication();

    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

    List<MultiplicationResultAttempt> getStatsForUser(String alias);

    MultiplicationResultAttempt getResultById(Long resultId);
}
