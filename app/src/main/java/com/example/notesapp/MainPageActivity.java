package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.example.notesapp.ui.home.HomeFragment;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button btn_group = (Button)findViewById(R.id.button9);
        Button btn_docs = (Button) findViewById(R.id.button5);
        Button btn_upload = (Button) findViewById(R.id.button);
        Button btn_nav = (Button) findViewById(R.id.button7);


/*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, HomeFragment.class, null)
                    .commit();
        }*/


        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPageActivity.this, myGroups.class));
                finish();

            }
        });

        btn_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPageActivity.this, UserDocument.class));
                finish();

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPageActivity.this, UploadActivity.class));
                finish();

            }
        });

        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPageActivity.this, NavigationDrawerActivity.class));
                finish();

            }
        });
    }


}