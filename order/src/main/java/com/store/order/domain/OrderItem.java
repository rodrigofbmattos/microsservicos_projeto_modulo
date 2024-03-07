package com.store.order.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity // Informa que OrderItem é uma entidade
@Table(name = "order_item") // Nome da tabela do BD a ser criada relacionada com esta classe
@Getter // Cria os Getters
@Setter // Cria os Setters
@NoArgsConstructor // Cria um construtor sem nenhum arqumento
@AllArgsConstructor // Cria um contrutor com todos os argumentos
@EqualsAndHashCode(of = "id")

// Permite relacionamento entre OrderItem e Item
// Propriedade Id é a propriedade de relacionamento de OrderItem
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // Utilizado para relacionamentos de BD
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long product_id;
    // OBS.: É com ele que vai, ao fechar o Pedido (Order), se comunica com o microsserivço de Produto, pegar o nome do Produto e mandar ele no Json

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_id")
    // Retorna no Json quando fizer a listagem
    // Faz listar a Order e os OrdeItem no mesmo Get
    @JsonBackReference
    private Order order;
}
