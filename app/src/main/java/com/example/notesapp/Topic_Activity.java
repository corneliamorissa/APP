package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Group;
import com.example.notesapp.appObjects.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Topic_Activity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String TOPIC_URL = "https://studev.groept.be/api/a21pt103/grap_topics/";
    private int group_id;
    LinearLayout layout;
    private int user_id;
    private int topic_id;
    private ArrayList<Topic> topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_page);
        Bundle extras = getIntent().getExtras();
        topics = new ArrayList<Topic>();
        if (extras != null) {
            user_id = extras.getInt("user_id");
            group_id = extras.getInt("group_id");
            topic_id =extras.getInt("topic_id");
            //The key argument here must match that used in the other activity
        }

        Button btn_nav = (Button) findViewById(R.id.nav_button);
        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Topic_Activity.this, NavigationDrawerActivity.class));
                finish();

            }
        });
    }
    //TODO method to grab all docs in the topic

}


