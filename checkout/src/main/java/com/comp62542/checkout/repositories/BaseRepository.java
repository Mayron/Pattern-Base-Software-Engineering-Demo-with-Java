package com.comp62542.checkout.repositories;

import com.comp62542.checkout.singletons.DatabaseSingleton;
import net.ravendb.client.documents.session.IDocumentSession;
import java.util.List;

public class BaseRepository<T> implements IRepository<T> {

    protected final IDocumentSession session;

    public BaseRepository() {
        this.session = DatabaseSingleton.createSession();
    }

    @Override
    public String getDocumentId(T document) {
        return session.advanced().getDocumentId(document);
    }

    @Override
    public void refresh(T entity) {
        session.advanced().refresh(entity);
    }

    @Override
    public List<T> getAll(Class<T> classType) {
        return session.query(classType).toList();
    }

    @Override
    public void addAll(T[] items) {
        for (T item : items) {
            session.store(item);
        }
    }

    @Override
    public void deleteAll(T[] items) {
        for (T item : items) {
            session.delete(item);
        }
    }

    @Override
    public T get(Class<T> classType, String id) {
        return session.load(classType, id);
    }

    @Override
    public void add(T item) {
        session.advanced().storeIdentifier();
        session.store(item);
    }

    @Override
    public void delete(T item) {
        session.delete(item);
    }

    @Override
    public void saveChanges() {
        session.saveChanges();
    }
}
