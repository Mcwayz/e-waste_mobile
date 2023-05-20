package com.example.e_waste.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.R;
import com.example.e_waste.model.authentication.Auth;
import com.example.e_waste.model.profile.DetailsRequest;
import com.example.e_waste.model.profile.DetailsResponse;
import com.example.e_waste.model.profile.ProfileRequest;
import com.example.e_waste.model.profile.ProfileResponse;
import com.example.e_waste.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements LocationListener {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private Dialog dialog;
    private String auth_id;
    private int user_id;
    private Button update;
    private ImageView imgBack;
    private TextInputEditText p_address, position, auth_t;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        p_address = findViewById(R.id.tf_address);
        position = findViewById(R.id.tf_location);
        update = findViewById(R.id.btn_update);
        imgBack = findViewById(R.id.img_back_mno);
        auth_t = findViewById(R.id.tf_auth);
        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Auth auth = new Auth(getApplicationContext());
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
            String IdWithoutQuotes = auth_id.replace("\"", "");
            auth_t.setText(auth_id);
            auth_id = IdWithoutQuotes;
            getProfile();
        } catch (JWTDecodeException exception){
            Log.e("TAG", "Invalid JWT token: " + exception.getMessage());
        }

        update.setOnClickListener(v -> validate());
        position.setOnClickListener(v -> getLocation());
        imgBack.setOnClickListener(v -> {
            Intent i = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Request runtime permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
            return;
        }

        // Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        getProfile();
    }


    // Function that validates user entries

    private void validate(){
        String usr_address;
        usr_address = Objects.requireNonNull(p_address.getText()).toString();
        if (TextUtils.isEmpty(usr_address)) {
            Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show();
        }else {
            updateProfile(getDetails());
            dialog = new Dialog(ProfileActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }
    // Function that posts user profile details
    private ProfileRequest getDetails(){
        ProfileRequest profileRequest = new ProfileRequest();
        if(Objects.requireNonNull(p_address.getText()).toString().equals("")) {
            Toast.makeText(this, "Please Enter User Address", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(auth_t.getText()).toString().equals("")){
            Toast.makeText(this, "Please Enter User ID", Toast.LENGTH_SHORT).show();
        }else{
            profileRequest.setAddress(p_address.getText().toString());
            profileRequest.setAuth_id(Integer.parseInt(auth_id));
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
                        goBack();
                    }else{
                        Toast.makeText(ProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        goBack();
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


    private void getLocation(){

    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        position.setText(latitude + " " + longitude);
    }
    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }
    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }
    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void goBack(){
        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    private void getProfile(){
        Call<DetailsResponse> detailsResponseCall = ApiService.getWasteApiService().getProfile(Integer.parseInt(auth_id));
        detailsResponseCall.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                if(response.isSuccessful()){
                    DetailsResponse detailsResponse = response.body();
                    if (detailsResponse != null) {
                        String address = detailsResponse.getAddress();
                        String firstname = detailsResponse.getFirstname();
                        String lastname = detailsResponse.getLastname();
                        String email = detailsResponse.getEmail();
                        int user_id = detailsResponse.getUser_id();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setTitle("Profile Details");
                        builder.setMessage("First Name : " +firstname+ " \n" +
                                "Last Name : " +lastname+ " \n" +
                                "Set Email : " +email+ " \n" +
                                "Set Address : " +address
                        );
                        auth_t.setText(String.valueOf(user_id));
                        p_address.setText(address);
                        builder.setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error Returning User Profile" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                goBack();
            }
        });
    }


}