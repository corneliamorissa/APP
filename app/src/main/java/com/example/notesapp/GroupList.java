package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Group;

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

    public GroupList() {
       groups = new ArrayList<Group>();
       my_groups = new ArrayList<Group>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
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
                                String name = (String) o.get("group_name");
                                String date =  (String) o.get("add_date");
                                int a_id = Integer.parseInt((String) o.get("admin_id"));
                                Group g = new Group(id,name,a_id,date);
                                groups.add(g);
                                for(Group m : groups)
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
    }

    //TODO method to add a group

    /*public void add_groups()
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