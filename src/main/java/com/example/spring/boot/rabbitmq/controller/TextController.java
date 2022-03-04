package com.example.spring.boot.rabbitmq.controller;

import com.example.spring.boot.rabbitmq.data.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TextController {

    private final MessagePublisher messagePublisher;

    @PostMapping(value = "/text")
    public void postText(@RequestParam String text) {
        log.info("Received POST for text: {}", text);
        this.messagePublisher.publish(text);
        log.info("Sent text to publisher: {}", text);
    }
}
