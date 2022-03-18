package com.comp62542.checkout.repositories;

import java.util.List;

public interface IRepository<T> {
    List<T> getAll(Class<T> classType);
    void addAll(T... items);
    void deleteAll(T... items);
    T get(Class<T> classType, String id);
    void add(T item);
    void delete(T item);
    void saveChanges();
    String getDocumentId(T document);
    void refresh(T entity);
}
