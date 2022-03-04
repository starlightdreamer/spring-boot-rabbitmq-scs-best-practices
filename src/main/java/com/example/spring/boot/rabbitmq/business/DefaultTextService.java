package com.example.spring.boot.rabbitmq.business;

import com.example.spring.boot.rabbitmq.vo.TextWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultTextService implements TextService {

    public TextWrapper wrapText(String text) {
        return TextWrapper.builder().text(text).build();
    }

}
