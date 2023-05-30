package com.example.e_waste.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.R;
import com.example.e_waste.model.authentication.Auth;
import com.example.e_waste.model.profile.DetailsResponse;
import com.example.e_waste.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CardView cvCollection, cvHistory, cvSubscribe, cvProfile;
    private String UserName;
    private int user_id;
    private String auth_user_id;
    private ImageView imgProfile;
    private TextView cDate, Username;
    Dialog dialog;

    Auth auth;


    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d'" + getDayOfMonthSuffix(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + "'", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private static String getDayOfMonthSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String currentDate = getCurrentDate();
        cDate = findViewById(R.id.tv_dash_date);
        Username = findViewById(R.id.tv_dash_profile_name);
        cvCollection = findViewById(R.id.cv_collection);
        cvSubscribe = findViewById(R.id.cv_sub);
        cvHistory = findViewById(R.id.cv_history);
        cvProfile = findViewById(R.id.cv_profile);
        UserName = getIntent().getStringExtra("username");
        Username.setText("Hi "+UserName);
        cDate.setText(currentDate);
        imgProfile = findViewById(R.id.img_profile);

        cvProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        cvHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        cvCollection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
            startActivity(intent);
        });

        cvSubscribe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SubscribeActivity.class);
            startActivity(intent);
        });

        auth = new Auth(getApplicationContext());

        String token = auth.getToken();
        auth.startRunnable();

        if (token.isEmpty()) {
            Log.e("TAG", "No auth token found in shared preferences");
            return;
        }
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> claims = jwt.getClaims();
            String auth_id = String.valueOf(claims.get("user_id"));
            Log.d("TAG", "Auth ID: " + auth_id);
            String IdWithoutQuotes = auth_id.replace("\"", "");
            auth_user_id = IdWithoutQuotes;
        } catch (JWTDecodeException exception) {
            Log.e("TAG", "Invalid JWT token: " + exception.getMessage());
        }

        getProfile();

        imgProfile.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, imgProfile);
            popupMenu.getMenuInflater().inflate(R.menu.menu_options, popupMenu.getMenu());
            // Set click listener for menu items
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menu_logout:
                        // Handle logout action here
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.show();
        });


    }

    private void getProfile(){
        Call<DetailsResponse> detailsResponseCall = ApiService.getWasteApiService().getProfile(Integer.parseInt(auth_user_id));
        detailsResponseCall.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                if(response.isSuccessful()){
                    DetailsResponse detailsResponse = response.body();
                    if (detailsResponse != null) {
                        user_id = detailsResponse.getUser_id();
                        SharedPreferences sharedPreferences = getSharedPreferences("my_app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("profile_id", String.valueOf(user_id));
                        editor.apply();
                    }else{
                        Toast.makeText(MainActivity.this, "Error Returning Profile Info", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error Returning User Profile" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


}