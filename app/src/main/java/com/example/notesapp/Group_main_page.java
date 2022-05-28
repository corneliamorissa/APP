package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Group_main_page extends AppCompatActivity  {
    String groupName, userName, selectedAdmin;
    private static final String DELETE = "https://studev.groept.be/api/a21pt103/delete_group/";
    private static final String LEAVE = "https://studev.groept.be/api/a21pt103/leave_group/";
    private static final String SPINNER = "https://studev.groept.be/api/a21pt103/geab_all_memebrs_of_a_group/";
    private static final String ADMIN_CHECK ="https://studev.groept.be/api/a21pt103/get_admin_id/";
    private static final String UPDATE_ADMIN ="https://studev.groept.be/api/a21pt103/make_another_admin/";
    private static final String MEMBER_CHECK ="https://studev.groept.be/api/a21pt103/check_if_user_member/";
    private static final String getId = "https://studev.groept.be/api/a21pt103/getId/";
    private static final String IS_A_MEMBER = "https://studev.groept.be/api/a21pt103/isMember/";
    private static final String REQUESTJOIN_URL = "https://studev.groept.be/api/a21pt103/send_join_request/";
    private static final String CHECK_URL = "https://studev.groept.be/api/a21pt103/check_if_request_sent/";
    Integer admin, member;
    boolean isAdmin;
    boolean isMember;
    private RequestQueue requestQueue;
    int groupid, userid;
    TextView name_show;
    AlertDialog dialog1;
    AlertDialog dialog2 ;
    ArrayAdapter<String> adminAdapter;
    ArrayList<Integer> adminIdList = new ArrayList<>();
    ArrayList<String> adminUsernameList = new ArrayList<>();
    ArrayList<Integer> mems;
    ArrayList<String> mems_name;
    FloatingActionButton settings;
    int test;
    Button join_btn;
    Button topic;
    boolean myGroups;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main_page);
        name_show = findViewById(R.id.groupName);


        //Button delete = findViewById(R.id.delete_group);
        //Button leave = findViewById(R.id.leave_group);

        Button delete2 = findViewById(R.id.delete_group2);
        Button leave2 = findViewById(R.id.leave_group2);
        join_btn = findViewById(R.id.join_button);
        settings = findViewById(R.id.settings_group);
        topic = findViewById(R.id.topics);
        Bundle extras = getIntent().getExtras();
        test = 0;
        mems = new ArrayList<>();
        mems_name = new ArrayList<>();

        if (extras != null) {
            groupName = extras.getString("group name").toUpperCase(Locale.ROOT);
            groupid = extras.getInt("group id");
            userid = extras.getInt("user id");
            userName = extras.getString("user name");
            myGroups = extras.getBoolean(" my groups");

        }
        name_show.setText(groupName);
        join_btn.setVisibility(View.INVISIBLE);

        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("Topic Notes");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        getAdminId();



