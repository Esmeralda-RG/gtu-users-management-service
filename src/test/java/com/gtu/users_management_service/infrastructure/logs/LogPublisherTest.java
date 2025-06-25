package com.gtu.users_management_service.infrastructure.logs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class LogPublisherTest {

    @Mock
    private AmqpTemplate amqpTemplate;

    private LogPublisher logPublisher;

    @BeforeEach
    void setUp() {
        logPublisher = new LogPublisher(amqpTemplate);
        ReflectionTestUtils.setField(logPublisher, "exchange", "log.exchange");
        ReflectionTestUtils.setField(logPublisher, "routingKey", "log.routingkey");
    }

    @Test
    void testSendLogSuccess() {
        String timestamp = "2025-06-24T18:43:39Z";
        String service = "email-service";
        String level = "INFO";
        String message = "Test log message";
        Map<String, Object> details = Map.of("key", "value");

        logPublisher.sendLog(timestamp, service, level, message, details);

        verify(amqpTemplate, times(1)).convertAndSend(eq("log.exchange"), eq("log.routingkey"), anyString());
    }

    @Test
    void testSendLogFailure() {
        doThrow(new RuntimeException("Test exception")).when(amqpTemplate).convertAndSend(anyString(), anyString(),
                anyString());

        String timestamp = "2025-06-24T18:43:39Z";
        String service = "email-service";
        String level = "ERROR";
        String message = "Test log failure";
        Map<String, Object> details = Map.of("error", "details");

        logPublisher.sendLog(timestamp, service, level, message, details);

        verify(amqpTemplate, times(1)).convertAndSend(eq("log.exchange"), eq("log.routingkey"), anyString());
    }
}