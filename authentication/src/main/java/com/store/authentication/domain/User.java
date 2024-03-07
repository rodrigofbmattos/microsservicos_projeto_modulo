package com.store.authentication.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // Informa que User é uma entidade
@Table(name = "`user`") // Nome da tabela do BD a ser criada relacionada com esta classe
@Getter // Cria os Getters
@Setter // Cria os Setters
@NoArgsConstructor // Cria um construtor sem nenhum arqumento
@AllArgsConstructor // Cria um contrutor com todos os argumentos

// Permite relacionamento entre Company e User
// Propriedade Id é a propriedade de relacionamento de Company
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // Utilizado para relacionamentos de BD

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = false, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY, optional = true) // Muitos pra um: Uma company, muitos user; um user, uma company
    @JoinColumn(name = "company_id") // Cria a foreign key na tabela user, que referencia a tabela company
    // Retorna no Json quando fizer a listagem
    // Faz listar a Company e os User no mesmo Get
    @JsonBackReference
    private Company company;
}