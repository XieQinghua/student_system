package com.stu.system.http;

import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ApiObserver<T> implements Observer<T> {

    private static final String TAG = "ApiLog";

    private ApiCallback<T> mCallback;

    private boolean isLog = true;

    public ApiObserver(ApiCallback<T> callback) {
        this.mCallback = callback;
    }

    public ApiObserver(ApiCallback<T> callback, String tag) {
        this(callback, true);
    }

    public ApiObserver(ApiCallback<T> callback, boolean isLog) {
        this.mCallback = callback;
        this.isLog = isLog;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        if (isLog) {
            Log.e(TAG, e.toString());
        }
        if (mCallback != null) {
            mCallback.onError(e);
        }
    }

    @Override
    public void onNext(T t) {
        if (isLog) {
            //LogUtils.e(TAG, "Response:" + t);
        }
        if (mCallback != null) {
            mCallback.onNext(t);
        }
    }

    @Override
    public void onComplete() {
        if (mCallback != null) {
            mCallback.onCompleted();
        }
    }

    public static <T> ApiObserver<T> getApiObserver(ApiCallback<T> callback) {
        return new ApiObserver<>(callback);
    }

    public static <T> void subscribe(Observable<T> observable, ApiCallback<T> callback) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getApiObserver(callback));
    }

}
