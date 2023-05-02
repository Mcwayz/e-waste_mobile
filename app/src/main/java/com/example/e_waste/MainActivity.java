package com.example.e_waste;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.e_waste.activity.CollectionActivity;
import com.example.e_waste.activity.LoginActivity;
import com.example.e_waste.activity.ProfileActivity;
import com.example.e_waste.activity.SubscribeActivity;
import com.example.e_waste.activity.HistoryActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private CardView cvCollection, cvHistory, cvSubscribe, cvProfile;
    private String UserName;

    private ImageView imgProfile;
    private TextView cDate, Username;


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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String currentDate = getCurrentDate();
        cDate = findViewById(R.id.tv_dash_date);
        Username = findViewById(R.id.tv_dash_profile_name);
        cvCollection = findViewById(R.id.cv_collection);
        cvSubscribe = findViewById(R.id.cv_sub);
        cvHistory = findViewById(R.id.cv_history);
        cvProfile = findViewById(R.id.cv_profile);
        UserName = getIntent().getStringExtra("username");
        Username.setText("Hi "+UserName);
        cDate.setText(currentDate);
        imgProfile = findViewById(R.id.img_profile);

        cvProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        cvHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        cvCollection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
            startActivity(intent);
        });

        cvSubscribe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SubscribeActivity.class);
            startActivity(intent);
        });

        imgProfile.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, imgProfile);
            popupMenu.getMenuInflater().inflate(R.menu.menu_options, popupMenu.getMenu());
            // Set click listener for menu items
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menu_logout:
                        // Handle logout action here
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.show();
        });

    }


}