package com.example.notesapp.group_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;


import android.content.Intent;
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
import com.example.notesapp.MainPageActivity;
import com.example.notesapp.NaviagtionPage;
import com.example.notesapp.R;
import com.example.notesapp.appObjects.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class myGroups extends AppCompatActivity {
    private ArrayList<Group> myGroups;
    private ArrayList<Group> allGroups;
    private static final String MYGROUP_URL = "https://studev.groept.be/api/a21pt103/my_groups/";
    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups/";
    private static final String ADDGROUP_URL = "https://studev.groept.be/api/a21pt103/add_Group/";
    private static final String JOIN_URL = "https://studev.groept.be/api/a21pt103/join_group/";
    private static final String GROUPID_URL = "https://studev.groept.be/api/a21pt103/grab_group_from_name/";
    private RequestQueue requestQueue;
    LinearLayout layout;
    private String user_name;
    private int user_id;
    AlertDialog dialog;
    boolean mainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        requestQueue = Volley.newRequestQueue(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            mainPage = extras.getBoolean("main page");
            //The key argument here must match that used in the other activity
        }

        myGroups = new ArrayList<Group>();
        allGroups = new ArrayList<Group>();
        grabMyGroups(); //grabing all group for which user is memeber

        grabAllGroups();// grabing all group to be used for add groups fucntionallity

        createGroupDialog(); // this is for add groups

        ActionBar actionBar = getSupportActionBar(); // for the back button
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("My Groups");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override // back button
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mainPage) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(myGroups.this, MainPageActivity.class);
                    intent.putExtra("user id", user_id);
                    intent.putExtra("user name", user_name);
                    startActivity(intent);
                    this.finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
        else
        {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(myGroups.this, NaviagtionPage.class);
                    intent.putExtra("user id", user_id);
                    intent.putExtra("user name", user_name);
                    startActivity(intent);
                    this.finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }

// add button
    public void onBtnCreateGroup_Clicked(View caller) {
        dialog.show();
    }
// making array list of all groups
    public void grabAllGroups() {
        String url = GROUP_URL;
        System.out.println(url);
        requestQueue = Volley.newRequestQueue(this);
        System.out.println("test");
        JsonArrayRequest queueRequest;

        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); ++i) {

                    System.out.println("test1");
                    JSONObject o = null;

                    try {
                        o = response.getJSONObject(i);
                        final Group g = new Group(o.getInt("group_id"), o.getString("group_name"), o.getInt("admin_id"), o.getString("add_date"));
                        System.out.println(g.getName());
                        System.out.println(g.getId());
                        allGroups.add(g);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(myGroups.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });


        requestQueue.add(queueRequest);

    }
// grabing groups to be displayed
    public void grabMyGroups()
    {
        String url = MYGROUP_URL + user_id + "/" + user_id;
        System.out.println(url);

        JsonArrayRequest queueRequest;

        queueRequest = new JsonArrayRequest(Request.Method.GET,url,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("test0");
                layout = findViewById(R.id.container);
                System.out.println("test00");

                for(int i = 0; i < response.length(); ++i) {

                    System.out.println("test1");
                    JSONObject o = null;

                    try{
                        o = response.getJSONObject(i);
                        final Group g = new Group(o.getInt("group_id"), o.getString("group_name"), o.getInt("admin_id"), o.getString("add_date"));
                        System.out.println(g.getName());
                        myGroups.add(g);


                        final View view = getLayoutInflater().inflate(R.layout.my_group_row, null);
                        Button b = view.findViewById(R.id.button__topic_name);

                        b.setText(g.getName());
                        System.out.println(g.getName());

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(myGroups.this, Group_main_page.class)
                                        .putExtra("group name", g.getName())
                                        .putExtra("group id", g.getId())
                                        .putExtra("my groups", true)
                                        .putExtra("user id", user_id)
                                        .putExtra("user name", user_name)
                                        .putExtra("main page", mainPage)
                                );
                                finish();
                            }
                        });

                        layout.addView(view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                for (Group m : myGroups) {
                    System.out.println(m.getName());
                }


            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(myGroups.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(queueRequest);
    }
//add group dialog
    private void createGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_topic, null);

        final EditText name = view.findViewById(R.id.nameEdit);


        builder.setView(view);
        builder.setTitle("Create Group")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean duplicates  = false;


                        for(Group g : allGroups)
                        {
                            if(g.getName().equals(name.getText().toString()))
                            {
                                duplicates = true;
                                break;

                            }

                        }
                        if(duplicates)
                        {
                            Toast.makeText(myGroups.this, "Group name already in use", Toast.LENGTH_SHORT).show();

                        }
                        else if(name.getText().toString().trim().length() == 0)
                        {
                            Toast.makeText(myGroups.this, "Please enter group name", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            addGroup(name.getText().toString());
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
// add group query
    public void addGroup(String name)
    {
        String url = ADDGROUP_URL + name + "/" + user_id;

        requestQueue = Volley.newRequestQueue(this);

        System.out.println(url);

        StringRequest queueRequest = new StringRequest(Request.Method.GET,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                getGroupId(name);
                Intent refresh = new Intent(myGroups.this, myGroups.class);
                refresh.putExtra("user name", user_name).putExtra("user id", user_id).putExtra("my groups", true).putExtra("main page", mainPage);
                startActivity(refresh); //Start the same Activity
                finish(); //finish Activity.
                Toast.makeText(myGroups.this, "Group created", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(myGroups.this, "Unable to add group", Toast.LENGTH_LONG).show();
                    }

                });

        requestQueue.add(queueRequest);

    }
    public void addToMember(int g_id)
    {
        requestQueue = Volley.newRequestQueue(this);
        String url2 = JOIN_URL+ g_id + "/" + user_id ;
        System.out.println("adding admin as member ="+url2);

        StringRequest queueRequest2;

        queueRequest2 = new StringRequest(Request.Method.GET,url2,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(myGroups.this, "group ok", Toast.LENGTH_LONG).show();

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(myGroups.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(queueRequest2);
    }

    public void getGroupId(String name)
    {

        String url1 = GROUPID_URL + name;
        System.out.println("get group id ="+url1);
        requestQueue = Volley.newRequestQueue(myGroups.this);
        JsonArrayRequest queueRequest1;
        queueRequest1 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";

                for (int i = 0; i < response.length(); ++i) {

                    JSONObject o = null;
                    try {
                        boolean isMember = false;
                        o = response.getJSONObject(i);
                        addToMember(o.getInt("group_id"));



                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }},
                error -> Toast.makeText(myGroups.this, "Unable to communicate with server", Toast.LENGTH_LONG).show());
        requestQueue.add(queueRequest1);
    }

}
