package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.notesapp.appObjects.Group;
import com.example.notesapp.userInfo.UserInfo;
import com.example.notesapp.userInfo.UserLog;


public class LoginActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private static final String LOGIN_URL = "https://studev.groept.be/api/a21pt103/login/";
    private static final String PASS_URL = "https://studev.groept.be/api/a21pt103/password_check";
    private EditText userName, password;
    private Button btn_log_in;
    private String euserName;
    private String epassword;
    private UserInfo user;
    private int user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
        Button btn = (Button) findViewById(R.id.btn_register1);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_log_in = (Button) findViewById(R.id.btn_login);
        getSupportActionBar().setTitle("Log In");


    }

    public void onBtnRegister_Clicked(View caller) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    public void onBtnLogIn_Clicked(View caller) {
        euserName = userName.getText().toString();
        epassword = password.getText().toString();

        if (TextUtils.isEmpty(euserName) && TextUtils.isEmpty(epassword)) {
            Toast.makeText(LoginActivity.this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
        }
        else {
            String pass = LOGIN_URL +  euserName;

            System.out.println(pass);

            JSONObject p = new JSONObject();

            System.out.println("0");

            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                    pass,
                    null,

                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            String p = "";
                            String u = "";
                          System.out.println("1");
                            if(response.length()==0)
                            {

                                Toast.makeText(LoginActivity.this, "No such account exists", Toast.LENGTH_LONG).show();
                            }
                            else {
                                for (int i = 0; i < response.length(); ++i) {
                                    JSONObject o = null;
                                    System.out.println("2");

                                    try {
                                        o = response.getJSONObject(i);
                                        p = o.get("pass").toString();
                                        u = o.get("user_name").toString();
                                        if (p.equals(epassword)) {
                                            int id =  o.getInt("user_id");
                                            String f_name =  o.getString("first_name");
                                            String l_name =  o.getString("last_name");
                                            String email =  o.getString("email");
                                            UserLog user = new UserLog(euserName,epassword,f_name,l_name,email);
                                            user.setEmail(email);
                                            user.setFirstname(f_name);
                                            user.setPassword(epassword);
                                            user.setUserName(euserName);
                                            user.setLastname(l_name);

                                            /***UserInfo user1 = new UserInfo();
                                            user1.setPass(epassword);
                                            user1.setUser(euserName);
                                            user1.setFirst_name(f_name);
                                            user1.setEmail(email);
                                            user1.setId(id);***/

                                            //System.out.println(email +","+ f_name +","+ l_name+ ","+ euserName+"," + epassword);
                                            //System.out.println(user.getEmail() +","+ user.getFirstName() +","+ user.getLastname()+ ","+ user.getUserName()+"," + user.getPassword());

                                            //startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                                            Intent intent = new Intent(LoginActivity.this, UserDocument.class);
                                            intent.putExtra("user id", id );
                                            System.out.println(id);
                                            intent.putExtra("user name",euserName);
                                            intent.putExtra("User Info", (Parcelable) user);
                                            startActivity(intent);
                                            Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_LONG).show();


                                        } else {

                                            Toast.makeText(LoginActivity.this, "Wrong Password or username", Toast.LENGTH_LONG).show();

                                        }


                                        System.out.println(p);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }


                        }
                    },
                    error -> Toast.makeText(LoginActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

            requestQueue.add(queueRequest);
        }
    }

    /* public void loginUser()
    {
        String login = LOGIN_URL + "/" +  euserName + "/" + epassword;
        JSONObject p = new JSONObject();

        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                login,
                null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject o = null;
                            try{
                            o = response.getJSONObject(i);

                            int id = Integer.parseInt((String) o.get("user_id"));
                            String f_name = (String) o.get("first_name");
                            String l_name =  (String) o.get("last_name");
                            String email =  (String) o.get("email");
                            user = new UserInfo(id,f_name,l_name,epassword,euserName,email);

                            startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                            Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_LONG).show();

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                },
                error -> Toast.makeText(LoginActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);

    }
*/
   /* private void loginUser() {


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
                                p = o.get("pass").toString();

                                if(p.equals(epassword))
                                {
                                    setContentView(R.layout.activity_main_page);
                                    Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_LONG).show();
                                }

                                else
                                {

                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                                    setContentView(R.layout.activity_login);

                                }


                                System.out.println(p);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(LoginActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

                requestQueue.add(queueRequest);


/*
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
                }*/




}

