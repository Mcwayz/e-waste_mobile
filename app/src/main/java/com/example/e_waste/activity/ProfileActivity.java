package com.example.e_waste.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.R;
import com.example.e_waste.model.authentication.Auth;
import com.example.e_waste.model.profile.ProfileRequest;
import com.example.e_waste.model.profile.ProfileResponse;
import com.example.e_waste.service.ApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private Dialog dialog;

    private String auth_id;

    private Button update;
    private EditText p_address, position;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        p_address = findViewById(R.id.address);
        position = findViewById(R.id.location);
        update = findViewById(R.id.btn_update);
        Auth auth = new Auth(getApplicationContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        auth.startRunnable();
        String token = auth.getToken();

        if (token.isEmpty()) {
            Log.e("TAG", "No auth token found in shared preferences");
            return;
        }
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> claims = jwt.getClaims();
            auth_id = String.valueOf(claims.get("user_id"));
            Log.d("TAG", "Auth ID: " + auth_id);
            String subjectWithoutQuotes = auth_id.replace("\"", "");
        } catch (JWTDecodeException exception){
            Log.e("TAG", "Invalid JWT token: " + exception.getMessage());
        }


        update.setOnClickListener(v -> validate());

        position.setOnClickListener(v -> getLocation());

    }


    // Function that validates user entries

    private void validate(){
        String usr_address, usr_position;
        usr_address = p_address.getText().toString();
        usr_position = position.getText().toString();
        if (TextUtils.isEmpty(usr_address)) {
            Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(usr_position)) {
            Toast.makeText(this, "Please Select Your Location!", Toast.LENGTH_SHORT).show();
        }else {
            updateProfile(getDetails());
            dialog = new Dialog(ProfileActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    // Function that gets the current location of the device
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        // Do something with the latitude and longitude
                        Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                        position.setText(latitude+" "+longitude);
                    } else {
                        Log.d("Location", "Location is null");
                    }
                }).addOnFailureListener(this, e -> Log.e("Location", "Error getting location", e));
    }


    // Function that posts user profile details

    private ProfileRequest getDetails(){
        ProfileRequest profileRequest = new ProfileRequest();
        if(Objects.requireNonNull(p_address.getText()).toString().equals("")) {
            Toast.makeText(this, "Please Enter User Address", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(position.getText()).toString().equals("")){
            Toast.makeText(this, "Please Enter Firstname", Toast.LENGTH_SHORT).show();
        }else{
            profileRequest.setAddress(p_address.getText().toString());
            profileRequest.setAuth(Integer.parseInt(auth_id));
            profileRequest.setLatitude(latitude);
            profileRequest.setLongitude(longitude);
        }
        return profileRequest;
    }


    // Function that updates profile details

    private void updateProfile(ProfileRequest profileRequest)
    {
        Call<ProfileResponse> call = ApiService.getWasteApiService().updateProfile(profileRequest);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                String success; int auth;
                if(response.isSuccessful()){
                    ProfileResponse profileResponse = response.body();
                    auth = profileResponse.getAuth();
                    success = profileResponse.getSuccess();
                    if(success.equals("true")){
                        Log.d(TAG, "Auth ID: "+auth);
                        Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        Toast.makeText(ProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Failed to Reach the Server", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }




}