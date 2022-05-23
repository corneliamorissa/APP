package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Group;
import com.example.notesapp.appObjects.Notify;
import com.example.notesapp.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainPageActivity extends AppCompatActivity {
    private String user_name;
    private int user_id;
    RecyclerView notifview;
    private ArrayList<Notify> joins;
    private static final String USER_URL = "https://studev.groept.be/api/a21pt103/user_from_id/";
    private static final String JOIN_URL = "https://studev.groept.be/api/a21pt103/grab_join_request/";
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button btn_group = (Button)findViewById(R.id.button9);
        Button btn_docs = (Button) findViewById(R.id.button5);
        Button btn_upload = (Button) findViewById(R.id.button);
        ImageButton btn_nav = (ImageButton) findViewById(R.id.button2);
        ImageButton btn_settings = (ImageButton) findViewById(R.id.button_settings);
        RecyclerView notifview = findViewById(R.id.notif_view) ;

        joins = new ArrayList<Notify>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        notifview.setLayoutManager(layoutManager);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            //The key argument here must match that used in the other activity
        }


/*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainerView, HomeFragment.class, null)
                    .commit();
        }*/


        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent(MainPageActivity.this, myGroups.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", user_id);
                startActivity(intent);
                finish();

            }
        });

        btn_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPageActivity.this, UserDocument.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", user_id);
                startActivity(intent);
                finish();

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPageActivity.this, UploadActivity.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", user_name);
                startActivity(intent);
                finish();

            }
        });

        btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPageActivity.this, NaviagtionPage.class);
                intent.putExtra("user id", user_id);
                intent.putExtra("user name", user_name);
                startActivity(intent);
                finish();

            }
        });

       btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPageActivity.this, SettingsActivity.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", user_id);
                startActivity(intent);
                finish();

            }
        });


       //put the notifications


    }

    //notification
    private void populateNotifList()
    {
        String url = JOIN_URL + user_id;
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
                        Notify n = new Notify(o.getInt("request_id"), o.getInt("group_id"), o.getInt("admin_id"));
                        joins.add(n);
                        System.out.println("test2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPageActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(queueRequest);
        requestQueue = Volley.newRequestQueue(this);
    }
    public void getUserName(Notify n)
    {
        String u_name;
        String url = USER_URL + n.getUser_id();
        System.out.println(url);

        System.out.println("test");

        StringRequest queueRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                n.setUser_name(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPageActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(queueRequest);
        requestQueue = Volley.newRequestQueue(this);
    }




}