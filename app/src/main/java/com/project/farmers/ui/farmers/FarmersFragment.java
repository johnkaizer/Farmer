package com.project.farmers.ui.farmers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.farmers.R;
import com.project.farmers.databinding.FragmentFarmersBinding;
import com.project.farmers.entity.farmers.Farmer;
import com.project.farmers.entity.farmers.FarmerAdapter;
import com.project.farmers.entity.farmers.FarmerDao;
import com.project.farmers.entity.weight.WeighingDatabase;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.List;

public class FarmersFragment extends Fragment {

    private FragmentFarmersBinding binding;
    private WeighingDatabase weighingDatabase;
    private FarmerDao farmerDao;
    private FarmerAdapter farmerAdapter;
    private List<Farmer> allFarmersList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFarmersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Room database
        weighingDatabase = WeighingDatabase.getInstance(getContext());
        farmerDao = weighingDatabase.farmerDao();

        // Setup RecyclerView and Adapter
        farmerAdapter = new FarmerAdapter(new ArrayList<>(), getContext(), farmerDao);
        binding.rvFarmers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvFarmers.setAdapter(farmerAdapter);

        // Fetch all farmers from the database
        fetchFarmers();

        // Set up search functionality
        binding.etSearchFarmer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFarmers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        });

        // Set up the button click listener to show the dialog
        binding.btnAddFarmer.setOnClickListener(v -> showAddFarmerDialog());

        return root;
    }

    private void fetchFarmers() {
        new Thread(() -> {
            allFarmersList = farmerDao.getAllFarmers();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> farmerAdapter.setFarmerList(allFarmersList));
            }
        }).start();
    }

    private void filterFarmers(String query) {
        // Convert the query to lowercase for case-insensitive comparison
        String lowerCaseQuery = query.toLowerCase();

        // Filter the list based on the query (name or phone number)
        List<Farmer> filteredList = new ArrayList<>();
        for (Farmer farmer : allFarmersList) {
            if (farmer.getName().toLowerCase().contains(lowerCaseQuery) ||
                    farmer.getPhone().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(farmer);
            }
        }

        // Update the adapter with the filtered list
        farmerAdapter.setFarmerList(filteredList);
    }

    private void showAddFarmerDialog() {
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_farmer, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Get references to dialog input fields
        EditText etFarmerName = dialogView.findViewById(R.id.etFarmerName);
        EditText etFarmerPhone = dialogView.findViewById(R.id.etFarmerPhone);
        Button btnSaveFarmer = dialogView.findViewById(R.id.btnSaveFarmer);

        // Set the save button click listener
        btnSaveFarmer.setOnClickListener(v -> {
            String farmerName = etFarmerName.getText().toString().trim();
            String farmerPhone = etFarmerPhone.getText().toString().trim();

            // Simple validation
            if (farmerName.isEmpty() || farmerPhone.isEmpty()) {
                etFarmerName.setError("Please enter both name and phone");
                return;
            }

            // Save the new farmer in the database
            Farmer newFarmer = new Farmer(farmerName, farmerPhone);
            new Thread(() -> {
                farmerDao.insert(newFarmer);

                // Fetch updated farmer list and refresh RecyclerView
                fetchFarmers();

                // Close the dialog
                if (getActivity() != null) {
                    getActivity().runOnUiThread(dialog::dismiss);
                }
            }).start();
        });

        // Show the dialog
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
