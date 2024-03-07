package com.store.notification.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClienteConfiguration {
    @Bean
    // Conversa com o serviço de Authentication via HTTP Request (através da URL criada no desenvolvimento de Authentication)
    public WebClient webCliente(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8088/api").build();
    }
}
