package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private TextView txtInfo;
    private Button bRegister;

   private static final String Register_URL  = "https://studev.groept.be/api/a21pt103/registration/";
    private static final String test_URL  = "ttps://studev.groept.be/api/a21pt103/email";
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        requestQueue = Volley.newRequestQueue(this);
        EditText elastName = (EditText) findViewById(R.id.name);
        EditText efirstName = (EditText) findViewById(R.id.firstname);
        EditText euserName = (EditText) findViewById(R.id.username);
        EditText epassword = (EditText) findViewById(R.id.password);
        EditText eEmail = (EditText) findViewById(R.id.email);
        Button bRegister = findViewById(R.id.btn_register);

       getSupportActionBar().setTitle("Register");

    }

    public void onBtnReg_click(View caller){
        EditText elastName = (EditText) findViewById(R.id.name);
        EditText efirstName = (EditText) findViewById(R.id.firstname);
        EditText euserName = (EditText) findViewById(R.id.username);
        EditText epassword = (EditText) findViewById(R.id.password);
        EditText eEmail = (EditText) findViewById(R.id.email);


        String requestURL = Register_URL + "/" + elastName.getText() + "/" + efirstName.getText() + "/" + euserName.getText() + "/"  + epassword.getText() + "/"  + eEmail.getText();
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
                        setContentView(R.layout.activity_main_page);
                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();

                    }
                },
                error -> Toast.makeText(RegisterActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);
    }
}
