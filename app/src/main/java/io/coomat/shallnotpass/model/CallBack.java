package io.coomat.shallnotpass.model;

public interface CallBack<T> {
    void fire(T data);
}
