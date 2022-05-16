package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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


public class GroupList extends AppCompatActivity {
    private ArrayList<Group> groups;
    private ArrayList<Group> my_groups;

    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups";
    private static final String MYGROUP_URL = "https://studev.groept.be/api/a21pt103/my_groups/";
    private static final String ADDGROUP_URL = "https://studev.groept.be/api/a21pt103/add_Group/";
    RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private UserInfo user;
    ConstraintLayout cl;
    RecyclerView r;
    LinearLayout layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        requestQueue = Volley.newRequestQueue(this);
        groups = new ArrayList<Group>();
        Button btn_creategroup = (Button) findViewById(R.id.createGroup);

        grabGroups();


        //Group me = new Group(10, "mae", 2222, "ok");
        //groups.add(me);
       // Group de = new Group(12, "hiiii", 2222, "ok");
        //groups.add(de);


        layout = findViewById(R.id.container);
        for (Group m : groups) {
            final View view = getLayoutInflater().inflate(R.layout.row_group, null);
            Button g = view.findViewById(R.id.button_name);

            g.setText(m.getName());
            System.out.println(m.getName());

            g.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(GroupList.this, Group_main_page.class)
                            .putExtra("name", m.getName())
                            .putExtra("id", m.getId()));
                }
            });

            layout.addView(view);


        }
    }

    public void onBtnMain_Clicked(View caller) {
        startActivity(new Intent(GroupList.this, MainPageActivity.class));
        finish();
    }

    public void onBtnCreateGroup_Clicked(View caller) {
        startActivity(new Intent(GroupList.this, CreateGroupActivity.class));
        finish();
    }
    public void grabGroups() {
        String url = GROUP_URL ;
        System.out.println(url);
        JSONObject p = new JSONObject();

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
                                groups.add(g);
                                for (Group m : groups) {
                                    System.out.println(m.getName());
                                }
                                //test
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());




    }
}

 /*   // TODO method to gab MY GROUPS

    public void my_groups()
    {
        int id = UserInfo.getInstance().getId();
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
                                System.out.println(my_groups);
                                for(Group m : my_groups)
                                {
                                    System.out.println(m);
                                }
                                //print_my_groups();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(GroupList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);



    }
public void print_my_groups()
{
    for(Group g: my_groups) {
        Button b = new Button(this);
        recyclerView = findViewById(R.id.groupView);


        b.setLayoutParams(new
                RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        if (recyclerView!= null) {
            recyclerView.addView(b);
        }
    }
}*/
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
