package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class myGroups extends AppCompatActivity  implements View.OnClickListener{
    private ArrayList<Group> myGroups;
    private static final String MYGROUP_URL = "https://studev.groept.be/api/a21pt103/my_groups/";
    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups/";
    private UserInfo user;
    RecyclerView recyclerView;
    Button buttonAdd;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        requestQueue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.rec_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        myGroups = new ArrayList<Group>();
        Group me = new Group(10,"mae",2222,"ok");
        myGroups.add(me);
        Group de = new Group(12,"dee",2222,"ok");
        myGroups.add(de);
        String pass = MYGROUP_URL + 1;
        System.out.println(pass);
        //String pass = GROUP_URL;1


        JSONObject p = new JSONObject();

        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                pass,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); ++i) {
                        JSONObject o = null;
                            try {
                                o = response.getJSONObject(i);

                                int id = Integer.parseInt((String) o.get("group_id"));
                                System.out.println(id);
                                String name =  o.get("group_name").toString();
                                System.out.println(name);
                                String date =   o.get("add_date").toString();
                                System.out.println(date);
                                int a_id = Integer.parseInt((String) o.get("admin_id"));
                                Group g = new Group(id,name,a_id,date);
                                myGroups.add(g);
                                for(Group m : myGroups)
                                {
                                    System.out.println(m.getName());
                                }
                                //print_my_groups();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(myGroups.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);

        recyclerView.setAdapter(new GroupAdapter(myGroups));





    }

    public void onBtnMain_Clicked(View caller) {
        startActivity(new Intent(myGroups.this, MainPageActivity.class));
        finish();
    }
    public void onBtnCreateGroup_Clicked(View caller) {
        startActivity(new Intent(myGroups.this, CreateGroupActivity.class));
        finish();
    }



    /*public void print_my_groups()
    {

        for(Group g: my_groups) {
            System.out.println(g.getName());
            
            addView();
            Button b = new Button(this);
            b.setId(g.getId());
            b.setText("join");
            t.addView(b);

            TextView name = new TextView(this);
            name.setText(g.getName());
            t.addView(name);



        }
    }*/

    private void addView() {



        /*View groupView = getLayoutInflater().inflate(R.layout.row_add_group,null,false);

        AppCompatSpinner spinnerTeam = (AppCompatSpinner) groupView.findViewById(R.id.spinner_team);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,my_groups);
        spinnerTeam.setAdapter(arrayAdapter);

        layoutList.addView(groupView); */

    }
    public void groupClick()
    {



        startActivity(new Intent(myGroups.this, Group_main_page.class));
        finish();
    }

    @Override
    public void onClick(View view) {

    }
}