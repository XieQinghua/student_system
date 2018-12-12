package com.stu.system.http;


public abstract class SimpleCallback<T> implements ApiCallback<T>{

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onCompleted() {

    }
}
