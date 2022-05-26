package com.example.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class Group_main_page extends AppCompatActivity  {
    String groupName, userName, selectedAdmin;
    private static final String DELETE = "https://studev.groept.be/api/a21pt103/delete_group/";
    private static final String LEAVE = "https://studev.groept.be/api/a21pt103/leave_group/";
    private static final String SPINNER = "https://studev.groept.be/api/a21pt103/geab_all_memebrs_of_a_group/";
    private static final String ADMIN_CHECK ="https://studev.groept.be/api/a21pt103/get_admin_id/";
    private static final String UPDATE_ADMIN ="https://studev.groept.be/api/a21pt103/make_another_admin/";
    private static final String MEMBER_CHECK ="https://studev.groept.be/api/a21pt103/check_if_member/";
    private static final String getId = "https://studev.groept.be/api/a21pt103/getId/";
    private static final String IS_A_MEMBER = "https://studev.groept.be/api/a21pt103/isMember/";
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


        delete.setVisibility(View.INVISIBLE);
        leave.setVisibility(View.INVISIBLE);




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




    }

    public void onTopic_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                .putExtra("group name",groupName)
                .putExtra("group id",groupid));
        finish();
    }
    public void onMemebrs_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                .putExtra("group name",groupName)
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
        AlertDialog dialog1 = builder.create();
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
        isMember = false;
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
    /*
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

    }*/

    /*
    to check if user is a member
    String url1 = IS_A_MEMBER + userid + "/" + groupid;


        System.out.println(url1);
        requestQueue = Volley.newRequestQueue(this);
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
    */
}