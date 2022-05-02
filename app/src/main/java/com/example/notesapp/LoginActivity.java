package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.*;

import com.android.volley.toolbox.Volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private static final String LOGIN_URL  = "https://studev.groept.be/api/a21pt103/login/";
    private static final String PASS_URL  = "https://studev.groept.be/api/a21pt103/getpassword/";
    private EditText userName,password;
    private Button btn_log_in;
    private String euserName;
    private String epassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
        Button btn = (Button)findViewById(R.id.btn_register1);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_log_in = (Button)findViewById(R.id.btn_login);

    }

    public void onBtnRegister_Clicked(View caller)
    {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    public void onBtnLogIn_Clicked(View caller)
    {
         euserName = userName.getText().toString();
         epassword = password.getText().toString();

        if (TextUtils.isEmpty(euserName) && TextUtils.isEmpty(epassword)) {
            Toast.makeText(LoginActivity.this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
        }

        loginUser(euserName, epassword);
    }

    private void loginUser(String userName, String password) {


                String checkpass;
                String pass = PASS_URL + "/" + euserName;
                System.out.println(pass);
                JSONObject p = new JSONObject();
        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                pass,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String p = "";
                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject o = null;
                            try {
                                o = response.getJSONObject(i);
                                p = (String) o.get("pass");

                                System.out.println(p);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        setContentView(R.layout.activity_main_page);
                        Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginActivity.this, MainPageActivity.class);
                        startActivity(i);
                    }
                },
                error -> Toast.makeText(LoginActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);


                if(password.equals(pass)) {
                    String requestURL = LOGIN_URL + "/" + euserName + "/" + epassword;
                    JsonArrayRequest log = new JsonArrayRequest(Request.Method.GET,
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
                                    setContentView(R.layout.activity_main_page);
                                    Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(LoginActivity.this, MainPageActivity.class);
                                    startActivity(i);
                                }
                            },
                            error -> Toast.makeText(LoginActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

                    requestQueue.add(log);


                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                }
            }

    }

