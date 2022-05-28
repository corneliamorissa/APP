package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Topic_Main_Page extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String TOPIC_URL = "https://studev.groept.be/api/a21pt103/grap_topics/";
    private static final String NEW_TOPIC_URL = "https://studev.groept.be/api/a21pt103/add_topic/";
    private static final String DELETE = "https://studev.groept.be/api/a21pt103/deleteTopic/";
    private ArrayList<Topic> topics;
    Integer groupid, userid;
    String groupName,userName;
    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    Button btn_nav;
    boolean mainpage, mygroups;
    //this page is the list of topics of the group
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            groupName = extras.getString("group name");
            groupid = extras.getInt("group id");
            userid = extras.getInt("user id");
            userName = extras.getString("user name");
            mainpage = extras.getBoolean("main page");
            mygroups = extras.getBoolean("my groups");
            //The key argument here must match that used in the other activity
        }

        requestQueue = Volley.newRequestQueue(this);

        topics = new ArrayList<>();
        add = findViewById(R.id.add);
        layout = findViewById(R.id.container_topic);

        grabTopics();

        buildDialog();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        ActionBar actionBar = getSupportActionBar();


        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("Topics");


        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Topic_Main_Page.this,Group_main_page.class);
                intent.putExtra("user id", userid );
                intent.putExtra("user name", userName);
                intent.putExtra("group id", groupid);
                intent.putExtra("group name", groupName);
                intent.putExtra("main page", mainpage);
                intent.putExtra("my groups", mygroups);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_topic, null);

        final EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Create Topic")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean duplicates  = false;

                        for(Topic t : topics)
                        {
                            if(t.getName().equals(name.getText().toString()))
                            {
                                duplicates = true;
                                break;

                            }

                        }
                        if(duplicates)
                        {
                            Toast.makeText(Topic_Main_Page.this, "Topic name already exists", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            addTopic(name.getText().toString());
                            newTopic(name.getText().toString());
                            Topic t = new Topic(name.getText().toString(), groupid);
                            topics.add(t);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
    }

    private void addTopic(String name) {
        final View view = getLayoutInflater().inflate(R.layout.row_topic, null);


        Button topic_btn = view.findViewById(R.id.button__topic_name);

        topic_btn.setText(name);

        topic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Topic_Main_Page.this, Topic_Activity.class)
                        .putExtra("topic name",name)
                        .putExtra("group id", groupid)
                        .putExtra("user name", userName)
                        .putExtra("user id", userid)
                        .putExtra("group name", groupName)
                        .putExtra("main page",mainpage)
                        .putExtra("my groups", mygroups)

                        );
            }
        });

        layout.addView(view);
    }


    //TODO method to grab all topic for a group
    private void grabTopics()
    {
            String url = TOPIC_URL + groupid;

            JSONObject p = new JSONObject();


            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            int p;
                            if(response.length()==0)
                            {

                            }
                            else {
                                for (int i = 0; i < response.length(); ++i) {
                                    JSONObject o = null;
                                    layout = findViewById(R.id.container_topic);
                                    try {
                                        o = response.getJSONObject(i);

                                        Topic t = new Topic(o.getInt("topic_id"), o.getString("topic_name"), groupid);
                                        topics.add(t);
                                        final View view = getLayoutInflater().inflate(R.layout.row_topic, null);


                                        Button topic_btn = view.findViewById(R.id.button__topic_name);

                                        topic_btn.setText(t.getName());

                                        topic_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(Topic_Main_Page.this, Topic_Activity.class)
                                                        .putExtra("topic name", t.getName())
                                                        .putExtra("topic id", t.getId())
                                                        .putExtra("group id", groupid)
                                                        .putExtra("user id", userid)
                                                        .putExtra("user name", userName)
                                                        .putExtra("group name", groupName)
                                                        .putExtra("main page", mainpage)
                                                        .putExtra("my groups", mygroups)
                                                );
                                            }
                                        });

                                        layout.addView(view);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }
                    },
                    error -> Toast.makeText(Topic_Main_Page.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

            requestQueue.add(queueRequest);
        }


        public void newTopic(String name)
        {

            String url = NEW_TOPIC_URL + groupid + "/" + name;
            requestQueue = Volley.newRequestQueue(this);

            System.out.println(url);

            StringRequest queueRequest = new StringRequest(Request.Method.GET,url,new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                                Toast.makeText(Topic_Main_Page.this, "topic added", Toast.LENGTH_SHORT).show();

                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Topic_Main_Page.this, "Unable to add topic", Toast.LENGTH_LONG).show();

                                    }


                        });

            requestQueue.add(queueRequest);

        }

    }
//TODO add topic page  layout


