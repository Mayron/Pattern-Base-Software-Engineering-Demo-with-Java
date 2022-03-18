package com.comp62542.checkout.observers;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSubject<T> implements ISubject<T> {
    final List<IObserver<T>> observers = new ArrayList<>();

    public final void register(IObserver<T> observer) {
        this.observers.add(observer);
        observer.setSubject(this);
    }

    public final void unregister(IObserver<T> observer) {
        this.observers.remove(observer);
        observer.setSubject(null);
    }

    public void notifyObservers(T state) {
        List<IObserver<T>> copiedList = new ArrayList<>(this.observers);
        for (IObserver<T> observer : copiedList) {
            observer.update(state);
        }
    }
}