package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    Button changePass, logOut, delete;
    private final String CHANGE_PASS = "https://studev.groept.be/api/a21pt103/changePassword/";
    private final String DELETE_ACC = "https://studev.groept.be/api/a21pt103/deleteAccount/";
    private static final String LEAVE = "https://studev.groept.be/api/a21pt103/leave_group/";
    private static final String SPINNER = "https://studev.groept.be/api/a21pt103/geab_all_memebrs_of_a_group/";
    private static final String ADMIN_CHECK ="https://studev.groept.be/api/a21pt103/get_admin_id/";
    private static final String UPDATE_ADMIN ="https://studev.groept.be/api/a21pt103/make_another_admin/";
    private static final String GROUP_URL = "https://studev.groept.be/api/a21pt103/grab_Groups";
    String user_name,email;
    Integer user_id, group_id;
    private RequestQueue requestQueue;
    AlertDialog dialog, dialog2;
    LinearLayout layout;
    boolean isMainPage = false;
    AlertDialog dialog1;
    ArrayList<Integer> adminIdList = new ArrayList<>();
    ArrayList<String> adminUsernameList = new ArrayList<>();
    ArrayAdapter<String> adminAdapter;
    String selectedAdmin;
    private ArrayList<Group> groups;
    Integer admin, groupid;




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
        groups = new ArrayList<Group>();
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


    private void buildDialog1( int groupid , String groupName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        View mview = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setTitle("Choose new Admin for " + groupName);
        Spinner spinner = (Spinner) mview.findViewById(R.id.choose_admin_spinner);


        requestQueue = Volley.newRequestQueue(this);

        String url = SPINNER + groupid;
        System.out.println(url);
        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject o = null;
                                o = response.getJSONObject(i);
                                String groupName = o.get("user_name").toString();
                                Integer groupId = o.getInt("user_id");
                                adminUsernameList.add(groupName);
                                adminIdList.add(groupId);
                                //groupIdList.add(group_id);
                                adminAdapter = new ArrayAdapter<String>(SettingsActivity.this,
                                        android.R.layout.simple_spinner_item, adminUsernameList);
                                adminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adminAdapter);
                                System.out.println("test thursday 3");

                                adminAdapter.notifyDataSetChanged();

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        spinner.setOnItemSelectedListener(new SettingsActivity.OnSpinnerItemClicked());
        requestQueue.add(queueRequest);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeAdmin(String.valueOf(spinner.getSelectedItem()));

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setView(mview);
        dialog1 = builder.create();
        dialog1.show();


    }
    public class OnSpinnerItemClicked implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getId() == R.id.choose_admin_spinner) {
                selectedAdmin = adapterView.getSelectedItem().toString();
                for (int j = 0; j < adminUsernameList.size(); j++) {
                    if (adminUsernameList.get(j).equals(selectedAdmin)) {
                        admin = adminIdList.get(j);
                    }
                }
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    public void changeAdmin(String newAdmin)
    {
        for(int j = 0; j<adminUsernameList.size();j++)
        {
            if(adminUsernameList.get(j).equals(newAdmin))
            {
                admin = adminIdList.get(j);
            }
        }

        /***to implement update admin***/
        String change = UPDATE_ADMIN + admin + "/" + groupid;
        System.out.println(change);
        requestQueue = Volley.newRequestQueue(SettingsActivity.this);
        StringRequest queueRequest2;
        queueRequest2 = new StringRequest(Request.Method.GET, change,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(SettingsActivity.this,"Change admin request is executed", Toast.LENGTH_LONG).show();
                //leave();
            }

        },
                error -> Toast.makeText(SettingsActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest2);

    }
    public void leave(int groupid)
    {
        String leave = LEAVE + user_id + "/" + groupid;
        System.out.println(leave);
        requestQueue = Volley.newRequestQueue(SettingsActivity.this);
        StringRequest queueRequest;
        queueRequest = new StringRequest(Request.Method.GET ,leave, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(SettingsActivity.this,"Leave request is executed", Toast.LENGTH_LONG).show();
            }},
                error -> Toast.makeText(SettingsActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
    }

    public void addButtons()
    {
        String url = GROUP_URL;
        System.out.println(url);
        requestQueue = Volley.newRequestQueue(this);
        System.out.println("test");
        JsonArrayRequest queueRequest;

        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); ++i) {
                    System.out.println("test1");
                    JSONObject o = null;

                    try {
                        o = response.getJSONObject(i);
                        final Group g = new Group(o.getInt("group_id"), o.getString("group_name"), o.getInt("admin_id"), o.getString("add_date"));
                        System.out.println(g.getName());
                        System.out.println(g.getId());
                        groups.add(g);



                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                for (Group m : groups) {
                    System.out.println(m.getName());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });


        requestQueue.add(queueRequest);
    }


}


