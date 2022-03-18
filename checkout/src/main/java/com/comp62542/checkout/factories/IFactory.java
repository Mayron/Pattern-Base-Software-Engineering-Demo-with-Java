package com.comp62542.checkout.factories;

public interface IFactory<T1, T2> {
    T1 create(T2 value);
}
