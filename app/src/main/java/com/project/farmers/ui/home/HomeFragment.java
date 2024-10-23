package com.project.farmers.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.farmers.databinding.FragmentHomeBinding;
import com.project.farmers.entity.farmers.FarmerDao;
import com.project.farmers.entity.weight.WeighingDao;
import com.project.farmers.entity.weight.WeighingDatabase;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private WeighingDatabase weighingDatabase;
    private FarmerDao farmerDao;
    private WeighingDao weighingDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize database and DAOs
        weighingDatabase = WeighingDatabase.getInstance(requireContext());
        farmerDao = weighingDatabase.farmerDao();
        weighingDao = weighingDatabase.weighingDao();

        // Fetch and display counts
        fetchCounts();

        return root;
    }

    private void fetchCounts() {
        // Fetch the count of registered farmers
        int farmersCount = farmerDao.getFarmersCount(); // Assuming this method exists in your FarmerDao
        binding.tvFarmersCount.setText(String.valueOf(farmersCount));

        // Fetch the count of weighed products
        int productsCount = weighingDao.getWeighingsCount(); // Assuming this method exists in your WeighingDao
        binding.tvProductsCount.setText(String.valueOf(productsCount));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
