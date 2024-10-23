package com.project.farmers.ui.weigh;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.project.farmers.databinding.FragmentWeighBinding;
import com.project.farmers.entity.farmers.Farmer;
import com.project.farmers.entity.farmers.FarmerDao;
import com.project.farmers.entity.weight.Weighing;
import com.project.farmers.entity.weight.WeighingDao;
import com.project.farmers.entity.weight.WeighingDatabase;
import com.project.farmers.entity.weight.WeighingListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeighFragment extends Fragment {

    private FragmentWeighBinding binding;
    private WeighingDatabase weighingDatabase;
    private WeighingDao weighingDao;
    private FarmerDao farmerDao;
    private WeighingListAdapter adapter;
    private List<Weighing> weighingList = new ArrayList<>();
    private List<Farmer> farmerList = new ArrayList<>();  // Store farmers
    private String[] productList = {"Wheat", "Maize", "Barley", "Rice", "Beans", "Cotton"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWeighBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize database and DAOs
        weighingDatabase = WeighingDatabase.getInstance(requireContext());
        weighingDao = weighingDatabase.weighingDao();
        farmerDao = weighingDatabase.farmerDao();

        // Setup RecyclerView with Adapter
        adapter = new WeighingListAdapter(weighingList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

        // Fetch all available weighing data when the fragment opens
        fetchWeighingData();

        // Populate the Spinner with the product list
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, productList);
        binding.spinnerProduct.setAdapter(productAdapter);

        // Load farmers into the farmer spinner
        loadFarmersIntoSpinner();

        // Set up the "Weigh" button to simulate the weighing process
        binding.btnWeigh.setOnClickListener(v -> {
            ProgressDialog loadingDialog = new ProgressDialog(requireContext());
            loadingDialog.setMessage("Fetching weight ...");
            loadingDialog.show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                double randomWeight = 30 + (Math.random() * (100 - 30));
                binding.tvWeight.setText("Weight: " + String.format("%.2f", randomWeight) + " KG");
                loadingDialog.dismiss();

                saveWeighingData(randomWeight);  // Save after fetching weight
            }, 2000);
        });

        return root;
    }

    // Load farmers into the spinner
    private void loadFarmersIntoSpinner() {
        farmerList = farmerDao.getAllFarmers();  // Fetch all farmers
        List<String> farmerNames = new ArrayList<>();
        for (Farmer farmer : farmerList) {
            farmerNames.add(farmer.getName());
        }

        ArrayAdapter<String> farmerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, farmerNames);
        binding.spinnerFarmer.setAdapter(farmerAdapter);

        binding.spinnerFarmer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected farmer and retrieve the phone number
                Farmer selectedFarmer = farmerList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void saveWeighingData(double weight) {
        // Get selected farmer and product
        String farmerName = binding.spinnerFarmer.getSelectedItem().toString();
        String product = binding.spinnerProduct.getSelectedItem().toString();
        Farmer selectedFarmer = farmerList.get(binding.spinnerFarmer.getSelectedItemPosition());

        // Save weighing data with farmer name and phone
        Weighing weighing = new Weighing(farmerName, selectedFarmer.getPhone(), product, weight, new Date());
        weighingDao.insert(weighing);

        // Refresh data in RecyclerView
        fetchWeighingData();
        Toast.makeText(requireContext(), "Weighing data saved", Toast.LENGTH_SHORT).show();
    }

    private void fetchWeighingData() {
        weighingList = weighingDao.getAllWeighings();
        adapter.updateData(weighingList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
