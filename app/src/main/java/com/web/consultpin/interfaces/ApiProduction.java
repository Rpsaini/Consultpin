package com.web.consultpin.interfaces;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiProduction {
    // private static final String BASE_URL = "https://api.stackexchange.com/2.2/";
    private final Context context;

    private ApiProduction(Context context) {
        this.context = context;
    }

    public static ApiProduction getInstance(AppCompatActivity context) {
        return new ApiProduction(context);
    }

    private Retrofit provideRestAdapter() {


        return new Retrofit.Builder()
                .baseUrl("http://52.66.238.215/v1/")
                .client(OkHttpProduction.getOkHttpClient(context, true))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .build();
    }

    public <S> S provideService(Class<S> serviceClass) {
        return provideRestAdapter().create(serviceClass);
    }
}