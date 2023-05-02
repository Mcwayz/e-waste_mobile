package com.example.e_waste.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.e_waste.R;
import com.example.e_waste.api.WasteInterface;
import com.example.e_waste.model.WasteType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SubscribeActivity extends AppCompatActivity {

    private Dialog dialog;
    private int wasteTypeId;
    private AutoCompleteTextView tfWasteType;
    private ArrayList<String> getWasteType = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        tfWasteType = findViewById(R.id.tf_waste_type);
        getWasteType();
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
                Log.i("Response", response.body());
                if(response.isSuccessful())
                {
                    if(response.body()!=null){
                        Log.i("Success", response.body());
                        try {
                            String getResponse = response.body();
                            List<WasteType> getWasteTypeData = new ArrayList<>();
                            JSONObject object = new JSONObject(getResponse);
                            JSONArray array  = object.getJSONArray("Waste Types");
                            getWasteTypeData.add(new WasteType(-1, "---Select---","0.00"));
                            for (int i = 0; i < array.length(); i++)
                            {
                                WasteType waste = new WasteType();
                                JSONObject JsonObject = array.getJSONObject(i);
                                waste.setId(JsonObject.getInt("waste_id"));
                                waste.setWaste_type(JsonObject.getString("waste_type")+" "+JsonObject.getString("monthly_price"));
                                waste.setCost(JsonObject.getString("monthly_price"));
                                getWasteTypeData.add(waste);
                            }
                            for(int i=0; i<getWasteTypeData.size(); i++){
                                getWasteType.add(getWasteTypeData.get(i).getWaste_type());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SubscribeActivity.this, R.layout.spinner_item, getWasteType);

                            adapter.setDropDownViewResource(R.layout.spinner_item);
                            tfWasteType.setAdapter(adapter);

                            tfWasteType.setOnItemClickListener((parent, view, position, id) -> {
                                String selectedItem = (String) parent.getItemAtPosition(position);
                                // Get the index number of the selected item
                                int index = getWasteType.indexOf(selectedItem);
                                // You can now use the "index" variable to get the item from the JSON array
                                Log.d("item selected", String.valueOf(index));
                                wasteTypeId = index;
                                // You can now use the index variable to access the selected item in the JSON array
                            });
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }
                        dialog.dismiss();
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