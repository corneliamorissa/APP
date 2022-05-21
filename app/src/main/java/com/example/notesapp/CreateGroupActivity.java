package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateGroupActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String ADDGROUP_URL = "https://studev.groept.be/api/a21pt103/add_Group/";
    private static final String ADDADMIN_URL =  "https://studev.groept.be/api/a21pt103/";
    int id;
    private EditText group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("admin_id");
            //The key argument here must match that used in the other activity
        }

        group_name = findViewById(R.id.group_name);
        requestQueue = Volley.newRequestQueue(this);
        Button button = findViewById(R.id.createGroup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = ADDGROUP_URL + group_name.getText().toString()  + id ;
                System.out.println(url);


                System.out.println("test");
                System.out.println(group_name.getText().toString());

                StringRequest queueRequest;

                queueRequest = new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateGroupActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
                    }
                });

                requestQueue.add(queueRequest);

                String url2 = ADDADMIN_URL + id ;
                System.out.println(url2);


                System.out.println("test");
                System.out.println(group_name.getText().toString());

                StringRequest queueRequest2;

                queueRequest2 = new StringRequest(Request.Method.POST,url2,new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CreateGroupActivity.this, "Group added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateGroupActivity.this, Group_main_page.class);
                        intent.putExtra("name", group_name.getText().toString());
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateGroupActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
                    }
                });

                requestQueue.add(queueRequest);



            }
        });




        }


    }


