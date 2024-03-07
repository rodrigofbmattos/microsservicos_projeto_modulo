package com.store.order.service.implement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.store.order.domain.Order;
import com.store.order.domain.OrderItem;
import com.store.order.repository.OrderItemRepository;
import com.store.order.repository.OrderRepository;
import com.store.order.service.GenericService;
import com.store.order.service.OrderService;
import com.store.order.util.Conversor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
@Component
public class OrderServiceImplements extends GenericServiceImplements<Order, Long, OrderRepository>  implements OrderService {
    private WebClient webClient;
    private final WebClient webClientProduct;

    List<Long> product_id = new ArrayList<>();

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.exchange.name.to.pay}")
    private String exchangeToPay;

    @Value("${rabbitmq.routing.key.to.pay}")
    private String routingKeyToPay;

    private final AmqpTemplate rabbitTemplate;

    public OrderServiceImplements(OrderRepository repository, WebClient webClient, WebClient webClientProduct, AmqpTemplate rabbitTemplate) {
        super(repository);
        this.webClient = webClient;
        this.webClientProduct = webClientProduct;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void save(Order order) {

        System.out.println("Método Save do Order.");
        try {
            if (order.getStatus_order().equals("SHOPPING_CART")) {
                this.webClient.get()
                        .uri("/user/" + String.valueOf(order.getUser_id()))
                        .accept(MediaType.APPLICATION_JSON)
                        .exchangeToMono(response -> {
                            int i = 0;
                            if (response.statusCode().equals(HttpStatus.OK)) {
                                Order o = repository.save(order);
                                //repository.save(o);
                                for (OrderItem item: order.getOrderItems()) {
                                    OrderItem orderItem = o.getOrderItems().get(i);

                                    orderItem.setOrder(o);
                                    orderItem.setProduct_id(item.getProduct_id());

                                    product_id.add(item.getProduct_id());

                                    orderItemRepository.save(orderItem);
                                    i++;
                                }

                                this.sendNotification(o, exchangeToPay, routingKeyToPay); // Envia a Notification para o User
                                return response.toEntity(String.class);
                            }
                            else if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                                System.out.println("Não há usuário com esse ID.");
                                return response.toEntity(String.class);
                            }
                            else {
                                return response.createError();
                            }
                        }).block();
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    // Envia a Notification para o User
    private void sendNotification(Order order, String exchange, String routingKey) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule()); // Converte o objeto Json  para string para fazer parse na data

            String json = mapper.writeValueAsString(order); // Converte o Json para string

            rabbitTemplate.convertAndSend(exchange, routingKey, json); // Pega a mensagem e envia para o Exchange em formato de string
        } catch (JsonProcessingException e) {
            System.out.println("Erro: " + e);
        }
    }

    // Envia a Notification do Payment para o User
    private void sendOrderToPayment(Order order) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.registerModule(new JavaTimeModule()); // Converte o objeto Json  para string para fazer parse na data

            String json = mapper.writeValueAsString(order); // Converte o Json para string

            rabbitTemplate.convertAndSend(exchangeToPay, routingKeyToPay, json); // Pega a mensagem e envia para o Exchange em formato de string
        } catch (JsonProcessingException e) {
            System.out.println("Erro: " + e);
        }
    }
}
