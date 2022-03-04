package com.example.spring.boot.rabbitmq.business;

import com.example.spring.boot.rabbitmq.data.publisher.MessagePublisher;
import com.example.spring.boot.rabbitmq.vo.TextWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultTextService implements TextService {

    private final MessagePublisher messagePublisher;

    public TextWrapper wrapText(String text) {
        return TextWrapper.builder().text(text).build();
    }

    public <T> void publishData(T data) {
        this.messagePublisher.publish(data);
    }
}
