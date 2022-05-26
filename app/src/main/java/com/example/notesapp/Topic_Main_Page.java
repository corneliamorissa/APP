package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    private ArrayList<Topic> topics;
    Integer groupid, userid;
    String groupName,userName;
    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    Button btn_nav;
    //this page is the list of topics of the group
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_page);
        btn_nav = findViewById(R.id.nav_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            groupName = extras.getString("group name").toUpperCase(Locale.ROOT);
            groupid = extras.getInt("group id");
            userid = extras.getInt("user id");
            userName = extras.getString("user name");
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


        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Topic_Main_Page.this, NaviagtionPage.class);
                intent.putExtra("group id", groupid);
                intent.putExtra("user id", userName);
                intent.putExtra("group name", groupName);
                intent.putExtra("user name", userName);
                startActivity(intent);
                finish();

            }
        });
    }
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_topic, null);

        final EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addTopic(name.getText().toString());
                        newTopic(name.getText().toString());
                        Topic t = new Topic(name.getText().toString(), groupid);
                        topics.add(t);
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
                        .putExtra("user id", userName)
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
                            for (int i = 0; i < response.length(); ++i) {
                                JSONObject o = null;
                                layout = findViewById(R.id.container_topic);
                                try {
                                    o = response.getJSONObject(i);

                                    Topic t = new Topic(o.getInt("topic_id"),o.getString("topic_name"),groupid);
                                   topics.add(t);
                                    final View view = getLayoutInflater().inflate(R.layout.row_topic, null);


                                    Button topic_btn = view.findViewById(R.id.button__topic_name);

                                    topic_btn.setText(t.getName());

                                    topic_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(Topic_Main_Page.this, Topic_Activity.class)
                                                    .putExtra("topic name",t.getName())
                                                    .putExtra("topic id",t.getId())
                                                    .putExtra("group id",groupid)
                                            );
                                        }
                                    });

                                    layout.addView(view);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        }
                    },
                    error -> Toast.makeText(Topic_Main_Page.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

            requestQueue.add(queueRequest);
        }


        public void newTopic(String name)
        {

            String url = NEW_TOPIC_URL;
            requestQueue = Volley.newRequestQueue(this);
            JSONObject p = new JSONObject();
            String requestURL = url + groupid + "/" + name;

            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.POST,
                    requestURL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                                Toast.makeText(Topic_Main_Page.this, "topic added", Toast.LENGTH_SHORT).show();

                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Topic_Main_Page.this, "Unable to add topic", Toast.LENGTH_LONG).show();

                                    }


                        });

                        //error -> Toast.makeText(Topic_Main_Page.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());


        }
    }
//TODO add topic page  layout


