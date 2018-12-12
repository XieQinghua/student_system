package com.stu.system.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


public class LogInterceptor implements Interceptor {
    private static final String TAG = "ApiLog";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.i(TAG, String.format("%1$s->%2$s", request.method(), java.net.URLDecoder.decode(String.valueOf(request.url()), "UTF-8")));
        if (request.headers() != null) {
            Log.i(TAG, "RequestHeaders:" + request.headers());
        }
        if (request.body() != null) {
            Log.i(TAG, "RequestBody:" + java.net.URLDecoder.decode(bodyToString(request.body()), "UTF-8"));
        }

        Response response = chain.proceed(chain.request());
        MediaType mediaType = response.body().contentType();
        String responseBody = response.body().string();
        Log.e(TAG, "ResponseBody:" + responseBody);

        return response.newBuilder()
                .body(ResponseBody.create(mediaType, responseBody))
                .build();
    }

    private String bodyToString(final RequestBody request) {
        if (request != null) {
            try {
                final RequestBody copy = request;
                final Buffer buffer = new Buffer();
                copy.writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                Log.e(TAG, e.toString());
            }
        }
        return null;
    }
}
