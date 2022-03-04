package com.example.spring.boot.rabbitmq.business;

import com.example.spring.boot.rabbitmq.vo.TextWrapper;

public interface TextService {

    TextWrapper wrapText(String text);

    <T> void publishData(T data);
}
