package com.example.marmm.reminderdemotest;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NumbersApiService {

    String BASE_URL = "http://numbersapi.com/";


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // The string in the GET annotation is added to the BASE_URL
    @GET("/{month}/{dayOfMonth}/date?json")

    // The query is added after ?json, for example it could look like '?json&max=999'
    Call<DayQuoteItem> getTodaysQuote(@Path("month") int monthNumber, @Path("dayOfMonth") int dayOfMonth);
}


