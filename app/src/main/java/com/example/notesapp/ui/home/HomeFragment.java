package com.example.notesapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.notesapp.GroupList;
import com.example.notesapp.MainPageActivity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.MainPageActivity;
import com.example.notesapp.R;
import com.example.notesapp.SettingsFragment;
import com.example.notesapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment  {

    private FragmentHomeBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;



    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}