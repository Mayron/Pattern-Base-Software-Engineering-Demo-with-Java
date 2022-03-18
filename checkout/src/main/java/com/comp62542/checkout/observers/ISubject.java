package com.comp62542.checkout.observers;

public interface ISubject<T> {
    void register(IObserver<T> observer);
    void unregister(IObserver<T> observer);
    void notifyObservers(T state);
}
