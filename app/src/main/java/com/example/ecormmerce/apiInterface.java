package com.example.ecormmerce;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface apiInterface {
    @POST("signin/")
    Call<LoginResponse> getLogged(@Body LoginData loginData);


    @POST("register/")
    Call<RegisterResponse> postRegisterData(@Body RegisterData registerData);

    @GET("products/")
    Call<ProductsList> getProducts();

    @POST("cart/")
    Call<CartList> getCartProducts(@Header("Authorization") String accessToken);


    @POST("updateItem/")
    Call<AddItemResponse> postCartId(@Header("Authorization") String accessToken,@Body CartID cartID);
}

