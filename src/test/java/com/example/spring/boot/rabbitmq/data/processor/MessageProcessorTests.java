package com.example.spring.boot.rabbitmq.data.processor;

import com.example.spring.boot.rabbitmq.business.TextService;
import com.example.spring.boot.rabbitmq.vo.TextWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.messaging.Message;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageProcessorTests {

    private TextService textServiceMock;

    private MessageProcessor messageProcessor;

    @BeforeEach
    void beforeEach() {
        this.textServiceMock = Mockito.mock(TextService.class);
        this.messageProcessor = new MessageProcessor(this.textServiceMock);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"racecar", "radar", "able was I ere I saw elba"})
    void textToTextWrapperProcessor__shouldWrapInputAndPublishIt(String testCase) {

        BDDMockito.given(this.textServiceMock.wrapText(testCase)).willReturn(TextWrapper.builder().text(testCase).build());

        Message<TextWrapper> testResult = this.messageProcessor.textToTextWrapperProcessor().apply(testCase);

        assertThat(testResult.getHeaders()).containsEntry("hello", "world!");
        assertThat(testResult.getPayload()).isEqualTo(TextWrapper.builder().text(testCase).build());
        BDDMockito.then(this.textServiceMock).should().wrapText(testCase);
    }
}
