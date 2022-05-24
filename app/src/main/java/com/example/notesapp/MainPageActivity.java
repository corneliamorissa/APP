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
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private static final String JOINS_URL = "https://studev.groept.be/api/a21pt103/grab_join_request/";
    private static final String JOIN_URL = "https://studev.groept.be/api/a21pt103/join_group/";
    private static final String DELETE_URL ="https://studev.groept.be/api/a21pt103/delete_request/";
    private RequestQueue requestQueue;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button btn_group = (Button)findViewById(R.id.button9);
        Button btn_docs = (Button) findViewById(R.id.button5);
        Button btn_upload = (Button) findViewById(R.id.button);
        ImageButton btn_nav = (ImageButton) findViewById(R.id.button2);
        ImageButton btn_settings = (ImageButton) findViewById(R.id.button_settings);
        layout = findViewById(R.id.notifycontainer) ;

        joins = new ArrayList<Notify>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        notifview.setLayoutManager(layoutManager);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            //The key argument here must match that used in the other activity
        }


        //goes to my groups
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
        //docs button goes to the buttons the user has uploaded
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
        //the + button allows to upload something
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
        //goes to the navigation panel
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
        //goes to settings
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

        populateNotifList();
    }

    //notification
    private void populateNotifList()
    {
        String url = JOINS_URL + user_id;
        System.out.println(url);

        System.out.println("test");
        JsonArrayRequest queueRequest;

        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                layout = findViewById(R.id.notifycontainer);
                String info = "";
                for (int i = 0; i < response.length(); ++i) {
                    System.out.println("test1");
                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(i);
                        Notify n = new Notify(o.getInt("j.request_id"), o.getInt("j.group_id"), o.getInt("j.admin_id"));
                        joins.add(n);
                        System.out.println("test2");
                        final View view = getLayoutInflater().inflate(R.layout.notification_row, null);
                        TextView nameView = view.findViewById(R.id.requesttext);
                        Button viewProfile = view.findViewById(R.id.viewprofile);
                        Button accept = view.findViewById(R.id.accept);
                        Button reject= view.findViewById(R.id.reject);

                        nameView.setText( o.getString("m.username") + "is requesting to join your group " + o.getString("g.group_name"));

                        System.out.println(n.getRequest_id());
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                join(user_id,n.getGroup_id(),n.getRequest_id());
                            }
                        });
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteRequest(n.getRequest_id());
                                Toast.makeText(MainPageActivity.this, "User rejected", Toast.LENGTH_LONG).show();


                            }
                        });


                        layout.addView(view);


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

    public void join(int u_id,int g_id,int r_id)
    {

        String url2 = JOIN_URL+ g_id + "/" + u_id ;
        System.out.println(url2);


        System.out.println("test");

        StringRequest queueRequest2;

        queueRequest2 = new StringRequest(Request.Method.POST,url2,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainPageActivity.this, "User accepted", Toast.LENGTH_LONG).show();
                deleteRequest(r_id);

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPageActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });


    }
    public void deleteRequest(int r_id)
    {
        String url2 = DELETE_URL+ r_id;
        System.out.println(url2);


        System.out.println("test");

        StringRequest queueRequest2;

        queueRequest2 = new StringRequest(Request.Method.POST,url2,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {


            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPageActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });


    }




}