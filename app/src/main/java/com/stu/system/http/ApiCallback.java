package com.stu.system.http;


public interface ApiCallback<T> {

    void onNext(T t);

    void onError(Throwable e);

    void onCompleted();
}
