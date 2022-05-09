package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


public class GroupList extends AppCompatActivity {
    private ArrayList<Group> groups;
    private ArrayList<Group> my_groups;
    private RequestQueue requestQueue;
    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups";
    private static final String MYGROUP_URL = "https://studev.groept.be/api/a21pt103/my_groups/";
    private static final String ADDGROUP_URL = "https://studev.groept.be/api/a21pt103/add_Group/";
    private UserInfo user;
    ConstraintLayout cl;
    RecyclerView recyclerView;

    public GroupList() {
       groups = new ArrayList<Group>();
       my_groups = new ArrayList<Group>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        requestQueue = Volley.newRequestQueue(this);
        get_groups();
        my_groups();

    }

    //TODO method to grab all Groups
    public void get_groups()
    {
        String pass = GROUP_URL;
        System.out.println(pass);
        JSONObject p = new JSONObject();

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
                                int id = Integer.parseInt((String) o.get("group_id"));
                                String name = (String) o.get("group_name");
                                String date =  (String) o.get("add_date");
                                int a_id = Integer.parseInt((String) o.get("admin_id"));
                                Group g = new Group(id,name,a_id,date);
                                groups.add(g);
                                for(Group m : groups)
                                {
                                    System.out.println(m.getName());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);
        for(Group g: groups) {
            Button b = new Button(this);
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setHasFixedSize(true);
            b.setText("join");
            b.setLayoutParams(new
                    RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            if (recyclerView!= null) {
                recyclerView.addView(b);
            }
        }
    }

    // TODO method to gab MY GROUPS
    @SuppressLint("SetTextI18n")
    public void my_groups()
    {
        int id = user.getId();
        String pass = MYGROUP_URL +"/" + id;

        JSONObject p = new JSONObject();

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

                                int id = Integer.parseInt((String) o.get("group_id"));
                                String name = (String) o.get("group_name");
                                String date =  (String) o.get("add_date");
                                int a_id = Integer.parseInt((String) o.get("admin_id"));
                                Group g = new Group(id,name,a_id,date);
                                my_groups.add(g);
                                for(Group m : my_groups)
                                {
                                    System.out.println(m);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);

        for(Group g: my_groups) {
            Button b = new Button(this);
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setHasFixedSize(true);
            b.setText("join");
            b.setLayoutParams(new
                    RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            if (recyclerView!= null) {
                recyclerView.addView(b);
            }
        }
    }

    //TODO method to add a group

    /*public void add_groups()
    {

        String pass = ADDGROUP_URL + "/" + g_name + "/" + user.getId();;

        JSONObject p = new JSONObject();

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
                                System.out.println(2);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);
    }
*/
    //TODO method to join a group

    //TODO creating a group page
}