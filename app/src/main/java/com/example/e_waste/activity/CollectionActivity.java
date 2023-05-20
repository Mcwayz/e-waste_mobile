package com.example.e_waste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.R;
import com.example.e_waste.model.authentication.Auth;
import com.example.e_waste.model.collections.CollectionRequest;
import com.example.e_waste.model.collections.CollectionResponse;
import com.example.e_waste.model.profile.ProfileResponse;
import com.example.e_waste.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionActivity extends AppCompatActivity {
    private String auth_user_id;
    private Button request;
    private Dialog dialog;
    private TextInputEditText sub_id;
    private ImageView imgBack;
    private TextInputEditText request_date;
    private TextInputEditText request_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        request = findViewById(R.id.btn_request);
        sub_id = findViewById(R.id.tf_id);
        request_date = findViewById(R.id.tf_request_date);
        request_time = findViewById(R.id.tf_request_time);
        imgBack = findViewById(R.id.img_back_mno);
        Auth auth = new Auth(getApplicationContext());
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
        } catch (JWTDecodeException exception){
            Log.e("TAG", "Invalid JWT token: " + exception.getMessage());
        }

        request.setOnClickListener(v -> validate());

        imgBack.setOnClickListener(v -> {
            Intent i = new Intent(CollectionActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void validate(){
        String r_time, r_date;
        r_time = Objects.requireNonNull(request_time.getText()).toString();
        r_date = Objects.requireNonNull(request_date.getText()).toString();

        if (TextUtils.isEmpty(r_date)) {
            Toast.makeText(this, "Please Enter Request Date Address", Toast.LENGTH_SHORT).show();
        }else  if (TextUtils.isEmpty(r_time)) {
            Toast.makeText(this, "Please Enter Request Time", Toast.LENGTH_SHORT).show();
        }
        else
        {
           getRequest(getDetails());
            dialog = new Dialog(CollectionActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    private void getRequest(CollectionRequest collectionRequest){
        Call<CollectionResponse> call = ApiService.getWasteApiService().getCollection(collectionRequest);
        call.enqueue(new Callback<CollectionResponse>() {
            @Override
            public void onResponse(Call<CollectionResponse> call, Response<CollectionResponse> response) {
                if(response.isSuccessful()){
                    String request_date; int collect;
                    CollectionResponse collectionResponse = response.body();
                    collect = collectionResponse.getCollection_id();
                    request_date = collectionResponse.getRequest_date();
                    if(collect > 0){
                        Toast.makeText(CollectionActivity.this, "Collection Request Sent!: "+request_date, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        Toast.makeText(CollectionActivity.this, "Failed to Reach the Server", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        goBack();
                    }
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse> call, Throwable t) {
                Toast.makeText(CollectionActivity.this, "Failed to Reach the Server", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                goBack();
            }
        });
    }


    private CollectionRequest getDetails()
    {
        String r_time, r_date;
        CollectionRequest collectionRequest = new CollectionRequest();
        r_time = Objects.requireNonNull(request_time.getText()).toString();
        r_date = Objects.requireNonNull(request_date.getText()).toString();
        if (TextUtils.isEmpty(r_date)) {
            Toast.makeText(this, "Please Enter Request Date Address", Toast.LENGTH_SHORT).show();
        }else  if (TextUtils.isEmpty(r_time)) {
            Toast.makeText(this, "Please Enter Request Time", Toast.LENGTH_SHORT).show();
        } else {
            collectionRequest.setUser_id(Integer.parseInt(auth_user_id));
            collectionRequest.setIs_collected(false);
            collectionRequest.setRequest_date(r_date+" "+r_time);
            collectionRequest.setCollection_date(r_date+" "+r_time);
        }
        return collectionRequest;
    }


    private void goBack(){
        Intent i = new Intent(CollectionActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}