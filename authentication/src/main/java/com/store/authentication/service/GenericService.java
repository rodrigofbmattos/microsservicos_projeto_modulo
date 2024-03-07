package com.store.authentication.service;

import com.store.authentication.domain.User;

import java.util.List;

// Interface que vai exigir que o Service Impements implemente a minha Interface
// T é um conceito para dizer que esta interface irá receber um objeto genérico
public interface GenericService<T> {
    List<T> getAll();

    T get(Long id, String noSuchElementException);

    void save(T entity);

    void update(T entity);

    void delete(Long id);
}
