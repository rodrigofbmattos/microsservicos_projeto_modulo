package com.store.order.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfiguration {
    // Pega os valores do arquivo properties
    @Value("${rabbitmq.queue.name.payed}")
    private String queue;

    @Value("${rabbitmq.exchange.name.payed}")
    private String exchange; // Padrão do RabbitMQ, é um componente de despacho de mensagem

    @Value("${rabbitmq.routing.key.payed}")
    private String routingKey;
    //

    // Iniciação da fila
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    // Inicia o tópico
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    // Faz a fila funcionar, faz a construção da fila para ser enxergada
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())     // Construção da queue
                .to(exchange())    // Aponta a queue para o Exchange
                .with(routingKey); // Passa  rota da fila
    }
}
