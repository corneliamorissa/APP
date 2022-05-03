package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Topic_Main_Page extends AppCompatActivity {

    public Topic_Main_Page(int groupId)
    {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_page);

        Button btn_group = (Button)findViewById(R.id.button4);
        Button btn_docs = (Button) findViewById(R.id.button3);
        Button btn_upload = (Button) findViewById(R.id.button2);
        Button btn_nav = (Button) findViewById(R.id.button6);

        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Topic_Main_Page.this, GroupList.class));
                finish();

            }
        });

        btn_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Topic_Main_Page.this, MainPageActivity.class));
                finish();

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Topic_Main_Page.this, UploadActivity.class));
                finish();

            }
        });

        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Topic_Main_Page.this, NavigationSettingsActivity.class));
                finish();

            }
        });
    }


    //TODO method to grab all topic for a group
    private void grabTopics()
    {

    }



}