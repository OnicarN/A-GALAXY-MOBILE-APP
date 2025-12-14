package com.polancodev.galaxyjourney;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiRetrofit {
    private static final String BASE_URL = "http://pabloglezs.es/";
    private static Retrofit retrofit;


    public MiRetrofit() {

    }

    // Devuelve siempre la misma instancia de Retrofit
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static ApiMissionService getApiService() {
        return getInstance().create(ApiMissionService.class);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        MiRetrofit.retrofit = retrofit;
    }
}
