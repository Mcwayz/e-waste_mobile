package com.example.e_waste.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.example.e_waste.model.collections.CollectionResponse;
import com.example.e_waste.model.profile.DetailsRequest;
import com.example.e_waste.model.profile.DetailsResponse;
import com.example.e_waste.model.profile.ProfileRequest;
import com.example.e_waste.model.subscriptions.SubscriptionRequest;
import com.example.e_waste.model.subscriptions.SubscriptionResponse;
import com.example.e_waste.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SubscribeActivity extends AppCompatActivity {
    private Dialog dialog;
    private int wasteTypeId;
    private String currentDate;
    private int auth_id_user, user_id;
    private TextInputEditText tfID;
    private ImageView  imgBack;
    private TextView option;
    private AutoCompleteTextView tfWasteType;

    private Button subscribe;
    private ArrayList<String> getWasteType = new ArrayList<>();

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        tfWasteType = findViewById(R.id.tf_waste_type);
        option = findViewById(R.id.tv_total_price);
        tfID = findViewById(R.id.tf_auth);
        imgBack = findViewById(R.id.img_back_mno);
        subscribe = findViewById(R.id.btn_subscribe);
        currentDate = getCurrentDate();
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
            auth_id_user = Integer.parseInt(IdWithoutQuotes);
        } catch (JWTDecodeException exception){
            Log.e("TAG", "Invalid JWT token: " + exception.getMessage());
        }

        getWasteType();

        imgBack.setOnClickListener(v -> {
            Intent i = new Intent(SubscribeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        subscribe.setOnClickListener(v -> validate());
        getProfile();
    }



    private void validate(){
        String waste_type;
        waste_type = Objects.requireNonNull(tfWasteType.getText()).toString();
        if (TextUtils.isEmpty(waste_type)) {
            Toast.makeText(this, "Please Select Waste Type", Toast.LENGTH_SHORT).show();
        }else {
            makeSubscription(getDetails());
            dialog = new Dialog(SubscribeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wait2);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }


    private SubscriptionRequest getDetails()
    {
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        String waste_type, sub_id;
        sub_id = Objects.requireNonNull(tfID.getText()).toString();
        waste_type = Objects.requireNonNull(tfWasteType.getText()).toString();
        if (TextUtils.isEmpty(waste_type)) {
            Toast.makeText(this, "Please Select Waste Type", Toast.LENGTH_SHORT).show();
        }else {
            subscriptionRequest.setUser(Integer.parseInt(sub_id));
            subscriptionRequest.setWaste(wasteTypeId);
        }
        return subscriptionRequest;
    }



    private void makeSubscription(SubscriptionRequest subscriptionRequest){
        Call<SubscriptionResponse> subscriptionRequestCall = ApiService.getWasteApiService().getSubscription(subscriptionRequest);
        subscriptionRequestCall.enqueue(new Callback<SubscriptionResponse>() {
            @Override
            public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {

                if(response.isSuccessful()){
                    String sub_date; int sub;
                    SubscriptionResponse subscription = response.body();
                    sub = subscription.getSub_id();
                    sub_date = subscription.getSub_date();
                    if(sub > 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SubscribeActivity.this);
                        builder.setTitle("Subscription Success");
                        builder.setMessage("Subscription Request  Has Been Sent\n" +
                                "Our Payment Gateway is Down, We will just Collect\n" +
                                "Cash Upon Your First Waste Collection :-(..!");
                        builder.setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                            goBack();
                        });
                    }else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SubscribeActivity.this);
                        builder.setTitle("Subscription Failed");
                        builder.setMessage("Failed to Reach the Server\n" +
                                "Try Again..!");
                        builder.setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                            goBack();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Subscription Request Failed" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                goBack();
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
    private void goBack(){
        Intent i = new Intent(SubscribeActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void getProfile(){
        Call<DetailsResponse> detailsResponseCall = ApiService.getWasteApiService().getProfile(auth_id_user);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(SubscribeActivity.this);
                        builder.setTitle("Profile Details");
                        builder.setMessage("First Name : " +firstname+ " \n" +
                                "Last Name : " +lastname+ " \n" +
                                "Set Email : " +email+ " \n" +
                                "Set Address : " +address
                        );
                        tfID.setText(String.valueOf(user_id));
                        builder.setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        builder.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SubscribeActivity.this);
                        builder.setTitle("No Profile Details");
                        builder.setMessage("Provide Profile Details in the Profile Section Before Subscribing!");
                        builder.setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                            Intent intent = new Intent(SubscribeActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Error Returning User Profile" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                goBack();
            }
        });
    }

}