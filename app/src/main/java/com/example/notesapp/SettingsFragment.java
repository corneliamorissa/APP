package com.example.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.os.Bundle;

import com.android.car.ui.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.root_preferences);
    }
}