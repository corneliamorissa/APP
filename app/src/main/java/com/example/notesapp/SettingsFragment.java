package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    TextView changePass;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        EditTextPreference changePassw = findPreference("account");
        ListPreference deleteacc = findPreference("delete_acc");
        ListPreference logOut = findPreference("logout");

        assert changePassw != null;
        changePassw.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());




    }


}