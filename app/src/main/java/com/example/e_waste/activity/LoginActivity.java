package com.example.e_waste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_waste.R;
import com.example.e_waste.model.authentication.TokenRequest;
import com.example.e_waste.model.authentication.TokenResponse;
import com.example.e_waste.service.ApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText user_name;
    private TextView signup;
    private EditText user_password;
    private Button login;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_name = findViewById(R.id.username);
        user_password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.signUpRedirectText);

        //Setting the onclick event on the login button
        login.setOnClickListener(v -> Validate());
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Function That Validates The User Input
    private void Validate(){
        String username, password;
        username = user_name.getText().toString();
        password = user_password.getText().toString();
        if(username.isEmpty()) {
            Toast.makeText(this, "Please Enter Username..", Toast.LENGTH_LONG).show();
        }
        else if(password.isEmpty()) {
            Toast.makeText(this, "Please Enter Password..", Toast.LENGTH_LONG).show();
        }else{
            //Calling the authentication function

            AuthToken(getAuthToken());
            dialog = new Dialog(LoginActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    //The Authentication Function
    private void AuthToken(TokenRequest tokenRequest){
        SharedPreferences sharedPreferences = getSharedPreferences("my_app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Call<TokenResponse> tokenRequestCall = ApiService.getWasteApiService().getAuthToken(tokenRequest);
        tokenRequestCall.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.isSuccessful()){
                    String auth_token, refresh, username;
                    username = user_name.getText().toString();
                    TokenResponse tokenResponse = response.body();
                    auth_token = tokenResponse.getAccess();
                    refresh = tokenResponse.getRefresh();
                    Log.d("Bearer Token","Value : "+auth_token);
                    dialog.dismiss();
                    if(!auth_token.isEmpty()){
                        editor.putString("auth_token", auth_token);
                        editor.putString("refresh_token", refresh);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "Wrong User Credentials, Try Again!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "No Internet Connection, Try Again!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Failed, Try Again!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    //Function that returns the authentication token

    private TokenRequest getAuthToken(){
        TokenRequest tokenRequest = new TokenRequest();
        if(Objects.requireNonNull(user_name.getText()).toString().equals("")) {
            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(user_password.getText()).toString().equals("")){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }else{
            tokenRequest.setUsername(user_name.getText().toString());
            tokenRequest.setPassword(user_password.getText().toString());
        }
        return tokenRequest;
    }


}