package com.example.e_waste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_waste.MainActivity;
import com.example.e_waste.R;
import com.example.e_waste.model.Token;
import com.example.e_waste.model.TokenRequest;
import com.example.e_waste.model.TokenResponse;
import com.example.e_waste.service.ApiService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText user_email;
    private TextView signup;
    private EditText user_password;
    private Button login;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_email = findViewById(R.id.login_email);
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
        String email, password;
        email = user_email.getText().toString();
        password = user_password.getText().toString();
        if(email.isEmpty()) {
            Toast.makeText(this, "Please Enter User Email..", Toast.LENGTH_LONG).show();
        }
        else if(password.isEmpty()) {
            Toast.makeText(this, "Please Enter User Password..", Toast.LENGTH_LONG).show();
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
        Call<TokenResponse> tokenRequestCall = ApiService.getTicketApiService().getAuthToken(tokenRequest);
        tokenRequestCall.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.isSuccessful()){
                    String auth_token, refresh, email;
                    email = user_email.getText().toString();
                    TokenResponse tokenResponse = response.body();
                    List<Token> tokens = tokenResponse.getToken();
                    for (Token token : tokens ){
                        Log.d("Bearer Token","Value : "+token.getToken()+" Refresh Token Value : "+token.getRefresh());
                        auth_token = token.getToken();
                        refresh = token.getRefresh();
                        Log.d("Bearer Token","Value : "+token);
                        dialog.dismiss();
                        if(!auth_token.isEmpty()){
                            editor.putString("auth_token", auth_token);
                            editor.putString("refresh_token", refresh);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userEmail", email);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Wrong User Credentials, Try Again!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

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
        if(Objects.requireNonNull(user_email.getText()).toString().equals("")) {
            Toast.makeText(this, "Please Enter User Email", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(user_password.getText()).toString().equals("")){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }else{
            tokenRequest.setEmail(user_email.getText().toString());
            tokenRequest.setPassword(user_password.getText().toString());
        }
        return tokenRequest;
    }


}