package com.example.e_waste.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.R;
import com.example.e_waste.api.WasteInterface;
import com.example.e_waste.model.WasteType;
import com.example.e_waste.model.authentication.Auth;
import com.example.e_waste.model.subscriptions.SubscriptionRequest;
import com.example.e_waste.model.subscriptions.SubscriptionResponse;
import com.example.e_waste.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SubscribeActivity extends AppCompatActivity {
    private Dialog dialog;
    private int wasteTypeId;
    private TextInputEditText tfID;
    private ImageView  imgBack;
    private TextView option;
    private AutoCompleteTextView tfWasteType;

    private Button subscribe;
    private ArrayList<String> getWasteType = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        tfWasteType = findViewById(R.id.tf_waste_type);
        option = findViewById(R.id.tv_total_price);
        tfID = findViewById(R.id.tf_auth);
        imgBack = findViewById(R.id.img_back_mno);
        subscribe = findViewById(R.id.btn_subscribe);
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
            tfID.setText(IdWithoutQuotes);
        } catch (JWTDecodeException exception){
            Log.e("TAG", "Invalid JWT token: " + exception.getMessage());
        }

        getWasteType();

        imgBack.setOnClickListener(v -> {
            Intent i = new Intent(SubscribeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }



    private void validate(){

    }



    private void makeSubscription(SubscriptionRequest subscriptionRequest){
        Call<SubscriptionResponse> subscriptionRequestCall = ApiService.getWasteApiService().getSubscription(subscriptionRequest);
        subscriptionRequestCall.enqueue(new Callback<SubscriptionResponse>() {
            @Override
            public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {

                if(response.isSuccessful()){


                }
            }

            @Override
            public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Subscription Request Failed" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }





    private void getWasteType(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WasteInterface.base_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        WasteInterface api = retrofit.create(WasteInterface.class);
        Call<String> call = api.getWasteType();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
                {
                    if(response.body()!=null){
                        Log.i("Success", response.body());
                        String getResponse = response.body();
                        List<WasteType> getWasteTypeData = new ArrayList<>();
                        try {
                            JSONArray array = new JSONArray(getResponse);
                            getWasteTypeData.add(new WasteType(-1, "---Select---"));
                            for (int i = 0; i < array.length(); i++) {
                                WasteType waste = new WasteType();
                                JSONObject JsonObject = array.getJSONObject(i);
                                waste.setId(JsonObject.getInt("waste_id"));
                                waste.setWaste_type(JsonObject.getString("waste_type") + "    K" + JsonObject.getString("monthly_price"));
                                waste.setCost(JsonObject.getString("monthly_price"));
                                getWasteTypeData.add(waste);
                                Log.i(TAG, "onResponse: " + getWasteTypeData);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        for(int i=0; i<getWasteTypeData.size(); i++){
                            getWasteType.add(getWasteTypeData.get(i).getWaste_type());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SubscribeActivity.this, R.layout.spinner_item, getWasteType);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        tfWasteType.setAdapter(adapter);
                        tfWasteType.setOnItemClickListener((parent, view, position, id) -> {
                            String selectedItem = (String) parent.getItemAtPosition(position);
                            int index = getWasteType.indexOf(selectedItem);
                            Log.d("item selected", String.valueOf(index));
                            wasteTypeId = index;
                            option.setText(selectedItem);
                        });
                        Toast.makeText(SubscribeActivity.this, "Waste Types Loaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Request Failed" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}