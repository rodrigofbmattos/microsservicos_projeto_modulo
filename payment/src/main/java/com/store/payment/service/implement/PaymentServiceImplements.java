package com.store.payment.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.store.payment.domain.Payment;
import com.store.payment.repository.PaymentRepository;
import com.store.payment.service.PaymentService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PaymentServiceImplements extends GenericServiceImplements<Payment, Long, PaymentRepository> implements PaymentService {
    private final WebClient webClient;

    @Value("${rabbitmq.exchange.name}")
    private String exchangePayed;

    @Value("${rabbitmq.routing.key}")
    private String routingKeyPayed;

    private final AmqpTemplate rabbitTemplate;

    public PaymentServiceImplements(PaymentRepository repository, WebClient webClient, AmqpTemplate rabbitTemplate) {
        super(repository);
        this.webClient = webClient;
        this.rabbitTemplate = rabbitTemplate;
    }
    private void sendNotification(Payment payment) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.registerModule(new JavaTimeModule()); // Converte o objeto Json  para string para fazer parse na data

            String json = mapper.writeValueAsString(payment); // Converte o Json para string

            rabbitTemplate.convertAndSend(exchangePayed, routingKeyPayed, json); // Pega a mensagem e envia para o Exchange em formato de string
        } catch (JsonProcessingException e) {
            System.out.println("Erro: " + e);
        }
    }

//    private void sendOrderToPayment(Order order) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//
//            mapper.registerModule(new JavaTimeModule()); // Converte o objeto Json  para string para fazer parse na data
//
//            String json = mapper.writeValueAsString(order); // Converte o Json para string
//
//            //String begin = json.substring(0, 1);
//            //String middle = "\"products_name\" :\"Produto 1, Produto 2, Produto 3\",";
//            //String end = json.substring(1);
//
//            //json = begin + middle + end;
//
//            //convert the JSON string to a Java object
//            //order = mapper.readValue(json, Order.class);
//
//            rabbitTemplate.convertAndSend(exchangePayed, routingKeyPayed, json); // Pega a mensagem e envia para o Exchange em formato de string
//        } catch (JsonProcessingException e) {
//            System.out.println("Erro: " + e);
//        }
//    }
//
//    private void sendNotificationOrder(Order order) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//
//            mapper.registerModule(new JavaTimeModule()); // Converte o objeto Json  para string para fazer parse na data
//
//            String json = mapper.writeValueAsString(order); // Converte o Json para string
//
//            //String begin = json.substring(0, 1);
//            //String middle = "\"products_name\" :\"Produto 1, Produto 2, Produto 3\",";
//            //String end = json.substring(1);
//
//            //json = begin + middle + end;
//
//            //convert the JSON string to a Java object
//            //order = mapper.readValue(json, Order.class);
//
//            rabbitTemplate.convertAndSend(exchangePayed, routingKeyPayed, json); // Pega a mensagem e envia para o Exchange em formato de string
//        } catch (JsonProcessingException e) {
//            System.out.println("Erro: " + e);
//        }
//    }
}
