package com.timurisachenko.socialmultiplication.services;

import com.timurisachenko.socialmultiplication.domain.Multiplication;
import com.timurisachenko.socialmultiplication.domain.MultiplicationResultAttempt;
import com.timurisachenko.socialmultiplication.domain.User;
import com.timurisachenko.socialmultiplication.event.MultiplicationKafkaProducer;
import com.timurisachenko.socialmultiplication.event.MultiplicationSolvedEvent;
import com.timurisachenko.socialmultiplication.repository.MultiplicationResultAttemptRepository;
import com.timurisachenko.socialmultiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {
    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository attemptRepository;
    private UserRepository userRepository;
    private MultiplicationKafkaProducer producer;
    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
                                     final MultiplicationResultAttemptRepository attemptRepository,
                                     final UserRepository userRepository,
                                     final MultiplicationKafkaProducer producer) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.producer = producer;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt attempt){
        Optional<User> userOptional = userRepository.findByAlias(attempt.getUser().getAlias());

        //check's if it is correct
        boolean correct = attempt.getResultAttempt() == attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();
        Assert.isTrue(!attempt.isCorrect(), "You can't send attempt marked is correct!!!");
        MultiplicationResultAttempt checkedResult = new MultiplicationResultAttempt(
                userOptional.orElse(attempt.getUser()),
                attempt.getMultiplication(),
                attempt.getResultAttempt(),
                correct
        );
        attemptRepository.save(checkedResult);

        producer.send(
                new MultiplicationSolvedEvent(checkedResult.getId(),
                        checkedResult.getUser().getId(),
                        checkedResult.isCorrect())
        );

        return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String alias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(alias);
    }

    @Override
    public MultiplicationResultAttempt getResultById(final Long resultId) {
        return attemptRepository.findById(resultId).get();
    }

}
