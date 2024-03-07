package com.store.payment.component.implement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.store.payment.component.RabbitMQComponent;
//import com.store.payment.service.implement.EmailServiceImplements;
import com.store.payment.util.Conversor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

@Component
public class RabbitMQComponentImplements implements RabbitMQComponent  {
    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final AmqpTemplate rabbitTemplate;

    private WebClient webClient;

    // Conversa com o serviço de Authentication via HTTP Request (através da URL criada no desenvolvimento de Authentication)
    public RabbitMQComponentImplements(AmqpTemplate rabbitTemplate, WebClient webClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.webClient = webClient;
    }

    @RabbitListener(queues = "to_pay_notification") // Enxerga a fila e quando houver um item e a aplicação recebe
    // Faz uma Busca no serviço de Authentication via HTTP Request (neste caso busca o usuário: User de Authentication)
    public void handleMessageOrderPayed(String message) throws IOException {
        Map<String, Object> object = Conversor.convertToObject(message);

        String response;
        String payment_status = "PAYED";

        int user_id = (int) object.get("user_id");
        int order_id = (int) object.get("id");

        object.put("status_order", payment_status);

        // Busca o User
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8088/api")
                .build();

        response = this.webClient.get()
                .uri("/user/" + String.valueOf(user_id)) // Parte final da URL (do Athentication)
                .retrieve() // Recuperação da URL
                .bodyToMono(String.class) // Para retornar somente um item
                .block();

        Map<String, Object> user = Conversor.convertToObject(response);

        // Recupera a Order
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8084/api")
                .build();

        response = this.webClient.get()
                .uri("/order/" + String.valueOf(order_id)) // Parte final da URL (do Athentication)
                .retrieve() // Recuperação da URL
                .bodyToMono(String.class) // Para retornar somente um item
                .block();

        Map<String, Object> order = Conversor.convertToObject(response);

        // Recupera a Order
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8084/api")
                .build();

        response = this.webClient.get()
                .uri("/order_item/" + String.valueOf(order_id)) // Parte final da URL (do Athentication)
                .retrieve() // Recuperação da URL
                .bodyToMono(String.class) // Para retornar somente um item
                .block();

        Map<String, Object> orderItem = Conversor.convertToObject(response);


        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(object);

        rabbitTemplate.convertAndSend(exchange, routingKey, json); // Pega a mensagem e envia para o Exchange em formato de string

        System.out.println("Mensagem enviada com sucesso!");
        // TODO: Identificar o produto
    }

//    private void sendOrderToPayed(String message) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//
//            mapper.registerModule(new JavaTimeModule()); // Converte o objeto Json  para string para fazer parse na data
//
//            String json = mapper.writeValueAsString(message); // Converte o Json para string
//
//            rabbitTemplate.convertAndSend(exchange, routingKey, json); // Pega a mensagem e envia para o Exchange em formato de string
//        } catch (JsonProcessingException e) {
//            System.out.println("Erro: " + e);
//        }
//    }
}
