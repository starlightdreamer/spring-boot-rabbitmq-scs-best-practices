package com.example.spring.boot.rabbitmq.data.publisher;

import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.MessageDeliveryException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
public class MessagePublisherTests {

    private static final String TEST_BINDING_NAME = "bindingName";

    private final List<String> TEST_DATA = Collections.singletonList("1");

    private StreamBridge mockStreamBridge;

    private Publisher messagePublisher;

    @BeforeEach
    void beforeEach() {
        this.mockStreamBridge = Mockito.mock(StreamBridge.class);
        this.messagePublisher = new MessagePublisher(this.mockStreamBridge, TEST_BINDING_NAME);
    }

    @AfterEach
    void afterEach() {
        BDDMockito.then(this.mockStreamBridge).should().send(TEST_BINDING_NAME, TEST_DATA);
    }

    @Test
    void publish__shouldPublish_WhenStreamBridgeReturnsTrue() {
        BDDMockito.given(this.mockStreamBridge.send(TEST_BINDING_NAME, TEST_DATA)).willReturn(true);
        assertThatNoException().isThrownBy(() -> this.messagePublisher.publish(TEST_DATA));
    }

    @Test
    void publish__shouldThrowPublisherException_WhenStreamBridgeReturnsFalse() {
        BDDMockito.given(this.mockStreamBridge.send(TEST_BINDING_NAME, TEST_DATA)).willReturn(false);
        assertThatExceptionOfType(PublishException.class).isThrownBy(() -> this.messagePublisher.publish(TEST_DATA));
    }

    @Test
    void publish__shouldThrowPublisherException_WhenStreamBridge_ThrowsAnyRuntimeException() {
        BDDMockito.given(this.mockStreamBridge.send(TEST_BINDING_NAME, TEST_DATA)).willThrow(MessageDeliveryException.class);
        assertThatExceptionOfType(PublishException.class).isThrownBy(() -> this.messagePublisher.publish(TEST_DATA));
    }

}
