package com.timurisachenko.socialmultiplication.event;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Source.class)
public class MultiplicationKafkaProducer {
    private static final int SENDING_MESSAGE_TIMEOUTS_MS = 10000;
    private final Source source;

    public MultiplicationKafkaProducer(final Source source) {
        this.source = source;
    }

    public void send(final MultiplicationSolvedEvent multiplicationSolvedEvent) {
        this.source.output().send(MessageBuilder.withPayload(multiplicationSolvedEvent).build());
    }
}