/*

        name_show.setText(groupName);
        String url = ADMIN_CHECK + groupid;
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest;
        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); ++i) {

                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(0);

                        admin = o.getInt("admin_id");
                        System.out.println(admin);
                        if(userid == admin)
                        {
                            isAdmin = true;
                            //if admin then they can delete group and when leave group must appoint new admin

                            delete.setVisibility(View.VISIBLE);
                            delete.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteGroup();

                                    }});

                            leave.setVisibility(View.VISIBLE);



                        }
                        else
                        {
                            String url1 = IS_A_MEMBER + userid + "/" + groupid;
                            System.out.println(url1);
                            requestQueue = Volley.newRequestQueue(Group_main_page.this);
                            JsonArrayRequest queueRequest1;
                            queueRequest1 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    String info = "";
                                    if(response.length()==0)
                                    {
                                        leave.setVisibility(View.INVISIBLE);
                                        delete.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        for (int i = 0; i < response.length(); ++i) {

                                            JSONObject o = null;
                                            try {
                                                o = response.getJSONObject(i);

                                                member = o.getInt("user_id");
                                                if(userid == member)
                                                {
                                                    leave.setVisibility(View.VISIBLE);
                                                    delete.setVisibility(View.INVISIBLE);
                                                }

                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }

                                        }}}},
                                    error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

                            );
                            requestQueue.add(queueRequest1);
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


        ActionBar actionBar = getSupportActionBar();


        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_view_headline_24);
        actionBar.setTitle("Group Home Page");


        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);


    }
    */
    }
    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(myGroups)
        {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(Group_main_page.this, myGroups.class);
                    intent.putExtra("user id", userid);
                    intent.putExtra("user name", userName);
                    intent.putExtra("group id", groupid);
                    intent.putExtra("group name", groupName);
                    startActivity(intent);
                    this.finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
        else {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(Group_main_page.this, GroupList.class);
                    intent.putExtra("user id", userid);
                    intent.putExtra("user name", userName);
                    intent.putExtra("group id", groupid);
                    intent.putExtra("group name", groupName);
                    startActivity(intent);
                    this.finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    public void onTopic_Clicked(View caller) {
        String url1 = MEMBER_CHECK + userid + "/" + groupid;
        System.out.println(url1);
        requestQueue = Volley.newRequestQueue(Group_main_page.this);
        JsonArrayRequest queueRequest1;
        queueRequest1 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                if(response.length()==0)
                {
                    Toast.makeText(Group_main_page.this, "Join the Group First", Toast.LENGTH_LONG).show();
                }
                else{
                    for (int i = 0; i < response.length(); ++i) {

                        JSONObject o = null;
                        try {
                            o = response.getJSONObject(i);

                            member = o.getInt("user_id");
                            if(userid == member)
                            {
                                startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                                        .putExtra("group name",groupName)
                                        .putExtra("group id",groupid)
                                        .putExtra("user id",userid)
                                        .putExtra("user name", userName));
                                finish();
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }}}},
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest1);




    }
    public void onMemebrs_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, MemberList.class)
                .putExtra("user id",userid)
                .putExtra("group id",groupid));
        finish();
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
                    intent.putExtra("user id", userid );
                    intent.putExtra("user name", userName);
                    intent.putExtra("group id", groupid);
                    intent.putExtra("group name", groupName);
                    startActivity(intent);
                    Toast.makeText(Group_main_page.this,"Delete request is executed", Toast.LENGTH_LONG).show();
                }

            },
                    error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

            );
            requestQueue.add(queueRequest);
        }


    public void onLeave_Clicked(View v)
    {

        if(isAdmin)
        {
            buildDialog1();
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
                    intent.putExtra("user id", userid );
                    intent.putExtra("user name", userName);
                    intent.putExtra("group id", groupid);
                    intent.putExtra("group name", groupName);
                    startActivity(intent);
                    Toast.makeText(Group_main_page.this,"Leave request is executed", Toast.LENGTH_LONG).show();
                }

            },
                    error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

            );
            requestQueue.add(queueRequest);
        }
    }

    private void buildDialog1( ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Group_main_page.this);
        View mview = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setTitle("Choose new Admin : ");
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
                                adminAdapter = new ArrayAdapter<String>(Group_main_page.this,
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

        spinner.setOnItemSelectedListener(new OnSpinnerItemClicked());
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
       // dialog1.show();


    }
    private void buildDialog2(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Group_main_page.this);
        View mview = getLayoutInflater().inflate(R.layout.settings, null);
        Button leave2 = mview.findViewById(R.id.leave_group2);
        Button delete2 = mview.findViewById(R.id.delete_group2);
        Button photo = mview.findViewById(R.id.change_photo);
        delete2.setVisibility(View.INVISIBLE);
        leave2.setVisibility(View.INVISIBLE);
        photo.setVisibility(View.INVISIBLE);

        if(i == 0)
        {
            delete2.setVisibility(View.VISIBLE);
            System.out.println("deleteeeeee");
            delete2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteGroup();
                }
            });
            leave2.setVisibility(View.VISIBLE);
            leave2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buildDialog1();
                }
            });
            photo.setVisibility(View.VISIBLE);
        }
        else
        {
            leave2.setVisibility(View.VISIBLE);
            leave2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leave();
                }
            });
        }


        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setView(mview);
        dialog2 = builder.create();
        dialog2.show();


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


    public void adminLeave()
    {
        requestQueue = Volley.newRequestQueue(this);

        String leave = LEAVE + userid + "/" + groupid;
        System.out.println(leave);
        requestQueue = Volley.newRequestQueue(Group_main_page.this);
        StringRequest queueRequest1;
        queueRequest1 = new StringRequest(Request.Method.GET, leave, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(Group_main_page.this, myGroups.class);
                intent.putExtra("user id", userid );
                intent.putExtra("user name", userName);
                intent.putExtra("group id", groupid);
                intent.putExtra("group name", groupName);
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
        StringRequest queueRequest2;
        queueRequest2 = new StringRequest(Request.Method.GET, change,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(Group_main_page.this,"Change admin request is executed", Toast.LENGTH_LONG).show();
                leave();
            }

        },
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest2);
    }
    public void getMemberId()
    {
        String url1 = IS_A_MEMBER + groupid;
        System.out.println(url1);
        requestQueue = Volley.newRequestQueue(Group_main_page.this);
        JsonArrayRequest queueRequest1;
        queueRequest1 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";

                    for (int i = 0; i < response.length(); ++i) {

                        JSONObject o = null;
                        try {
                            o = response.getJSONObject(i);
                            member = o.getInt("user_id");
                            mems.add(member);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }

                    for(Integer i: mems)
                    {
                        if(i.intValue() == userid)
                        {
                            isMember = true;
                            settings.setVisibility(View.VISIBLE);
                            settings.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    buildDialog2(1);
                                }});
                            topic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                                            .putExtra("group name",groupName)
                                            .putExtra("group id",groupid)
                                            .putExtra("user id",userid)
                                            .putExtra("user name", userName));
                                    finish();
                                }});
                            break;

                        }
                    }
                    if(!isMember)
                    {
                        topic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Group_main_page.this, "Join the Group First", Toast.LENGTH_LONG).show();
                            }});
                        checkIfRequested();

                    }

            }},
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show());
        requestQueue.add(queueRequest1);
    }
    public void getAdminId()
    {
        String url1 = ADMIN_CHECK + groupid;
        System.out.println(url1);
        requestQueue = Volley.newRequestQueue(Group_main_page.this);
        JsonArrayRequest queueRequest1;
        queueRequest1 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";

                    for (int i = 0; i < response.length(); ++i) {

                        JSONObject o = null;
                        try {
                            o = response.getJSONObject(i);
                            admin = o.getInt("admin_id");
                            System.out.println(admin);
                            int b = admin;
                            if(b == userid)
                            {

                                settings.setVisibility(View.VISIBLE);
                                settings.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        buildDialog2(0);
                                    }
                                });
                                topic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                                            .putExtra("group name",groupName)
                                            .putExtra("group id",groupid)
                                            .putExtra("user id",userid)
                                            .putExtra("user name", userName));
                                    finish();
                                }
                            });
                            }
                            else
                            {
                                getMemberId();
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }}},
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest1);
    }
    public void leave()
    {
        String leave = LEAVE + userid + "/" + groupid;
        System.out.println(leave);
        requestQueue = Volley.newRequestQueue(Group_main_page.this);
        StringRequest queueRequest;
        queueRequest = new StringRequest(Request.Method.GET ,leave, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(Group_main_page.this, myGroups.class);
                intent.putExtra("user id", userid );
                intent.putExtra("user name", userName);
                intent.putExtra("group id", groupid);
                intent.putExtra("group name", groupName);
                startActivity(intent);
                Toast.makeText(Group_main_page.this,"Leave request is executed", Toast.LENGTH_LONG).show();
            }},
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
    }
    public void join()
    {
        String url = REQUESTJOIN_URL + groupid + "/" + userid +"/"+ groupid ;
        System.out.println(url);
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest;
        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(Group_main_page.this, "Join Request Sent", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Group_main_page.this, Group_main_page.class);
                intent.putExtra("user id", userid);
                intent.putExtra("group id", groupid);
                intent.putExtra("user name", userName);
                startActivity(intent);
                finish();
            }



        }
                ,
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
    }

    public void checkIfRequested()
    {
        String url = CHECK_URL + userid;
        System.out.println(url);
        requestQueue = Volley.newRequestQueue(this);
        System.out.println("test");
        ArrayList<Integer> alreadyRequested = new ArrayList<>();
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
                        int r_id = o.getInt("group_id");
                        alreadyRequested.add(r_id);
                        System.out.println(r_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                join_btn.setVisibility(View.VISIBLE);
                if(alreadyRequested.contains(userid)) {

                    join_btn.setText("requested");
                    join_btn.setTextColor(Color.parseColor("#FFFF9800"));
                    join_btn.setBackgroundColor(Color.parseColor("#FF393939"));

                }
                else
                {
                    join_btn.setVisibility(View.VISIBLE);
                    join_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            join();
                        }});

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Group_main_page.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show(); }
        });


        requestQueue.add(queueRequest);
    }
   }