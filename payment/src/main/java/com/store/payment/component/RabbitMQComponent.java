package com.store.payment.component;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface RabbitMQComponent {
    void handleMessageOrderPayed(String message) throws IOException;
}
