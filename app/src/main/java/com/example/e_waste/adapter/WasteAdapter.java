package com.example.e_waste.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_waste.R;
import com.example.e_waste.model.collections.Collection;

import java.util.List;

public class WasteAdapter extends RecyclerView.Adapter<WasteAdapter.ViewHolder> {

    private List<Collection> CollectionList;

    public WasteAdapter(List<Collection> collectionList) {
        this.CollectionList = collectionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Collection collection = CollectionList.get(position);
        holder.tvTask.setText(String.valueOf(collection.getTask_id()));
        holder.tvCollectionId.setText(String.valueOf(collection.getCollection_id()));
        holder.tvCollectorId.setText(collection.getCollector_id());
        holder.tvDateClosed.setText(collection.getDate_closed());
        holder.tvAssignedDate.setText(collection.getAssigned_date());
        holder.tvAddress.setText(collection.getAddress());
        holder.tvIs_collected.setText(collection.getCollector_id());
    }

    @Override
    public int getItemCount() {
        return CollectionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTask, tvCollectionId, tvCollectorId, tvDateClosed, tvAssignedDate, tvAddress, tvIs_collected;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTask = itemView.findViewById(R.id.tv_task_id);
            tvCollectionId = itemView.findViewById(R.id.tv_collection_id);
            tvCollectorId = itemView.findViewById(R.id.tv_collector_id);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDateClosed = itemView.findViewById(R.id.tv_collected_date);
            tvIs_collected = itemView.findViewById(R.id.tv_is_collected);
        }
    }
}
