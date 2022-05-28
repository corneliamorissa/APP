package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class NaviagtionPage extends AppCompatActivity {

    int id;
    String name, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naviagtion_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("user name");
            id = extras.getInt("user id");
            email = extras.getString("email");
            //The key argument here must match that used in the other activity
        }

        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("Navigation Bar");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


        Button group = findViewById(R.id.groups_nav);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NaviagtionPage.this, GroupList.class);
                intent.putExtra("user id", id);
                intent.putExtra("user name", name);
                intent.putExtra("main page", false);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        Button mygroup = findViewById(R.id.mygroups_nav);
        mygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NaviagtionPage.this, myGroups.class);
                intent.putExtra("user id", id);
                intent.putExtra("user name", name);
                intent.putExtra("email", email);
                intent.putExtra("main page", false);
                startActivity(intent);
            }
        });
        Button setting = findViewById(R.id.settings_nav);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NaviagtionPage.this, SettingsActivity.class);
                intent.putExtra("user id", id);
                intent.putExtra("user name", name);
                intent.putExtra("main page", false);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        Button profile = findViewById(R.id.profile_nav);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NaviagtionPage.this, UserProfileActivity.class);
                intent.putExtra("user id", id);
                intent.putExtra("user name", name);
                intent.putExtra("email", email);
                intent.putExtra("main page", false);
                startActivity(intent);
            }
        });
        Button docs = findViewById(R.id.docs_nav);
        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NaviagtionPage.this, UserDocument.class);
                intent.putExtra("user id", id);
                intent.putExtra("user name", name);
                intent.putExtra("main page", false);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(NaviagtionPage.this, MainPageActivity.class);
                intent.putExtra("user id", id);
                intent.putExtra("user name", name);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}