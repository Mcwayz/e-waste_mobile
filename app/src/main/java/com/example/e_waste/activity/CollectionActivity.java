package com.example.e_waste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.R;
import com.example.e_waste.model.authentication.Auth;
import com.example.e_waste.model.collections.CollectionRequest;
import com.example.e_waste.model.collections.CollectionResponse;
import com.example.e_waste.model.profile.DetailsResponse;
import com.example.e_waste.model.profile.ProfileResponse;
import com.example.e_waste.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionActivity extends AppCompatActivity {

    private Button request;
    private Dialog dialog;
    private ImageView imgBack;
    private TextInputEditText sub_id;
    private int auth_id_user, user_id;
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
            auth_id_user = Integer.parseInt(IdWithoutQuotes);
        } catch (JWTDecodeException exception){
            Log.e("TAG", "Invalid JWT token: " + exception.getMessage());
        }

        request.setOnClickListener(v -> validate());

        imgBack.setOnClickListener(v -> {
            Intent i = new Intent(CollectionActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        request_date.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CollectionActivity.this,
                    (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        // Handle selected date
                        String selectedDate = String.format("%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        request_date.setText(selectedDate);
                    },
                    year, month, dayOfMonth);
            datePickerDialog.show();
        });

        request_time.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    CollectionActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        // Handle selected time
                        String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        request_time.setText(selectedTime);
                    },
                    hour, minute, true);
            timePickerDialog.show();
        });

        getProfile();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(CollectionActivity.this);
                        builder.setTitle("Collection Request");
                        builder.setMessage("Collection Request  Has Been Sent :-)..!");
                        builder.setPositiveButton("Okay", (dialog, which) -> {

                            dialog.dismiss();
                        });
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
            collectionRequest.setUser_id(Integer.parseInt(sub_id.getText().toString()));
            collectionRequest.setIs_collected(false);
            /*collectionRequest.setRequest_date(r_date+" "+r_time);
            collectionRequest.setCollection_date(r_date+" "+r_time);
            collectionRequest.setRequest_date("");
            collectionRequest.setCollection_date("");*/
        }
        return collectionRequest;
    }


    private void goBack(){
        Intent i = new Intent(CollectionActivity.this, MainActivity.class);
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
                        int user_id = detailsResponse.getUser_id();
                        String firstname = detailsResponse.getFirstname();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CollectionActivity.this);
                        builder.setTitle("Sensor Error");
                        builder.setMessage("Hi.. " +firstname+ " \n" +
                                "The Bin Sensor Cannot Be Detected\n" +
                                "Kindly Select Request Date & Time\n" +
                                "For Manual Collection Request,\n" +
                                "Apologies For The Inconvenience Caused.");

                        builder.setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        builder.show();
                        sub_id.setText(String.valueOf(user_id));
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(CollectionActivity.this);
                        builder.setTitle("No Profile Details");
                        builder.setMessage("Provide Profile Details in the Profile Section!");
                        builder.setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                            Intent intent = new Intent(CollectionActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Toast.makeText(CollectionActivity.this, "Error Returning User Profile" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                goBack();
            }
        });
    }

}