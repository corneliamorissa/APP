package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.example.notesapp.ui.home.HomeFragment;

import java.util.Locale;

public class MainPageActivity extends AppCompatActivity {
    private String user_name;
    private int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button btn_group = (Button)findViewById(R.id.button9);
        Button btn_docs = (Button) findViewById(R.id.button5);
        Button btn_upload = (Button) findViewById(R.id.button);
        Button btn_nav = (Button) findViewById(R.id.button2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            //The key argument here must match that used in the other activity
        }


/*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, HomeFragment.class, null)
                    .commit();
        }*/


        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent(MainPageActivity.this, myGroups.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", user_id);
                startActivity(intent);
                finish();

            }
        });

        btn_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPageActivity.this, UserDocument.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", user_id);
                startActivity(intent);
                finish();

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPageActivity.this, UploadActivity.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", user_name);
                startActivity(intent);
                finish();

            }
        });

        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPageActivity.this, NaviagtionPage.class);
                intent.putExtra("user id", user_id);
                intent.putExtra("user name", user_name);
                startActivity(intent);
                finish();

            }
        });
    }

    //notification



}