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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        requestQueue = Volley.newRequestQueue(this);
        //recyclerView = findViewById(R.id.rec_view);
        ////LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        //recyclerView.setLayoutManager(layoutManager);
        myGroups = new ArrayList<Group>();
        //Group me = new Group(10, "mae", 2222, "ok");
        //myGroups.add(me);
        //Group de = new Group(12, "dee", 2222, "ok");
        //myGroups.add(de);

        grabGroups();


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
    }


    public void onBtnMain_Clicked(View caller) {
        startActivity(new Intent(myGroups.this, MainPageActivity.class));
        finish();
    }

    public void onBtnCreateGroup_Clicked(View caller) {
        startActivity(new Intent(myGroups.this, CreateGroupActivity.class));
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

/*
        recyclerView.setAdapter(new GroupAdapter(myGroups));
        View view = getLayoutInflater().inflate(R.layout.row_group, null);


        Button name = view.findViewById(R.id.text_group_name);



        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(myGroups.this, Group_main_page.class)
                        .putExtra("name",name.getText()));
            }
        });


*/












/*

    public void groupClick()
    {


        Button button = (Button) findViewById(R.id.text_group_name);
        Intent intent = new Intent(this, Group_main_page.class);
        intent.putExtra("name",button.getText());

    }

    @Override
    public void onClick(@NonNull View view) {
         ;

       // Button button = (Button) itemView.findViewById(R.id.text_group_name);
        Intent intent = new Intent(this, Group_main_page.class);
        //intent.putExtra("name",button.getText());

    }

 */
