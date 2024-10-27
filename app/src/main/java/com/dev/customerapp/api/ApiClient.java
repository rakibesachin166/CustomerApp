package com.dev.customerapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //    private static final String BASE_URL = "https://192.168.26.184/api/";

    public static final String BASE_URL = "http://192.168.26.184/CustomerAppApi";
    private static final String API_URL = BASE_URL + "/index.php/api/";
    private static ApiService apiService;

    public static ApiService getRetrofitInstance() {
        if (apiService == null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();


            apiService = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService.class);
        }
        return apiService;
    }


}