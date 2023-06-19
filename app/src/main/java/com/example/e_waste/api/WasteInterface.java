package com.example.e_waste.api;
import com.example.e_waste.model.collections.Collection;
import com.example.e_waste.model.collections.CollectionRequest;
import com.example.e_waste.model.collections.CollectionResponse;
import com.example.e_waste.model.profile.DetailsRequest;
import com.example.e_waste.model.profile.DetailsResponse;
import com.example.e_waste.model.profile.ProfileRequest;
import com.example.e_waste.model.profile.ProfileResponse;
import com.example.e_waste.model.subscriptions.SubsRequest;
import com.example.e_waste.model.subscriptions.SubsResponse;
import com.example.e_waste.model.subscriptions.Subscription;
import com.example.e_waste.model.subscriptions.SubscriptionRequest;
import com.example.e_waste.model.subscriptions.SubscriptionResponse;
import com.example.e_waste.model.authentication.TokenRequest;
import com.example.e_waste.model.authentication.TokenResponse;
import com.example.e_waste.model.profile.UserRequest;
import com.example.e_waste.model.profile.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WasteInterface {

    String base_url = "http://192.168.8.107:8000";
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
    @POST("/api/add-profile")
    Call<ProfileResponse> updateProfile(@Body ProfileRequest profileRequest);

    // Profile Details Endpoint
    @GET("/api/profile-details/{authId}/")
    Call<DetailsResponse> getProfile(@Path("authId") int authId);
    // My Collections Endpoint
    @GET("/api/my-requests/{authId}/")
    Call<List<Collection>>  getCollections(@Path("authId") int authId);

}
