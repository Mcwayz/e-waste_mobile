package com.example.e_waste.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.e_waste.R;
import com.example.e_waste.adapter.CustomAdapter;
import com.example.e_waste.api.RecyclerViewInterface;
import com.example.e_waste.model.authentication.Auth;
import com.example.e_waste.model.subscriptions.Subscription;
import com.example.e_waste.service.ApiService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private ArrayList<String> sub_id, firstname, lastname, waste_type, monthly_price, address, sub_date, auth_id;
    private ImageView imgBack;
    private CustomAdapter customAdapter;
    private String user_id;
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

        auth = new Auth(getApplicationContext());
        String token = auth.getToken();
        user_id = auth.getProfileID();
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

        imgBack.setOnClickListener(view -> {
           goBack();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        customAdapter = new CustomAdapter(HistoryActivity.this, sub_id, firstname, lastname, waste_type, monthly_price, sub_date, HistoryActivity.this);
        recyclerView.setAdapter(customAdapter);

        getSubs();
    }

 /*   private void getSubs() {
        int prof_id = Integer.parseInt(user_id);
        Call<List<Subscription>> call = ApiService.getWasteApiService().getSubscriptions(prof_id);
        call.enqueue(new Callback<List<Subscription>>() {
            @Override
            public void onResponse(Call<List<Subscription>> call, Response<List<Subscription>> response) {
                if (response.isSuccessful()) {
                    List<Subscription> subscriptionList = response.body();

                    for (Subscription subscription : subscriptionList) {
                        address.add(subscription.getAddress());
                        sub_date.add(subscription.getSub_date());
                        lastname.add(subscription.getLastname());
                        firstname.add(subscription.getFirstname());
                        waste_type.add(subscription.getWaste_type());
                        monthly_price.add(subscription.getMonthly_price());
                        sub_id.add(String.valueOf(subscription.getSub_id()));

                        Log.d("Sub Response", "Sub ID: " + subscription.getSub_id() + ", Name: " + subscription.getFirstname() + ", Waste Type: " + subscription.getWaste_type() + ", Monthly Price: " + subscription.getMonthly_price());
                    }

                    customAdapter = new CustomAdapter(HistoryActivity.this, sub_id, firstname,
                            lastname, waste_type, monthly_price, sub_date, HistoryActivity.this);
                    recyclerView.setAdapter(customAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    Toast.makeText(HistoryActivity.this, "Your Subscription History", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subscription>> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Error Fetching Data: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Sub Response", "Sub Msg: " + t.getLocalizedMessage());
            }
        });
    }*/

    private void getSubs() {
        int prof_id = Integer.parseInt(user_id);
        Call<List<Subscription>> call = ApiService.getWasteApiService().getSubscriptions(prof_id);
        call.enqueue(new Callback<List<Subscription>>() {
            @Override
            public void onResponse(Call<List<Subscription>> call, Response<List<Subscription>> response) {
                if (response.isSuccessful()) {
                    List<Subscription> subscriptionList = response.body();

                    // Create a SimpleDateFormat instance for parsing the original date format
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US);
                    // Create a SimpleDateFormat instance for formatting the desired date format
                    SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                    for (Subscription subscription : subscriptionList) {
                        address.add(subscription.getAddress());
                        sub_date.add(formatDate(subscription.getSub_date(), originalFormat, desiredFormat));
                        lastname.add(subscription.getLastname());
                        firstname.add(subscription.getFirstname());
                        waste_type.add(subscription.getWaste_type());
                        monthly_price.add(subscription.getMonthly_price());
                        sub_id.add(String.valueOf(subscription.getSub_id()));

                        Log.d("Sub Response", "Sub ID: " + subscription.getSub_id() + ", Name: " + subscription.getFirstname() + ", Waste Type: " + subscription.getWaste_type() + ", Monthly Price: " + subscription.getMonthly_price());
                    }

                    customAdapter = new CustomAdapter(HistoryActivity.this, sub_id, firstname,
                            lastname, waste_type, monthly_price, sub_date, HistoryActivity.this);
                    recyclerView.setAdapter(customAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    Toast.makeText(HistoryActivity.this, "Your Subscription History", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Subscription>> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Error Fetching Data: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Sub Response", "Sub Msg: " + t.getLocalizedMessage());
            }
        });
    }

    private String formatDate(String date, SimpleDateFormat originalFormat, SimpleDateFormat desiredFormat) {
        try {
            // Parse the original date string to a Date object using the original format
            Date originalDate = originalFormat.parse(date);
            // Format the Date object to the desired format
            return desiredFormat.format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return date; // Return the original date string if parsing fails
        }
    }

    @Override
    public void onItemClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setTitle("Subscription Details");
        builder.setMessage(""
                + "Sub ID : " + sub_id.get(position) + " \n"
                + "First Name : " + firstname.get(position) + " \n"
                + "Last Name : " + lastname.get(position) + " \n"
                + "Sub Date : " + sub_date.get(position) + " \n"
                + "Sub Address : " + address.get(position)
        );
        builder.setPositiveButton("Okay", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void goBack() {
        Intent i = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
