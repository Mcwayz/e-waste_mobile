package com.example.e_waste.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.R;
import com.example.e_waste.adapter.CustomAdapter;
import com.example.e_waste.adapter.WasteAdapter;
import com.example.e_waste.api.RecyclerViewInterface;
import com.example.e_waste.api.WasteInterface;
import com.example.e_waste.model.authentication.Auth;
import com.example.e_waste.model.subscriptions.SubsResponse;
import com.example.e_waste.model.subscriptions.Subscription;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HistoryActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private ArrayList<String> sub_id, firstname, lastname, waste_type, monthly_price, address, sub_date, auth_id;
    private ImageView imgBack;
    private CustomAdapter customAdapter;
    private Button btn_search;
    private EditText filterDate;
    private String filter_date;
    Dialog dialog;
    private String auth_user_id;

    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.recyclerView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        imgBack = findViewById(R.id.img_back_his);
        sub_id = new ArrayList<>();
        firstname = new ArrayList<>();
        lastname = new ArrayList<>();
        waste_type = new ArrayList<>();
        monthly_price = new ArrayList<>();
        address = new ArrayList<>();
        sub_date = new ArrayList<>();
        auth_id = new ArrayList<>();
        btn_search = findViewById(R.id.btn_filter);
        filterDate = findViewById(R.id.searchView);

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

        imgBack.setOnClickListener((View view) -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        filterDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(HistoryActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        int m_month = monthOfYear + 1;
                        int day = Integer.parseInt(String.valueOf(dayOfMonth));
                        int month = Integer.parseInt(String.valueOf(m_month));
                        int m_year = Integer.parseInt(String.valueOf(year));
                        filter_date = String.format("%02d/%02d/%04d", day, month, m_year);
                        filterDate.setText(filter_date);
                        Toast.makeText(HistoryActivity.this, filter_date, Toast.LENGTH_SHORT).show();
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        getSubs();
    }

    private void getSubs() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WasteInterface.base_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        WasteInterface api = retrofit.create(WasteInterface.class);
        Call<SubsResponse> call = api.getSubscriptions(Integer.parseInt(auth_user_id));
        call.enqueue(new Callback<SubsResponse>() {
            @Override
            public void onResponse(Call<SubsResponse> call, Response<SubsResponse> response) {
                if (response.isSuccessful()) {
                    SubsResponse subsResponse = response.body();
                    if (response.body() != null) {
                        List<Subscription> subscriptionList = subsResponse.getSubscription();
                        for (Subscription subscription : subscriptionList) {
                            Log.d("TicketResponse", "ID: " + subscription.getSub_id() + ", Firstname: " + subscription.getFirstname() + ", Lastname: " + subscription.getLastname() + ",  Waste Type: " + subscription.getWaste_type() + ", Price: " + subscription.getMonthly_price());
                            sub_id.add(String.valueOf(subscription.getSub_id()));
                            firstname.add(subscription.getFirstname());
                            lastname.add(subscription.getLastname());
                            waste_type.add(subscription.getWaste_type());
                            address.add(subscription.getAddress());
                            monthly_price.add(subscription.getMonthly_price());
                            sub_date.add(subscription.getSub_date());
                            auth_id.add(String.valueOf(subscription.getAuth_id()));
                        }
                        customAdapter = new CustomAdapter(HistoryActivity.this, sub_id, firstname,
                                lastname, waste_type, address, monthly_price, sub_date, auth_id, HistoryActivity.this);
                        recyclerView.setAdapter(customAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                        Toast.makeText(HistoryActivity.this, "Your Subscription History", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } else {
                    // Handle the error response
                }
            }

            @Override
            public void onFailure(Call<SubsResponse> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "No History Found", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setTitle("Subscription Details");
        builder.setMessage("" +
                "Sub ID : " +sub_id.get(position)+ " \n" +
                "First Name : " +firstname.get(position)+ " \n" +
                "Last Name : " +lastname.get(position)+ " \n" +
                "Sub Date : " +sub_date.get(position)+ " \n" +
                "Sub Address : " +address.get(position)
        );
        builder.setPositiveButton("Okay", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }
}