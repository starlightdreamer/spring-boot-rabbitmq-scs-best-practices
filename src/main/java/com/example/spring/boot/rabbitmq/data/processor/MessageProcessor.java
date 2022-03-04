package com.example.spring.boot.rabbitmq.data.processor;

import com.example.spring.boot.rabbitmq.business.TextService;
import com.example.spring.boot.rabbitmq.vo.TextWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MessageProcessor {

    private final TextService textService;

    /**
     * Use Message object types if you wish to view/add headers.
     * Function<Message<String>, Message<TextWrapper>> is otherwise functionally the same as Function<String, TextWrapper>.
     * For the latter, Spring automatically uses the payload as the input and outputs a Message with the TextWrapper as payload.
     */
    @Bean
    public Function<String, Message<TextWrapper>> textToTextWrapperProcessor() {
        return text -> {
            TextWrapper wrappedText = this.textService.wrapText(text);
            log.info("Consuming text: {}. Mapping original text into wrapper class: {}", text, wrappedText);
            return MessageBuilder.withPayload(wrappedText).setHeader("hello", "world!").build();
        };
    }

    @Bean
    public Consumer<Message<TextWrapper>> wrappedTextConsumer() {
        return message -> log.info("Consuming TextWrapper message: {}", message);
    }
}
