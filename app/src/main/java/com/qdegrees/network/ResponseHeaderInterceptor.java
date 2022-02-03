package com.qdegrees.network;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ResponseHeaderInterceptor implements Interceptor {
    private final Context context;
    public ResponseHeaderInterceptor( Context context1){

        this.context = context1;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if(response.code()==401){

            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
        }
        return response;
    }
}
