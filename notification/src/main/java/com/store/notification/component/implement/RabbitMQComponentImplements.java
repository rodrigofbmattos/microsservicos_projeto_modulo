package com.store.notification.component.implement;

import com.store.notification.component.RabbitMQComponent;
import com.store.notification.service.implement.EmailServiceImplements;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class RabbitMQComponentImplements implements RabbitMQComponent  {
    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Autowired
    private EmailServiceImplements emailServiceImplements;

    private final WebClient webClient;

    // Conversa com o serviço de Authentication via HTTP Request (através da URL criada no desenvolvimento de Authentication)
    public RabbitMQComponentImplements(WebClient webClient) {
        this.webClient = webClient;
    }

    @RabbitListener(queues = "order_notification") // Enxerga a fila e quando houver um item e a aplicação recebe
    // Faz uma Busca no serviço de Authentication via HTTP Request (neste caso busca o usuário: User de Authentication)
    public void handleMessage(String message) {
        Map<String, Object> object = emailServiceImplements.convertToObjet(message);

        //int order_id = (int) object.get("order_id");
        int user_id = (int) object.get("user_id");
        String products_name = (String) object.get("products_name");
        //double total_value = (double) object.get("total_value");

        String response = this.webClient.get()
                                            .uri("/user/" + String.valueOf(user_id)) // Parte final da URL (do Athentication)
                                            .retrieve() // Recuperação da URL
                                            .bodyToMono(String.class) // Para retornar somente um item
                                            .block();

        Map<String, Object> user = emailServiceImplements.convertToObjet(response);
        String email = (String) user.get("email");

        String content = emailServiceImplements.constructOrderContent(products_name, (String) user.get("name"));

        emailServiceImplements.sendEmail(content, email, "E-Commerce Infnet");

//        System.out.println("Mensagem enviada com sucesso!");
//        System.out.println(response);
    }
}
