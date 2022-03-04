package com.example.spring.boot.rabbitmq.integration;

import com.example.spring.boot.rabbitmq.MessagingExampleApplication;
import com.example.spring.boot.rabbitmq.vo.TextWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessagingIntegrationTests {

    private static final String TEST_PAYLOAD = "hello";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void testMultipleFunctions() throws IOException {

        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(MessagingExampleApplication.class)).run()) {

            InputDestination inputDestination = context.getBean(InputDestination.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<String> textMessageIn = MessageBuilder.withPayload(TEST_PAYLOAD).build();

            inputDestination.send(textMessageIn, "text");

            Message<byte[]> outputMessage = outputDestination.receive(0, "text.wrapped");
            TextWrapper outputPayload = OBJECT_MAPPER.readValue(outputMessage.getPayload(), new TypeReference<TextWrapper>() {
            });
            assertThat(outputPayload).isEqualTo(TextWrapper.builder().text(TEST_PAYLOAD).build());
        }
    }
}
