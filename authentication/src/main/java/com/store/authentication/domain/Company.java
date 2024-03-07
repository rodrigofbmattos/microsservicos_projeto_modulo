package com.store.authentication.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity // Informa que Company é uma entidade
@Table(name = "company") // Nome da tabela do BD a ser criada relacionada com esta classe
@Getter // Cria os Getters
@Setter // Cria os Setters
@NoArgsConstructor // Cria um construtor sem nenhum arqumento
@AllArgsConstructor // Cria um contrutor com todos os argumentos

// Permite relacionamento entre Company e User
// Propriedade Id é a propriedade de relacionamento de Company
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // Utilizado para relacionamentos de BD
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true) // Mapeamento para ser exibido na listagem de company
    private Set<User> users = new HashSet<>();
}
