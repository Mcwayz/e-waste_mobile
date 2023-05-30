package com.example.e_waste.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_waste.R;
import com.example.e_waste.api.RecyclerViewInterface;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements RecyclerViewInterface {
    private RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList sub_id, firstname, lastname, waste_type,  price, date;

    public CustomAdapter(Context context, ArrayList sub_id, ArrayList firstname, ArrayList lastname,
        ArrayList waste_type, ArrayList price, ArrayList date,
        RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.sub_id = sub_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.waste_type = waste_type;
        this.price = price;
        this.date = date;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvSubId.setText(String.valueOf(sub_id.get(position)));
        holder.tvWasteType.setText(String.valueOf(waste_type.get(position)));
        holder.tvPrice.setText(String.valueOf(price.get(position)));
        holder.tvDate.setText(String.valueOf(date.get(position)));
        holder.tvFirstname.setText(String.valueOf(firstname.get(position)));
        holder.tvLastname.setText(String.valueOf(lastname.get(position)));

    }

    @Override
    public int getItemCount() {
        return sub_id.size();
    }
    @Override
    public void onItemClick(int position) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubId, tvWasteType, tvPrice, tvDate, tvFirstname, tvLastname;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tvSubId = itemView.findViewById(R.id.tv_txn_id);
            tvWasteType = itemView.findViewById(R.id.tv_waste_type);
            tvPrice = itemView.findViewById(R.id.tv_price_his);
            tvDate = itemView.findViewById(R.id.tv_date_his);
            tvFirstname = itemView.findViewById(R.id.tv_first_name);
            tvLastname = itemView.findViewById(R.id.tv_lastname);

            itemView.setOnClickListener(v -> {
                if(recyclerViewInterface != null){
                    int pos = getAdapterPosition();
                    if(pos !=RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
