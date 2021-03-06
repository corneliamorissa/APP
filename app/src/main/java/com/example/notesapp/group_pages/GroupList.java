package com.example.notesapp.group_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.notesapp.MainPageActivity;
import com.example.notesapp.NaviagtionPage;
import com.example.notesapp.R;
import com.example.notesapp.appObjects.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GroupList extends AppCompatActivity {
    private ArrayList<Group> groups;
    private ArrayList<Integer> alreadyRequested;

    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups";
    private static final String REQUESTJOIN_URL = "https://studev.groept.be/api/a21pt103/send_join_request/";
    private static final String CHECK_URL = "https://studev.groept.be/api/a21pt103/check_if_request_sent/";
    private static final String IS_A_MEMBER = "https://studev.groept.be/api/a21pt103/isMember/";
    private static final String ADDGROUP_URL = "https://studev.groept.be/api/a21pt103/add_Group/";
    private static final String JOIN_URL = "https://studev.groept.be/api/a21pt103/join_group/";
    private static final String GROUPID_URL = "https://studev.groept.be/api/a21pt103/grab_group_from_name/";
    private RequestQueue requestQueue;
    LinearLayout layout;
    String user_name;
    int user_id, groupid;
    ArrayList<Integer> memberID;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");

            //The key argument here must match that used in the other activity
        }
        memberID = new ArrayList<>();
        groups = new ArrayList<Group>();
        alreadyRequested = new ArrayList<Integer>();
        checkIfRequested();

        createGroupDialog();

        Button btn_join = (Button) findViewById(R.id.join_group);
        Button btn_creategroup = (Button) findViewById(R.id.createGroup);

        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("All Groups");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(GroupList.this, NaviagtionPage.class);
                    intent.putExtra("user id", user_id);
                    intent.putExtra("user name", user_name);
                    startActivity(intent);
                    this.finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);


    }
// this populates the screen with all the groups from the database including the ones the memeber is already memeber of. If memeber of a group there is no extra button .
// Otherwise there is a join or requested button next to group name
    public void addButtons()
    {
        String url = GROUP_URL;
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest;

        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                layout = findViewById(R.id.container);
                for (int i = 0; i < response.length(); ++i) {

                    JSONObject o = null;

                    try {
                        o = response.getJSONObject(i);
                        final Group g = new Group(o.getInt("group_id"), o.getString("group_name"), o.getInt("admin_id"), o.getString("add_date"));
                        groups.add(g);

                        getMemberId(g);

                        final View view = getLayoutInflater().inflate(R.layout.row_group, null);
                        Button b = view.findViewById(R.id.button_group_name);
                        Button join = view.findViewById(R.id.join_group);

                        b.setText(g.getName());

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                startActivity(new Intent(GroupList.this, Group_main_page.class)
                                        .putExtra("group name", g.getName())
                                        .putExtra("group id", g.getId())
                                        .putExtra("user id", user_id)
                                        .putExtra("my groups", false)
                                        .putExtra("main page", false)
                                        .putExtra("user name", user_name));
                                finish();

                            }
                        });
                        //if join button pressed a join request is sent to admin of group
                        if(alreadyRequested.contains(g.getId()))
                        {
                            join.setText("requested");
                            join.setTextColor(Color.parseColor("#FFFF9800"));
                            join.setBackgroundColor(Color.parseColor("#FF393939"));

                        }
                        else if(g.getA_id() == user_id)
                        {
                            join.setVisibility(View.INVISIBLE);
                        }
                        else{
                        join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                sendJoinRequest(g.getId());


                            }
                        });}

                        layout.addView(view);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });


        requestQueue.add(queueRequest);
    }
