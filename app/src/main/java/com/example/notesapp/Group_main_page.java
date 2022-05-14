package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Group_main_page extends AppCompatActivity {
    public Group_main_page(String name)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main_page);
    }

    public void onTopic_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class));
        finish();
    }
    public void onMemebrs_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class));
        finish();
    }
}