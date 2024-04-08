package com.example.lab7.Model;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    String DOMAIN = "http://192.168.1.75:3000/";

    @GET("get-page-fruits")
    Call<Page<ArrayList<Fruits>>> getPaageFruits(@Header("Authorization") String token, @Query("page") int page);
}
