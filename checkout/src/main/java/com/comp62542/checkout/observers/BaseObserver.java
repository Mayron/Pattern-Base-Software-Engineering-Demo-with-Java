package com.comp62542.checkout.observers;

public abstract class BaseObserver<T> implements IObserver<T> {
    protected ISubject<T> subject;

    @Override
    public abstract void update(T state);

    @Override
    public final void setSubject(ISubject<T> subject) {
        this.subject = subject;
    }

    @Override
    public final ISubject<T> getSubject() {
        return this.subject;
    }
}
