package com.example.spring.boot.rabbitmq.data.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessagePublisher implements Publisher {

    private final StreamBridge streamBridge;

    private final String bindingName;

    public MessagePublisher(StreamBridge streamBridge, @Value("${exampleApp.publisher.bindingName}") String bindingName) {
        this.streamBridge = streamBridge;
        this.bindingName = bindingName;
    }

    public <T> void publish(T data) {
        log.info("Publishing data: {}", data);

        try {
            if (!this.streamBridge.send(bindingName, data)) {
                String errorMsg = String
                        .format("StreamBridge failed to send data %s to bindingName %s, but didn't throw an exception.",
                                data, bindingName);
                log.error(errorMsg);
                throw new PublishException(errorMsg);
            }
        } catch (RuntimeException e) {
            String errorMsg = String.format("StreamBridge send of %s to %s failed due to cause: %s", data, bindingName, e.getCause());
            log.error(errorMsg, e);
            throw new PublishException(errorMsg, e);
        }
        log.info("Published successfully: {}", data);
    }
}
