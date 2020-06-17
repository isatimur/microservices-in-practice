package com.timurisachenko.socialmultiplication.controllers;

import com.timurisachenko.socialmultiplication.domain.Multiplication;
import com.timurisachenko.socialmultiplication.services.MultiplicationService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.MetaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/multiplications")
final class MultiplicationController {

    private final MultiplicationService multiplicationService;
    private final int serverPort;
    @Autowired
    public MultiplicationController(final MultiplicationService multiplicationService, @Value("${server.port}") int serverPort) {
        this.multiplicationService = multiplicationService;
        this.serverPort = serverPort;
    }

    @GetMapping(value = "/random", produces = "application/json")
    Multiplication getRandomMultiplication() {
        log.info("Generating a random multiplication from server @ {}", serverPort);
        return multiplicationService.createRandomMultiplication();
    }

}