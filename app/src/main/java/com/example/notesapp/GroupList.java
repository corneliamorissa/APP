package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupList extends AppCompatActivity {
    private ArrayList<Integer> groups;
    private ArrayList<Integer> my_groups;
    private RequestQueue requestQueue;
    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups";
    private static final String MYGROUP_URL = "https://studev.groept.be/api/a21pt103/my_groups/";
    private static final String ADDGROUP_URL = "https://studev.groept.be/api/a21pt103/add_Group/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ArrayList<Integer> groups = new ArrayList<Integer>();
        ArrayList<Integer> my_groups = new ArrayList<Integer>();
        requestQueue = Volley.newRequestQueue(this);

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
                                groups.add(id);
                                System.out.println(id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);
    }

    // TODO method to gab MY GROUPS
    public void my_groups()
    {
        String pass = MYGROUP_URL;

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
                                my_groups.add(id);
                                System.out.println(id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);
    }

    //TODO method to add a group

    public void add_groups()
    {
        String pass = ADDGROUP_URL;

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
                                my_groups.add(id);
                                System.out.println(id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);
    }

    //TODO method to join a group
}