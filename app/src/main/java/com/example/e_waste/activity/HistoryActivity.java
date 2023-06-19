package com.example.e_waste.activity;


import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.e_waste.model.collections.Collection;
import com.example.e_waste.model.profile.DetailsResponse;
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
import java.util.TimeZone;

public class HistoryActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private ArrayList<String> address, request_date, desired_date, assigned_date, is_collected, longitude, latitude;
    private ArrayList<Integer> task_id, collection_id;
    private ImageView imgBack;
    private CustomAdapter customAdapter;
    private String user_id;
    private String auth_user_id;
    private Auth auth;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.recyclerView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        imgBack = findViewById(R.id.img_back_his);
        task_id = new ArrayList<>();
        collection_id = new ArrayList<>();
        address = new ArrayList<>();
        request_date = new ArrayList<>();
        desired_date = new ArrayList<>();
        assigned_date = new ArrayList<>();
        is_collected = new ArrayList<>();
        longitude = new ArrayList<>();
        latitude = new ArrayList<>();

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

        getProfile();
        getRequests();
    }


    private void getRequests() {
        auth = new Auth(getApplicationContext());
        auth_user_id = auth.getProfileID();
        try {
            int prof_id = Integer.parseInt(auth_user_id);
            Toast.makeText(HistoryActivity.this, "Profile ID: " + prof_id, Toast.LENGTH_SHORT).show();
            Call<List<Collection>> call = ApiService.getWasteApiService().getCollections(prof_id);
            call.enqueue(new Callback<List<Collection>>() {
                @Override
                public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                    if(response.isSuccessful()){
                        List<Collection> collectionList = response.body();
                        // Create a SimpleDateFormat instance for parsing the original date format
                        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US);
                        // Create a SimpleDateFormat instance for formatting the desired date format
                        SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                        for (Collection collection : collectionList) {

                            String requestDate = formatDateString(collection.getRequest_date());
                            String desiredDate = formatDateString(collection.getRequest_date());
                            is_collected.add("Collected: "+collection.isIs_collected());
                            task_id.add(collection.getTask_id());
                            address.add(collection.getAddress());
                            latitude.add(collection.getLatitude());
                            longitude.add(collection.getLongitude());
                            collection_id.add(collection.getCollection_id());
                            request_date.add(requestDate);
                            desired_date.add(desiredDate);
                            assigned_date.add(formatDate(collection.getAssigned_date(), originalFormat, desiredFormat));


                            Log.d("Response", "ID: " + collection.getTask_id() + ", Address: " + collection.getAddress() + ", R Date: " + collection.getRequest_date() + ", C Date: " + collection.getUser_collect_date());
                        }

                        runOnUiThread(() -> {
                            recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                            customAdapter = new CustomAdapter(HistoryActivity.this, task_id, collection_id, address, assigned_date, desired_date, is_collected, HistoryActivity.this);
                            recyclerView.setAdapter(customAdapter);
                            Toast.makeText(HistoryActivity.this, "Your Pending Tasks!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<Collection>> call, Throwable t) {
                    Toast.makeText(HistoryActivity.this, "Error Fetching Data: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Collection Data", "Error Msg: " + t.getLocalizedMessage());
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(HistoryActivity.this, "Invalid collector ID", Toast.LENGTH_SHORT).show();
            Log.d("Collection Data", "Error Msg: " + e.getMessage());
        }
    }

    public static String formatDateString(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSXXX");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date date = inputFormat.parse(inputDate);
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if there's an error
    }


    private String formatDate(String date, SimpleDateFormat originalFormat, SimpleDateFormat desiredFormat) {
        try {
            Date originalDate = originalFormat.parse(date);
            return desiredFormat.format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void getProfile(){
        Call<DetailsResponse> detailsResponseCall = ApiService.getWasteApiService().getProfile(Integer.parseInt(auth_user_id));
        detailsResponseCall.enqueue(new Callback<DetailsResponse>() {
            @Override
            public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                if(response.isSuccessful()){
                    DetailsResponse detailsResponse = response.body();
                    if (detailsResponse != null) {
                        user_id = String.valueOf(detailsResponse.getUser_id());
                        String address = detailsResponse.getAddress();
                        String firstname = detailsResponse.getFirstname();
                        String lastname = detailsResponse.getLastname();
                        String longitude = detailsResponse.getLongitude();
                        String latitude = detailsResponse.getLatitude();

                        Toast.makeText(HistoryActivity.this, "Address : "+address+" Fullname :"+firstname+" "+lastname+" Location : "+longitude+" "+latitude, Toast.LENGTH_SHORT).show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                        builder.setTitle("No Profile Details");
                        builder.setMessage("Provide Details in the Profile Section!");
                        builder.setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                            Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsResponse> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Error Returning User Profile" +t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                goBack();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setTitle("Request Details");
        builder.setMessage(
                "Collection Address : "+address.get(position)+ "\n" +
                "Assigned Date : "+assigned_date.get(position)+ "\n" +
                "Collection By : "+desired_date.get(position)+ "\n" +
                "Collection  : "+is_collected.get(position)+ "\n" +
                "Longitude : "+longitude.get(position)+ "\n" +
                "Latitude : "+latitude.get(position)
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
