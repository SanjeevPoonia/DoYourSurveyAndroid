package com.qdegrees.network;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qdegrees.doyoursurvey.DoYourSurvey;
import com.qdegrees.utils.Constants;
import com.qdegrees.utils.ProgressRequestBody;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class RetrofitAPIClient {
    public static ApiService getRetrofitClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient())
                .build()
                .create(ApiService.class);
    }
    public static OkHttpClient okHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpClient.sslSocketFactory(Objects.requireNonNull(getSSLSocketFactory()));
        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });


        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder newRequest = originalRequest.newBuilder();
                newRequest.header("Content-Type", "application/json");
                originalRequest = newRequest.build();
                Response response = chain.proceed(originalRequest);
                int responseCode = response.code();
                if(response.code()==401){
                    Log.e("error",response.message());
                    //Toast.makeText(DoYourSurvey.getApp(), response.message(), Toast.LENGTH_SHORT).show();
                }
                return response;
            }
        });

        // Log the request only in debug mode.
        /*  if (BuildConfig.DEBUG)*/
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // set the connection time to 1 minutes
        httpClient.protocols( Collections.singletonList(Protocol.HTTP_1_1) );
        httpClient.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .addInterceptor(httpLoggingInterceptor);
        return httpClient.build();
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return sslSocketFactory;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }

    }

    public static MultipartBody.Part getImageFilePartProgress(ProgressRequestBody fileBody){
        return MultipartBody.Part.createFormData("image",("dys" + System.currentTimeMillis()+".jpg"),fileBody);
    }
}
