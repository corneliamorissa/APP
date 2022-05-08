package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class Topic_Main_Page extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String TOPIC_URL = "https://studev.groept.be/api/a21pt103/grap_topics/";
    private ArrayList<Topic> topics;
    private int id;
    public Topic_Main_Page()
    {
    topics = new ArrayList<Topic>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_page);
        requestQueue = Volley.newRequestQueue(this);
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
            String pass = TOPIC_URL;

            JSONObject p = new JSONObject();
        String requestURL = pass + "/" + id;

            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                    pass,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            int p;
                            for (int i = 0; i < response.length(); ++i) {
                                JSONObject o = null;
                                try {

                                    int t_id = Integer.parseInt((String) o.get("topic_id"));
                                    String name = (String) o.get("topic_name");
                                    int g_id = Integer.parseInt((String) o.get("group_id"));
                                    Topic t = new Topic(id,name,g_id);
                                   topics.add(t);
                                    for(Topic m : topics)
                                    {
                                        System.out.println(m);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        }
                    },
                    error -> Toast.makeText(Topic_Main_Page.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

            requestQueue.add(queueRequest);
        }
    }



