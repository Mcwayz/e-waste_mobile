package com.example.e_waste.model.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.activity.LoginActivity;

import java.util.Date;

import io.paperdb.Paper;


public class Auth extends Activity {
    private SharedPreferences sharedpreferences;
    private Context context;
    public Handler handler = new Handler();


    public Auth(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences("my_app", MODE_PRIVATE);
    }

    public String getToken() {
        return sharedpreferences.getString("auth_token", "");
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String token = getToken();
            checkTokenValidity(token);
            logoutUser();
            handler.postDelayed(this, 1800000); // Run this Runnable again after 5 seconds
        }
    };

    // Call this method to start the Runnable
    public void startRunnable() {
        handler.postDelayed(runnable, 1800000); // Run the Runnable for the first time after 5 seconds
    }

    // Call this method to stop the Runnable
    public void stopRunnable() {
        handler.removeCallbacks(runnable);
    }

    public void checkTokenValidity(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expiresAt = jwt.getExpiresAt();
            Date currentDate = new Date();
            if (expiresAt != null && currentDate.after(expiresAt)) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear().apply();
                logoutUser();
            } else {
                stopRunnable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logoutUser() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Paper.book().destroy();
        stopRunnable();
    }
}