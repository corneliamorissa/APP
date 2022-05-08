package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Topic_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Button btn_group = (Button)findViewById(R.id.button13);
        Button btn_docs = (Button) findViewById(R.id.button12);
        Button btn_upload = (Button) findViewById(R.id.button11);
        Button btn_nav = (Button) findViewById(R.id.button10);

        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Topic_Activity.this, GroupList.class));
                finish();

            }
        });

        btn_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Topic_Activity.this, MainPageActivity.class));
                finish();

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Topic_Activity.this, UploadActivity.class));
                finish();

            }
        });

        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public   void onClick(View v) {

                startActivity(new Intent(Topic_Activity.this, NavigationDrawerActivity.class));
                finish();

            }
        });
    }
    //TODO method to grab all docs in the topic

}


