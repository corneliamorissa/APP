package com.example.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Group_main_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String groupName, userName, selectedAdmin;
    private static final String DELETE = "https://studev.groept.be/api/a21pt103/delete_group/";
    private static final String LEAVE = "https://studev.groept.be/api/a21pt103/leave_group/";
    private static final String SPINNER = "https://studev.groept.be/api/a21pt103/geab_all_memebrs_of_a_group/";
    private static final String ADMIN_CHECK ="https://studev.groept.be/api/a21pt103/get_admin_id/";
    private static final String UPDATE_ADMIN ="https://studev.groept.be/api/a21pt103/make_another_admin/";
    private static final String MEMBER_CHECK ="https://studev.groept.be/api/a21pt103/check_if_member/";
    private static final String getId = "https://studev.groept.be/api/a21pt103/getId/";
    Integer admin;
    boolean isAdmin = false;
    boolean isMember = false;
    private RequestQueue requestQueue;
    int groupid, userid;
    TextView name_show;
    AlertDialog dialog;
    ArrayAdapter<String> adminAdapter;
    ArrayList<Integer> adminIdList = new ArrayList<>();
    ArrayList<String> adminUsernameList = new ArrayList<>();
    ArrayList<Integer> mems;
    ArrayList<String> mems_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main_page);
        name_show = findViewById(R.id.groupName);
        Button delete = findViewById(R.id.delete_group);
        Button leave = findViewById(R.id.leave_group);
        Bundle extras = getIntent().getExtras();
        mems = new ArrayList<>();
        mems_name = new ArrayList<>();

        if (extras != null) {
            groupName = extras.getString("group name").toUpperCase(Locale.ROOT);
            groupid = extras.getInt("group id");
            userid = extras.getInt("user id");
            userName = extras.getString("user name");
            //The key argument here must match that used in the other activity
        }

        name_show.setText(groupName);
        members();
        checkIfAdmin();
        buildDialog();

        //if admin then they can delete group and when leave group must appoint new admin
        if(isAdmin && isMember)
        {
            delete.setVisibility(View.VISIBLE);
            leave.setVisibility(View.VISIBLE);
            delete.setOnClickListener( new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                    deleteGroup();

                }});

        }
        //otherwise they can just leave group
        else if(isMember){

            delete.setVisibility(View.INVISIBLE);
            leave.setVisibility(View.VISIBLE);



        }
        else{
            delete.setVisibility(View.INVISIBLE);
            leave.setVisibility(View.INVISIBLE);
        }

    }

    public void onTopic_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                .putExtra("name",groupName)
                .putExtra("id",groupid));
        finish();
    }
    public void onMemebrs_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                .putExtra("name",groupName)
                .putExtra("id",groupid));
        finish();
    }
    //checks if it is admin
    public void checkIfAdmin()
    {
        String url = ADMIN_CHECK + groupid;
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
                        o = response.getJSONObject(0);

                        admin = o.getInt("admin_id");
                        System.out.println(admin);
                        if(userid == admin)
                        {
                            isAdmin = true;
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }}},
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);


    }

    public void deleteGroup(){
            String delete = DELETE + groupid +  "/" + groupid +  "/" + groupid;
            System.out.println(delete);
            requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest queueRequest;
            queueRequest = new JsonArrayRequest(Request.Method.POST, delete, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    String info = "";
                    for (int i = 0; i < response.length(); ++i) {

                        JSONObject o = null;
                        try {
                            o = response.getJSONObject(i);
                            info += "all gooddddd";

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                    Intent intent = new Intent(Group_main_page.this, myGroups.class);
                    startActivity(intent);
                    Toast.makeText(Group_main_page.this,"Delete request is executed", Toast.LENGTH_LONG).show();
                }

            },
                    error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

            );
            requestQueue.add(queueRequest);
        }


    public void onLeave_Clicked(View caller)
    {
        if(isAdmin)
        {
            dialog.show();
        }

        else{
            String leave = LEAVE + userid + "/" + groupid;
            System.out.println(leave);
            requestQueue = Volley.newRequestQueue(Group_main_page.this);
            JsonArrayRequest queueRequest;
            queueRequest = new JsonArrayRequest(Request.Method.POST, leave, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    String info = "";
                    for (int i = 0; i < response.length(); ++i) {

                        JSONObject o = null;
                        try {
                            o = response.getJSONObject(i);
                            info += "all gooddddd";

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                    Intent intent = new Intent(Group_main_page.this, myGroups.class);
                    startActivity(intent);
                    Toast.makeText(Group_main_page.this,"Leave request is executed", Toast.LENGTH_LONG).show();
                }

            },
                    error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

            );
            requestQueue.add(queueRequest);
        }
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        requestQueue = Volley.newRequestQueue(this);
        Spinner spinner = (Spinner) findViewById(R.id.choose_admin_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mems_name);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        /*String url = SPINNER + groupid;
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
                                adminAdapter = new ArrayAdapter<String>(Group_main_page.this,
                                        android.R.layout.simple_spinner_item, adminUsernameList);
                                adminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sadmin.setAdapter(adminAdapter);
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
        requestQueue.add(queueRequest);

        requestQueue = Volley.newRequestQueue(this); */

        builder.setView(view);
        builder.setTitle("Choose new admin")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

        dialog = builder.create();
    }

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
    public void adminLeave()
    {
        requestQueue = Volley.newRequestQueue(this);

        String leave = LEAVE + userid + "/" + groupid;
        System.out.println(leave);
        requestQueue = Volley.newRequestQueue(Group_main_page.this);
        JsonArrayRequest queueRequest1;
        queueRequest1 = new JsonArrayRequest(Request.Method.POST, leave, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                for (int i = 0; i < response.length(); ++i) {

                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(i);
                        info += "all gooddddd";

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                Intent intent = new Intent(Group_main_page.this, myGroups.class);
                startActivity(intent);
                Toast.makeText(Group_main_page.this,"Leave request is executed", Toast.LENGTH_LONG).show();
            }

        },
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest1);
    }
    public void changeAdmin(String newAdmin)
    {   int index_name  = mems_name.indexOf(newAdmin);
        int id = mems.get(index_name);
        /***to implement update admin***/
        String change = UPDATE_ADMIN + id + "/" + groupid;
        System.out.println(change);
        requestQueue = Volley.newRequestQueue(Group_main_page.this);
        JsonArrayRequest queueRequest2;
        queueRequest2 = new JsonArrayRequest(Request.Method.POST, change, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                for (int i = 0; i < response.length(); ++i) {

                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(i);
                        info += "all gooddddd";
                        Toast.makeText(Group_main_page.this,"pkkkkk", Toast.LENGTH_LONG).show();
                        adminLeave();

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                Toast.makeText(Group_main_page.this,"Change admin request is executed", Toast.LENGTH_LONG).show();
            }

        },
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest2);
    }

    public void members()
    {
        String url = MEMBER_CHECK + groupid;


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

                        mems.add(o.getInt("user_id"));
                        System.out.println(o.getInt("user_id"));
                        mems_name.add(o.getString("user_name"));

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }}},
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
        for (Integer m: mems)
        {
            if (m == userid) {
                isMember = true;
                break;
            }
        }

    }
    public void getId()
    {
        String url = MEMBER_CHECK + groupid;


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

                        mems.add(o.getInt("user_id"));
                        System.out.println(o.getInt("user_id"));
                        mems_name.add(o.getString("user_name"));

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }}},
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
        for (Integer m: mems)
        {
            if (m == userid) {
                isMember = true;
                break;
            }
        }

    }
}