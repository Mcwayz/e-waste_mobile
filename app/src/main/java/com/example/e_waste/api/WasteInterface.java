package com.example.e_waste.api;
import com.example.e_waste.model.collections.CollectionRequest;
import com.example.e_waste.model.collections.CollectionResponse;
import com.example.e_waste.model.profile.ProfileRequest;
import com.example.e_waste.model.profile.ProfileResponse;
import com.example.e_waste.model.subscriptions.SubscriptionRequest;
import com.example.e_waste.model.subscriptions.SubscriptionResponse;
import com.example.e_waste.model.authentication.TokenRequest;
import com.example.e_waste.model.authentication.TokenResponse;
import com.example.e_waste.model.profile.UserRequest;
import com.example.e_waste.model.profile.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WasteInterface {

    String base_url = "http://192.168.8.111:8000";
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

    // Profile Endpoint
    @POST
    Call<ProfileResponse> updateProfile(@Body ProfileRequest profileRequest);
}
