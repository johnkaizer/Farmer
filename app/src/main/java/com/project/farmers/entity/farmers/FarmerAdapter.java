package com.project.farmers.entity.farmers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.farmers.R;

import java.util.List;

public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.FarmerViewHolder> {

    private List<Farmer> farmerList;
    private Context context;
    private FarmerDao farmerDao;  // Add DAO for database operations

    public FarmerAdapter(List<Farmer> farmerList, Context context, FarmerDao farmerDao) {
        this.farmerList = farmerList;
        this.context = context;
        this.farmerDao = farmerDao;
    }

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.farmer_item, parent, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, int position) {
        Farmer farmer = farmerList.get(position);
        holder.tvFarmerName.setText(farmer.getName());
        holder.tvFarmerPhone.setText(farmer.getPhone());

        // Edit button click listener
        holder.btnEditFarmer.setOnClickListener(v -> showEditFarmerDialog(farmer));

        // Delete button click listener
        holder.btnDeleteFarmer.setOnClickListener(v -> {
            // Confirm deletion
            new AlertDialog.Builder(context)
                    .setTitle("Delete Farmer")
                    .setMessage("Are you sure you want to delete this farmer?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new Thread(() -> {
                            farmerDao.delete(farmer);
                            farmerList.remove(position);
                            ((Activity) context).runOnUiThread(() -> notifyItemRemoved(position));
                        }).start();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return farmerList.size();
    }

    public static class FarmerViewHolder extends RecyclerView.ViewHolder {
        TextView tvFarmerName, tvFarmerPhone;
        ImageButton btnEditFarmer, btnDeleteFarmer;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFarmerName = itemView.findViewById(R.id.tvFarmerName);
            tvFarmerPhone = itemView.findViewById(R.id.tvFarmerPhone);
            btnEditFarmer = itemView.findViewById(R.id.btnEditFarmer);
            btnDeleteFarmer = itemView.findViewById(R.id.btnDeleteFarmer);
        }
    }

    public void setFarmerList(List<Farmer> farmers) {
        this.farmerList = farmers;
        notifyDataSetChanged();
    }

    // Show dialog for editing farmer details
    private void showEditFarmerDialog(Farmer farmer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_farmer, null);
        builder.setView(dialogView);

        EditText etFarmerName = dialogView.findViewById(R.id.etFarmerName);
        EditText etFarmerPhone = dialogView.findViewById(R.id.etFarmerPhone);
        Button btnSaveFarmer = dialogView.findViewById(R.id.btnSaveFarmer);

        // Pre-fill with existing data
        etFarmerName.setText(farmer.getName());
        etFarmerPhone.setText(farmer.getPhone());

        AlertDialog dialog = builder.create();

        // Set save button listener
        btnSaveFarmer.setOnClickListener(v -> {
            String newName = etFarmerName.getText().toString().trim();
            String newPhone = etFarmerPhone.getText().toString().trim();

            if (newName.isEmpty() || newPhone.isEmpty()) {
                etFarmerName.setError("Please enter both name and phone");
                return;
            }

            // Update farmer in the database
            farmer.setName(newName);
            farmer.setPhone(newPhone);

            new Thread(() -> {
                farmerDao.update(farmer);
                ((Activity) context).runOnUiThread(() -> {
                    notifyDataSetChanged();
                    dialog.dismiss();
                });
            }).start();
        });

        dialog.show();
    }
}
