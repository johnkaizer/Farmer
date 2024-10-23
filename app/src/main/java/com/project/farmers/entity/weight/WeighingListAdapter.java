package com.project.farmers.entity.weight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.farmers.R;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class WeighingListAdapter extends RecyclerView.Adapter<WeighingListAdapter.WeighingViewHolder> {

    private List<Weighing> weighingList;

    // Constructor to initialize the list
    public WeighingListAdapter(List<Weighing> weighingList) {
        this.weighingList = weighingList;
    }

    @NonNull
    @Override
    public WeighingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each row in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weighing, parent, false);
        return new WeighingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeighingViewHolder holder, int position) {
        // Get the data for the current position
        Weighing weighing = weighingList.get(position);
        // Bind the data to the views
        holder.bind(weighing);
    }

    @Override
    public int getItemCount() {
        // Return the size of the list
        return weighingList.size();
    }

    // Method to update the data in the adapter
    public void updateData(List<Weighing> newList) {
        this.weighingList = newList;
        notifyDataSetChanged(); // Notify the adapter to refresh the list
    }

    // ViewHolder class that holds the layout for each item in the RecyclerView
    static class WeighingViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFarmerName, tvFarmerPhone, tvProduct, tvWeight, tvDate;

        public WeighingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views for farmer name, farmer phone, product, weight, and date
            tvFarmerName = itemView.findViewById(R.id.tvFarmerName);
            tvFarmerPhone = itemView.findViewById(R.id.tvFarmerPhone); // Initialize the phone TextView
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        // Bind method to display data in the corresponding views
        public void bind(Weighing weighing) {
            tvFarmerName.setText(weighing.getFarmerName());
            tvFarmerPhone.setText(weighing.getFarmerPhone()); // Set farmer phone number
            tvProduct.setText(weighing.getProduct());
            tvWeight.setText(String.format("%.2f KG", weighing.getWeight()));

            // Format and set the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String formattedDate = dateFormat.format(weighing.getDate());
            tvDate.setText(formattedDate);
        }
    }
}
