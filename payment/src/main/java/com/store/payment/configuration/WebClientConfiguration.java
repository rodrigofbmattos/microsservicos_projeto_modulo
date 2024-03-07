package com.store.payment.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    // Conversa com o serviço de Authentication via HTTP Request (através da URL criada no desenvolvimento de Authentication)
    public WebClient webCliente(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8088/api").build(); // Como se comunica com o User (Authentication), deixar essa base (porta 8088 do Notification)
    }
}
