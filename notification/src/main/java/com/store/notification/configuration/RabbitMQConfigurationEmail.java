package com.store.notification.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit // Habilita o RabbitMQ para as configurações abaixo
public class RabbitMQConfigurationEmail {

    // Pega os valores do arquivo properties
    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange; // Padrão do RabbitMQ, é um componente de despacho de mensagem

    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    //

    // @Bean: Quando temos alguma classe que não é nossa (da aplicação) e queremos que o Spring também consiga a injetar

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
