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
    private ArrayList task_id, collection_id, address, assigned_date, user_collect_date, is_collected;

//


    public CustomAdapter(Context context, ArrayList task_id, ArrayList collection_id, ArrayList address, ArrayList assigned_date, ArrayList user_collect_date, ArrayList is_collected,RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.task_id = task_id;
        this.collection_id = collection_id;
        this.address = address;
        this.assigned_date = assigned_date;
        this.user_collect_date = user_collect_date;
        this.is_collected = is_collected;
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
        holder.tvTaskId.setText(String.valueOf(task_id.get(position)));
        holder.tvCollection_id.setText(String.valueOf(collection_id.get(position)));
        holder.tvAddress.setText(String.valueOf(address.get(position)));
        holder.tvIs_collected.setText(String.valueOf(is_collected.get(position)));
        holder.tvAssigned_date.setText(String.valueOf(assigned_date.get(position)));
        holder.tvDesired.setText(String.valueOf(user_collect_date.get(position)));

    }

    @Override
    public int getItemCount() {
        return task_id.size();
    }
    @Override
    public void onItemClick(int position) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskId, tvCollection_id, tvAssigned_date, tvAddress, tvDesired, tvIs_collected;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tvTaskId = itemView.findViewById(R.id.tv_task_id);
            tvCollection_id = itemView.findViewById(R.id.tv_collection_id);
            tvAssigned_date = itemView.findViewById(R.id.tv_request_date);
            tvDesired = itemView.findViewById(R.id.tv_desired_date);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvIs_collected = itemView.findViewById(R.id.tv_is_collected);
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
