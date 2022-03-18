package com.comp62542.checkout.observers;

public interface IObserver<T> {
    void update(T state);
    void setSubject(ISubject<T> subject);
    ISubject<T> getSubject();
}
