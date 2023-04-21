package com.example.e_waste.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import com.example.e_waste.activity.LoginActivity;


public class Auth extends Activity{
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
            if(token.isEmpty()){
                Intent logout = new Intent(context, LoginActivity.class);
                startActivity(logout);
                finish();
            }else{
                stopRunnable();
            }
            handler.postDelayed(this, 5000); // Run this Runnable again after 5 seconds
        }
    };

    // Call this method to start the Runnable
    public void startRunnable() {
        handler.postDelayed(runnable, 5000); // Run the Runnable for the first time after 5 seconds
    }

    // Call this method to stop the Runnable
    public void stopRunnable() {
        handler.removeCallbacks(runnable);
    }
}
