package com.example.e_waste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_waste.R;
import com.example.e_waste.model.profile.UserRequest;
import com.example.e_waste.model.profile.UserResponse;
import com.example.e_waste.service.ApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText username;
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password1;
    private EditText password2;
    private Button btnRegister;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.login_email);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        btnRegister = findViewById(R.id.signup_button);
        btnRegister.setOnClickListener(v -> validate());
    }

    // Function that validates user input

    private void validate(){
        String usr_email, usr_name, Fname, Lname, pass1, pass2;
        usr_email = email.getText().toString();
        usr_name = username.getText().toString();
        Fname = firstname.getText().toString();
        Lname = lastname.getText().toString();
        pass1 = password1.getText().toString();
        pass2 = password2.getText().toString();
        if(TextUtils.isEmpty(usr_name)){
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(usr_email)){
            Toast.makeText(this, "Please Enter User Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Fname)) {
            Toast.makeText(this, "Please Enter Firstname", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Lname)) {
            Toast.makeText(this, "Please Enter Lastname", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pass1)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass2)) {
            Toast.makeText(this, "Please Confirm Your Password", Toast.LENGTH_SHORT).show();
        } else if (!pass1.equals(pass2)) {
            Toast.makeText(this, "Passwords Don't Match!", Toast.LENGTH_SHORT).show();
        }else {
            UserRegistration(getUserDetails());
            dialog = new Dialog(RegistrationActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    // Function that registers a user

    private void UserRegistration(UserRequest userRequest){
        Call<UserResponse> userRequestCall = ApiService.getWasteApiService().getUser(userRequest);
        userRequestCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    int user_id;
                    String status;
                    UserResponse userResponse = response.body();
                    user_id = userResponse.getUser_id();
                    status = userResponse.getStatus();
                        Toast.makeText(RegistrationActivity.this, "Registration:"+status+" "+user_id, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Registration Failed, Try Again!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    // Function that processes user inputs

    private UserRequest getUserDetails(){
        UserRequest userRequest = new UserRequest();
        if(Objects.requireNonNull(email.getText()).toString().equals("")) {
            Toast.makeText(this, "Please Enter User Email", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(firstname.getText()).toString().equals("")){
            Toast.makeText(this, "Please Enter Firstname", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(lastname.getText()).toString().equals("")){
            Toast.makeText(this, "Please Enter Lastname", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(username.getText()).toString().equals("")){
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(password1.getText()).toString().equals("")){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }else if(Objects.requireNonNull(password2.getText()).toString().equals("")){
            Toast.makeText(this, "Please Confirm Your Password", Toast.LENGTH_SHORT).show();
        }else{
            userRequest.setUsername(username.getText().toString());
            userRequest.setEmail(email.getText().toString());
            userRequest.setFirstname(firstname.getText().toString());
            userRequest.setLastname(lastname.getText().toString());
            userRequest.setPassword1(password1.getText().toString());
            userRequest.setPassword2(password2.getText().toString());
        }
        return userRequest;
    }
}