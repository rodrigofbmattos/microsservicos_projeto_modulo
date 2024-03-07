package com.store.order.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity // Informa que Order é uma entidade
@Table(name = "`order`") // Nome da tabela do BD a ser criada relacionada com esta classe
@Getter // Cria os Getters
@Setter // Cria os Setters
@NoArgsConstructor // Cria um construtor sem nenhum arqumento
@AllArgsConstructor // Cria um contrutor com todos os argumentos

// Permite relacionamento entre Order e OrderItem
// Propriedade Id é a propriedade de relacionamento de Order
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // Utilizado para relacionamentos de BD
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String status_order;

    // Id do User
    @Column(nullable = false, updatable = false)
    private Long user_id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private Double total_value;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist // É usado para marcar um método em uma entidade que deve ser executado logo antes da entidade ser persistida pela primeira vez
    protected void onCreate() { createdAt = LocalDateTime.now(); }
}
