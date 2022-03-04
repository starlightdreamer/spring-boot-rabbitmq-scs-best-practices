package com.example.spring.boot.rabbitmq.data.publisher;

public interface Publisher {

    <T> void publish(T data);
}
