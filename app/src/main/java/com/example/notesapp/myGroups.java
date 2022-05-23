package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Group;
import com.example.notesapp.userInfo.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class myGroups extends AppCompatActivity {
    private ArrayList<Group> myGroups;
    private static final String MYGROUP_URL = "https://studev.groept.be/api/a21pt103/my_groups/";
    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups/";
    private UserInfo user;
    private RequestQueue requestQueue;
    LinearLayout layout;
    private String user_name;
    private int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        requestQueue = Volley.newRequestQueue(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            //The key argument here must match that used in the other activity
        }
        myGroups = new ArrayList<Group>();
        System.out.println(user_id);
        String s = Integer.toString(user_id);

        String url = MYGROUP_URL + s;
        System.out.println(url);
        JsonArrayRequest queueRequest;
        queueRequest = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                layout = findViewById(R.id.container);
                for (int i=0; i<response.length(); ++i) {

                    System.out.println("test1");
                    JSONObject o = null;

                    try {
                        o = response.getJSONObject(i);
                        final Group g = new Group(o.getInt("group_id"), o.getString("group_name"), o.getInt("admin_id"), o.getString("add_date"));
                        System.out.println(g.getName());
                        System.out.println(g.getId());
                        myGroups.add(g);
                        System.out.println(g.getName());
                        final View view = getLayoutInflater().inflate(R.layout.my_group_row, null);
                        Button b = view.findViewById(R.id.button_name);

                        b.setText(g.getName());
                        System.out.println(g.getName());

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(myGroups.this, Group_main_page.class)
                                        .putExtra("name", g.getName())
                                        .putExtra("group_id", g.getId())
                                        .putExtra("user_id", user_id));
                            }
                        });

                        layout.addView(view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
                for(Group m : myGroups)
                {
                    System.out.println(m.getName());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(myGroups.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(queueRequest);
        //grabGroups();

/*
        layout = findViewById(R.id.container);
        for (Group m : myGroups) {
            final View view = getLayoutInflater().inflate(R.layout.row_group, null);
            Button g = view.findViewById(R.id.button_name);

            g.setText(m.getName());
            System.out.println(m.getName());

            g.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(myGroups.this, Group_main_page.class)
                            .putExtra("name", m.getName())
                            .putExtra("id", m.getId()));
                }
            });

            layout.addView(view);
        }
        */

    }


    public void onBtnMain_Clicked(View caller) {
        Intent intent = new Intent(myGroups.this, MainPageActivity.class);
        intent.putExtra("user_id",user_id);
        startActivity(intent);
        finish();
    }

    public void onBtnCreateGroup_Clicked(View caller) {
        Intent intent = new Intent(myGroups.this, CreateGroupActivity.class);
        System.out.println(user_id);
        intent.putExtra("admin_id",user_id);
        startActivity(intent);
        finish();
    }

    public void grabGroups() {
        String url = MYGROUP_URL + '1';
        System.out.println(url);
        JSONObject p = new JSONObject();
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject o = null;
                            try {
                                o = response.getJSONObject(i);

                                int id = (int) o.get("group_id");
                                System.out.println(id);
                                String name = o.get("group_name").toString();
                                System.out.println(name);
                                String date = o.get("add_date").toString();
                                System.out.println(date);
                                int a_id = (int) o.get("admin_id");
                                Group g = new Group(id, name, a_id, date);
                                myGroups.add(g);
                                for (Group m : myGroups) {
                                    System.out.println(m.getName());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(myGroups.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);

    }

}
