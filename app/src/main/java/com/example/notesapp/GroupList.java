package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Group;
import com.example.notesapp.userInfo.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GroupList extends AppCompatActivity {
    private ArrayList<Group> groups;
    private ArrayList<Group> my_groups;
    private ArrayList<Integer> alreadyRequested;

    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups";
    private static final String JOINGroup_URL = "https://studev.groept.be/api/a21pt103/join_group/";
    private static final String REQUESTJOIN_URL = "https://studev.groept.be/api/a21pt103/send_join_request/";
    private static final String CHECK_URL = "https://studev.groept.be/api/a21pt103/check_if_request_sent/";
    RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private UserInfo user;
    LinearLayout layout;
    String user_name;
    int user_id;


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

        groups = new ArrayList<Group>();
        alreadyRequested = new ArrayList<Integer>();
        checkIfRequested();

        Button btn_creategroup = (Button) findViewById(R.id.createGroup);

    }

    public void addButtons()
    {
        String url = GROUP_URL;
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

                    System.out.println("test1");
                    JSONObject o = null;

                    try {
                        o = response.getJSONObject(i);
                        final Group g = new Group(o.getInt("group_id"), o.getString("group_name"), o.getInt("admin_id"), o.getString("add_date"));
                        System.out.println(g.getName());
                        System.out.println(g.getId());
                        groups.add(g);
                        final View view = getLayoutInflater().inflate(R.layout.row_group, null);
                        Button b = view.findViewById(R.id.button__topic_name);
                        Button join = view.findViewById(R.id.join_group);

                        b.setText(g.getName());
                        System.out.println(g.getName());

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(GroupList.this, Group_main_page.class)
                                        .putExtra("group name", g.getName())
                                        .putExtra("group id", g.getId())
                                        .putExtra("user id", user_id)
                                        .putExtra("user name", user_name));

                            }
                        });
                        //if join button pressed a join request is sent to admin of group
                        if(alreadyRequested.contains(g.getId()))
                        {
                            join.setText("requested");
                            //join.setClickable(false);
                            //join.setBackgroundColor(Color.GRAY);
                            System.out.println("buttontest1");
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
                for (Group m : groups) {
                    System.out.println(m.getName());
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

    public void sendJoinRequest(int id) {
        String url = REQUESTJOIN_URL + id + user_id + id ;
        System.out.println(url);

        StringRequest queueRequest;

        queueRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(GroupList.this, Group_main_page.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(queueRequest);


    }

    public void onBtnMain_Clicked(View caller){
        Intent intent = new Intent(GroupList.this, MainPageActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
        finish();
    }


    public void onBtnCreateGroup_Clicked(View caller) {
        Intent intent = new Intent(GroupList.this, CreateGroupActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
        finish();
    }

    public void grabGroups() {
        String url = GROUP_URL;
        System.out.println(url);

        System.out.println("test");
        JsonArrayRequest queueRequest;

        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                for (int i = 0; i < response.length(); ++i) {
                    System.out.println("test1");
                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(i);
                        Group g = new Group(o.getInt("group_id"), o.getString("group_name"), o.getInt("admin_id"), o.getString("add_date"));
                        groups.add(g);
                        System.out.println("test2");
                    } catch (JSONException e) {
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
        requestQueue = Volley.newRequestQueue(this);
    }
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

            System.out.println("test1");
            JSONObject o = null;

            try {
                o = response.getJSONObject(i);
                int r_id = o.getInt("group_id");
                alreadyRequested.add(r_id);
                System.out.println(r_id);
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
}
