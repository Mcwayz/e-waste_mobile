package com.example.e_waste.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_waste.R;
import com.example.e_waste.model.subscriptions.SubsResponse;
import com.example.e_waste.model.subscriptions.Subscription;

import java.util.List;

public class WasteAdapter extends RecyclerView.Adapter<WasteAdapter.ViewHolder> {

    private List<Subscription> subscriptionList;

    public WasteAdapter(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscription subscription = subscriptionList.get(position);
        holder.tvSubId.setText(String.valueOf(subscription.getSub_id()));
        holder.tvFirstName.setText(subscription.getFirstname());
        holder.tvLastName.setText(subscription.getLastname());
        holder.tvWasteType.setText(subscription.getWaste_type());
        holder.tvPrice.setText(subscription.getSub_date());
        holder.tvDate.setText(subscription.getSub_date());
    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubId, tvWasteType, tvFirstName, tvLastName, tvDate, tvPrice;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubId = itemView.findViewById(R.id.tv_sub_id);
            tvWasteType = itemView.findViewById(R.id.tv_waste_type);
            tvFirstName = itemView.findViewById(R.id.firstname);
            tvLastName = itemView.findViewById(R.id.lastname);
            tvPrice = itemView.findViewById(R.id.tv_price_his);
            tvDate = itemView.findViewById(R.id.tv_date_his);
        }
    }
}
