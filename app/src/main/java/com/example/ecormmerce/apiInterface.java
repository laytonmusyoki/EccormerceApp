package com.example.ecormmerce;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiInterface {
    @POST("signin/")
    Call<LoginResponse> getLogged(@Body LoginData loginData);


    @POST("register/")
    Call<RegisterResponse> postRegisterData(@Body RegisterData registerData);
}

