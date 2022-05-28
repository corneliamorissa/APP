package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class SettingsActivity extends AppCompatActivity {

    Button changePass, logOut, delete;
    private final String CHANGE_PASS = "https://studev.groept.be/api/a21pt103/changePassword/";
    private final String DELETE_ACC = "https://studev.groept.be/api/a21pt103/deleteAccount/";
    String user_name,email;
    Integer user_id;
    private RequestQueue requestQueue;
    AlertDialog dialog, dialog2;
    LinearLayout layout;
    boolean isMainPage = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        changePass = (Button) findViewById(R.id.change_pass);
        logOut = (Button) findViewById(R.id.log_out);
        delete = (Button) findViewById(R.id.delete_user);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            email = extras.getString("email");
            isMainPage = extras.getBoolean("main page");
            //The key argument here must match that used in the other activity
        }

        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("Settings");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        buildDialogChangePass();
        dialogDelAcc();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(isMainPage) {
                    Intent intent = new Intent(SettingsActivity.this, MainPageActivity.class);
                    intent.putExtra("user id", user_id);
                    intent.putExtra("user name", user_name);
                    startActivity(intent);
                    this.finish();
                    return true;
                }
                else
                {
                    Intent intent = new Intent(SettingsActivity.this, NaviagtionPage.class);
                    intent.putExtra("user id", user_id);
                    intent.putExtra("user name", user_name);
                    startActivity(intent);
                    this.finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void changePasswordBtn_Click (View view)
    {
        dialog.show();
    }


    public void buildDialogChangePass()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_change_pass, null);

        final EditText newPass = view.findViewById(R.id.pass_edit);
        requestQueue = Volley.newRequestQueue(this);

        builder.setTitle("Change Your Password")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String change = CHANGE_PASS + newPass.getText() + "/" + user_id;
                        System.out.println("try to change pass :"+ change);

                        JsonArrayRequest queueRequest;
                        queueRequest = new JsonArrayRequest(Request.Method.GET, change, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {



                                Toast.makeText(SettingsActivity.this,"Password Changed", Toast.LENGTH_LONG).show();

                            }

                        }
                                ,
                                error -> Toast.makeText(SettingsActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

                        );
                        requestQueue.add(queueRequest);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setView(view);

        dialog = builder.create();
    }


    public void LogOutBtn_Click (View view)
    {
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void DeleteAccBtn (View view)
    {
        dialog2.show();
    }

    public void dialogDelAcc()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

        requestQueue = Volley.newRequestQueue(this);

        builder.setTitle("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String delete = DELETE_ACC + user_id +"/"+ user_name;
                        System.out.println(delete);
                        JsonArrayRequest queueRequest;
                        queueRequest = new JsonArrayRequest(Request.Method.GET, delete, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(SettingsActivity.this,"Account Deleted", Toast.LENGTH_LONG).show();


                            }

                        }
                                ,
                                error -> Toast.makeText(SettingsActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

                        );
                        requestQueue.add(queueRequest);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        dialog2 = builder.create();
    }

    }