// if user clikc join button of one of the groups this query is sent
    public void sendJoinRequest(int id) {

        String url = REQUESTJOIN_URL + id + "/" + user_id +"/"+ id ;
        System.out.println(url);
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest;
        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(GroupList.this, "Join Request Sent", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GroupList.this, GroupList.class);
                intent.putExtra("user id", user_id);
                intent.putExtra("group id", id);
                intent.putExtra("user name", user_name);
                intent.putExtra("main page", false);
                startActivity(intent);
                finish();
            }



        }
                ,
                error -> Toast.makeText(GroupList.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);



    }

    public void onBtnMain_Clicked(View caller){
        Intent intent = new Intent(GroupList.this, MainPageActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
        finish();
    }


    public void onBtnCreateGroup_Clicked(View caller) {
        dialog.show();

    }
    //if user wants to create group
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

                        for(Group g : groups)
                        {
                            if(g.getName().equals(name.getText().toString())) //checks if group name aready exists
                            {
                                duplicates = true;
                                break;

                            }

                        }
                        if(duplicates)
                        {
                            Toast.makeText(GroupList.this, "Group name already in use", Toast.LENGTH_SHORT).show();

                        }
                        else if(name.getText().toString().trim().length() == 0)
                        {
                            Toast.makeText(GroupList.this, "Please enter group name", Toast.LENGTH_SHORT).show();
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
    //query to add group
    public void addGroup(String name) {
        String url = ADDGROUP_URL + name + "/" + user_id;

        requestQueue = Volley.newRequestQueue(this);

        System.out.println("adding group = "+ url);

        StringRequest queueRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getGroupId(name);
                Intent refresh = new Intent(GroupList.this, GroupList.class);
                refresh.putExtra("user name", user_name).putExtra("user id", user_id).putExtra("main page", false);
                startActivity(refresh); //Start the same Activity
                finish(); //finish Activity.
                Toast.makeText(GroupList.this, "Group created", Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GroupList.this, "Unable to add group", Toast.LENGTH_LONG).show();
                    }

                });

        requestQueue.add(queueRequest);
    }
    // this query checks if join request is already sent to dtermin layout of the join button
    public void checkIfRequested()
    {
        String url = CHECK_URL + user_id;
        System.out.println(url);
        requestQueue = Volley.newRequestQueue(this);
        System.out.println("test");

        JsonArrayRequest queueRequest;

        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
        String info = "";
        layout = findViewById(R.id.container);
        for (int i = 0; i < response.length(); ++i) {


            JSONObject o = null;

            try {
                o = response.getJSONObject(i);
                int r_id = o.getInt("group_id");
                alreadyRequested.add(r_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        addButtons();

    }
}, new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
        Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show(); }
        });


        requestQueue.add(queueRequest);
    }
    // checks if user is member of a group: to determine presence of join button
    public void getMemberId(Group g)
    {

        String url1 = IS_A_MEMBER + g.getId();
        System.out.println(url1);
        requestQueue = Volley.newRequestQueue(GroupList.this);
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
                        int member = o.getInt("user_id");
                        memberID.add(member);

                        final View view = getLayoutInflater().inflate(R.layout.row_group, null);
                        Button b = view.findViewById(R.id.button_group_name);
                        Button join = view.findViewById(R.id.join_group);

                        b.setText(g.getName());
                        for(Integer id: memberID) {
                            if (id.intValue() == user_id) {
                                isMember = true;
                            }
                        }

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                startActivity(new Intent(GroupList.this, Group_main_page.class)
                                        .putExtra("group name", g.getName())
                                        .putExtra("group id", g.getId())
                                        .putExtra("user id", user_id)
                                        .putExtra("my groups", false)
                                        .putExtra("user name", user_name)
                                        .putExtra("main page", false)
                                );

                            }
                        });
                        //if join button pressed a join request is sent to admin of group
                        if(alreadyRequested.contains(g.getId()))
                        {
                            join.setText("requested");
                            join.setTextColor(Color.parseColor("#FFFF9800"));
                            join.setBackgroundColor(Color.parseColor("#FF393939"));

                        }
                        else if((g.getA_id() == user_id) || isMember)
                        {
                            join.setVisibility(View.INVISIBLE);
                        }
                        else{
                            join.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    sendJoinRequest(g.getId());


                                }
                            });}

                        layout.addView(view);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }},
                error -> Toast.makeText(GroupList.this, "Unable to communicate with server", Toast.LENGTH_LONG).show());
        requestQueue.add(queueRequest1);
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
                Toast.makeText(GroupList.this, "group ok", Toast.LENGTH_LONG).show();

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(queueRequest2);
    }

    public void getGroupId(String name)
    {

        String url1 = GROUPID_URL + name;
        System.out.println("get group id ="+url1);
        requestQueue = Volley.newRequestQueue(GroupList.this);
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
                        addToMember(o.getInt("group id"));



                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }},
                error -> Toast.makeText(GroupList.this, "Unable to communicate with server", Toast.LENGTH_LONG).show());
        requestQueue.add(queueRequest1);
}


    }


