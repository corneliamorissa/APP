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
        super.onCreate(savedInstanceState);
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        EditTextPreference changePassw = findPreference("account");
        ListPreference reminder = findPreference("reminder");
        ListPreference uploadS = findPreference("pref_uploads");
        EditTextPreference deleteacc = findPreference("delete_acc");
        EditTextPreference logOut = findPreference("logout");

        assert reminder != null;
        reminder.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
        assert changePassw != null;
        changePassw.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());



        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }


}