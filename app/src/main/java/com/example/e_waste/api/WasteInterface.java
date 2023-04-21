package com.example.e_waste.api;
import com.example.e_waste.model.CollectionRequest;
import com.example.e_waste.model.CollectionResponse;
import com.example.e_waste.model.SubscriptionRequest;
import com.example.e_waste.model.SubscriptionResponse;
import com.example.e_waste.model.TokenRequest;
import com.example.e_waste.model.TokenResponse;
import com.example.e_waste.model.UserRequest;
import com.example.e_waste.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WasteInterface {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    // Get Waste Types
    @GET("/api/waste-types")
    Call<String> getWasteType();

    // Registration Endpoint
    @POST("/api/add-user/")
    Call<UserResponse> getUser(@Body UserRequest userRequest);

    // Collections Endpoint
    @POST("/api/add-collection")
    Call<CollectionResponse> getCollection(@Body CollectionRequest collectionRequest);

    // Subscription Endpoint
    @POST("/api/add-subscription")
    Call<SubscriptionResponse> getSubscription(@Body SubscriptionRequest subRequest);

    // Login Endpoint
    @POST("/api/token/")
    Call<TokenResponse> getAuthToken(@Body TokenRequest tokenRequest);
}
