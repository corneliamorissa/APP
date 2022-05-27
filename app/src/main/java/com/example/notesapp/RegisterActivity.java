package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.userInfo.UserLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private TextView txtInfo;
    private Button bRegister;
    private EditText lastName, firstName,userName, password, email;
    private ArrayList<String> names;

   private static final String Register_URL  = "https://studev.groept.be/api/a21pt103/registration/";
    private static final String test_URL  = "ttps://studev.groept.be/api/a21pt103/email";
    private static final String MEMBERS_URL  = "https://studev.groept.be/api/a21pt103/get_user_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        names = new ArrayList<String>();
        requestQueue = Volley.newRequestQueue(this);
        lastName = (EditText) findViewById(R.id.name);
        firstName = (EditText) findViewById(R.id.firstname);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        Button bRegister = findViewById(R.id.btn_register);


       ActionBar actionBar = getSupportActionBar();

       // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);







       members();


   }


    public void onBtnReg_click(View caller) {
        String elastName = lastName.getText().toString();
        String efirstName = firstName.getText().toString();
        String eEmail = email.getText().toString();
        String ePass = password.getText().toString();
        String eUserName = userName.getText().toString();
        if (names.contains(eUserName)) {
            Toast.makeText(RegisterActivity.this, "Username already exists ", Toast.LENGTH_LONG).show();

        }
        else {
            UserLog user = new UserLog(eUserName, ePass, efirstName, elastName, eEmail);
            String requestURL = Register_URL + lastName.getText() + "/" + firstName.getText() + "/" + userName.getText() + "/" + password.getText() + "/" + email.getText();
            System.out.println(requestURL);
            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                    requestURL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            String p = "";
                            String info = "";
                            for (int i = 0; i < response.length(); ++i) {
                                JSONObject o = null;
                                try {
                                    o = response.getJSONObject(i);
                                    info += "all gooddddd";
                                    p = o.get("password").toString();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            startActivity(new Intent(RegisterActivity.this, MainPageActivity.class));
                            getIntent().putExtra("User Info", user);
                            Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();

                        }
                    },
                    error -> Toast.makeText(RegisterActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

            requestQueue.add(queueRequest);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void members()
    {
        String url = MEMBERS_URL;


        System.out.println(url);
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest;
        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                for (int i = 0; i < response.length(); ++i) {

                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(i);

                        names.add(o.getString("user_name"));

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }}},
                error -> Toast.makeText(RegisterActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);


    }
}
