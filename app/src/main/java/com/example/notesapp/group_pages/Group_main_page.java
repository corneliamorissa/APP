package com.example.notesapp.group_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.MemberList;
import com.example.notesapp.R;
import com.example.notesapp.topic_pages.Topic_Main_Page;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group_main_page extends AppCompatActivity  {
    String groupName, userName, selectedAdmin;
    private static final String DELETE = "https://studev.groept.be/api/a21pt103/delete_group/";
    private static final String LEAVE = "https://studev.groept.be/api/a21pt103/leave_group/";
    private static final String SPINNER = "https://studev.groept.be/api/a21pt103/geab_all_memebrs_of_a_group/";
    private static final String ADMIN_CHECK ="https://studev.groept.be/api/a21pt103/get_admin_id/";
    private static final String UPDATE_ADMIN ="https://studev.groept.be/api/a21pt103/make_another_admin/";
    private static final String IS_A_MEMBER = "https://studev.groept.be/api/a21pt103/isMember/";
    private static final String REQUESTJOIN_URL = "https://studev.groept.be/api/a21pt103/send_join_request/";
    private static final String CHECK_URL = "https://studev.groept.be/api/a21pt103/check_if_request_sent/";
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/groupPict/";
    private static final String POST_URL = "https://studev.groept.be/api/a21pt103/insertGroupPict/";

    private int PICK_IMAGE_REQUEST = 111;
    private RequestQueue requestQueue;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    Integer admin, member;
    boolean isAdmin;
    boolean isMember;
    int groupid, userid;
    TextView name_show;
    AlertDialog dialog1;
    AlertDialog dialog2 ;
    ArrayAdapter<String> adminAdapter;
    ArrayList<Integer> adminIdList = new ArrayList<>();
    ArrayList<String> adminUsernameList = new ArrayList<>();
    ArrayList<Integer> memberID;
    ArrayList<String> memberNames;
    FloatingActionButton settings;
    int test;
    Button join_btn;
    Button topic;
    boolean myGroups, mainpage;
    ImageView groupPict;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main_page);
        name_show = findViewById(R.id.groupName);

        //Button delete2 = findViewById(R.id.delete_group2);
        //Button leave2 = findViewById(R.id.leave_group2);
        join_btn = findViewById(R.id.join_button);
        settings = findViewById(R.id.settings_group);
        topic = findViewById(R.id.topics);
        groupPict = findViewById(R.id.imageView3);
        Bundle extras = getIntent().getExtras();
        test = 0;
        memberID = new ArrayList<>();
        memberNames = new ArrayList<>();

        if (extras != null) {
            groupName = extras.getString("group name");
            groupid = extras.getInt("group id");
            userid = extras.getInt("user id");
            userName = extras.getString("user name");
            myGroups = extras.getBoolean("my groups");
            mainpage = extras.getBoolean("main page");

        }
        name_show.setText(groupName);
        join_btn.setVisibility(View.INVISIBLE);

        ActionBar actionBar = getSupportActionBar(); // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle(groupName);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        getAdminId();
        grabPProfilePict();


    }
    /**
     * Helper method to create a rescaled bitmap. You enter a desired width, and the height is scaled uniformly
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale = ((float) newWidth) / width;

        // We create a matrix to transform the image
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create the new bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void grabPProfilePict()
    {
        String url = GET_IMAGE_URL +groupid;
        System.out.println(url);
        @SuppressLint("UseCompatLoadingForDrawables") JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    //Check if the DB actually contains an image
                    if (response.length() == 0) {
                    }
                    else{
                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject o;
                            try {
                                o = response.getJSONObject(0);
                                System.out.println(o);
                                //converting base64 string to image
                                String b64String = o.getString("group_pict");

                                if(b64String.equals("null"))
                                {
                                    groupPict.setImageDrawable(getDrawable(R.drawable.group));
                                }

                                else {
                                    byte[] imageBytes = Base64.decode(b64String, Base64.DEFAULT);
                                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                    groupPict.setImageBitmap(bitmap2);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Just a double-check to tell us the request has completed
                        Toast.makeText(Group_main_page.this, "Group Profile Refreshed", Toast.LENGTH_SHORT).show();

                    }


                },
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()
        );
        requestQueue.add(retrieveImageRequest);

    }

    public void onBtnPickClicked(View caller)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        //this line will start the new activity and will automatically run the callback method below when the user has picked an image
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    /**
     * Processes the image picked by the user. For now, the bitmap is simply stored in an attribute.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Rescale the bitmap to 400px wide (avoid storing large images!)
                bitmap = getResizedBitmap( bitmap, 400 );
                //Setting image to ImageView
                groupPict.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(myGroups)
        {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(Group_main_page.this, com.example.notesapp.group_pages.myGroups.class);
                intent.putExtra("user id", userid);
                intent.putExtra("user name", userName);
                intent.putExtra("group id", groupid);
                intent.putExtra("group name", groupName);
                intent.putExtra("main page", mainpage);
                intent.putExtra("my groups", myGroups);
                startActivity(intent);
                this.finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
        else {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(Group_main_page.this, GroupList.class);
                intent.putExtra("user id", userid);
                intent.putExtra("user name", userName);
                intent.putExtra("group id", groupid);
                intent.putExtra("group name", groupName);
                intent.putExtra("main page", mainpage);
                intent.putExtra("my groups", myGroups);
                startActivity(intent);
                this.finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    public void onMemebrs_Clicked(View caller) {
        startActivity(new Intent(Group_main_page.this, MemberList.class)
                .putExtra("user id",userid)
                .putExtra("user name", userName)
                .putExtra("group id",groupid)
                .putExtra("main page", mainpage)
                .putExtra("my groups", myGroups)
        );
        finish();
    }



// if user is group admin they have the power to delete group
    public void deleteGroup(){
            String delete = DELETE + groupid +  "/" + groupid +  "/" + groupid +  "/" + groupid;
            System.out.println(delete);
            requestQueue = Volley.newRequestQueue(this);

            StringRequest queueRequest;
            queueRequest = new StringRequest(Request.Method.GET, delete, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(Group_main_page.this,"Group Deleted", Toast.LENGTH_LONG).show();

                    if(myGroups)
                    {
                        Intent intent = new Intent(Group_main_page.this, myGroups.class);
                        intent.putExtra("user id", userid );
                        intent.putExtra("user name", userName);
                        intent.putExtra("group id", groupid);
                        intent.putExtra("group name", groupName);
                        intent.putExtra("main page", mainpage);
                        intent.putExtra("my groups", myGroups);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Group_main_page.this,"Delete request is executed", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = new Intent(Group_main_page.this, GroupList.class);
                        intent.putExtra("user id", userid );
                        intent.putExtra("user name", userName);
                        intent.putExtra("group id", groupid);
                        intent.putExtra("group name", groupName);
                        intent.putExtra("main page", mainpage);
                        intent.putExtra("my groups", myGroups);
                        startActivity(intent);

                    }

                }

            },
                    error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

            );
            requestQueue.add(queueRequest);
        }


// if admin wants to leave group they must first transfer admin powers to another user
    private void changeAdminDialog( ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Group_main_page.this);
        View mview = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setTitle("Choose new Admin : ");
        Spinner spinner = (Spinner) mview.findViewById(R.id.choose_admin_spinner);


        requestQueue = Volley.newRequestQueue(this);

        String url = SPINNER + groupid;
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
        dialog1.show();


    }

    /**
     * Submits a new image to the database
     */
    @SuppressLint("NonConstantResourceId")
    public void onBtnPostClicked(View caller)
    {

        //Start an animating progress widget
        progressDialog = new ProgressDialog(Group_main_page.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();

        String upload = POST_URL;
        //convert image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //Execute the Volley call. Note that we are not appending the image string to the URL, that happens further below
        StringRequest submitRequest = new StringRequest(Request.Method.POST, upload,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Turn the progress widget off

                progressDialog.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Group_main_page.this, "Post request failed", Toast.LENGTH_LONG).show();
            }
        }) { //NOTE THIS PART: here we are passing the parameter to the webservice, NOT in the URL!
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("img", imageString);
                params.put("id", String.valueOf(groupid));
                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

//this doalog shows the possible actions a group memebr can take: leave and if admin they can also delte group and change group picture
    private void GroupSettingDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Group_main_page.this);
        View mview = getLayoutInflater().inflate(R.layout.settings, null);
        Button leave2 = mview.findViewById(R.id.leave_group2);
        Button delete2 = mview.findViewById(R.id.delete_group2);
        Button photo = mview.findViewById(R.id.change_photo);
        delete2.setVisibility(View.INVISIBLE);
        leave2.setVisibility(View.INVISIBLE);
        photo.setVisibility(View.INVISIBLE);

        if(i == 0)//this int is used to let the softwsre know wether the use is admin or memebr
        {
            delete2.setVisibility(View.VISIBLE);
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
                    changeAdminDialog();
                }
            });
            photo.setVisibility(View.VISIBLE);
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBtnPostClicked(v);
                }
            });
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



// sends query to change admin of the group
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

    // gets a list of all members of the group
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
                            memberID.add(member);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }

                    for(Integer i: memberID)
                    {
                        if(i.intValue() == userid)
                        {
                            isMember = true;
                            settings.setVisibility(View.VISIBLE);
                            settings.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    GroupSettingDialog(1);
                                }});
                            topic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                                            .putExtra("group name",groupName)
                                            .putExtra("group id",groupid)
                                            .putExtra("user id",userid)
                                            .putExtra("user name", userName)
                                    .putExtra("main page", mainpage)
                                    .putExtra("my groups", myGroups));
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
    //gets the admin if of a group
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
                                        GroupSettingDialog(0);
                                    }
                                });
                                topic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Group_main_page.this, Topic_Main_Page.class)
                                            .putExtra("group name",groupName)
                                            .putExtra("group id",groupid)
                                            .putExtra("user id",userid)
                                            .putExtra("user name", userName)
                                            .putExtra("main page", mainpage)
                                            .putExtra("my groups", myGroups)
                                    );
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
    // delets the member from the group
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
                intent.putExtra("main page", mainpage);
                intent.putExtra("my groups", myGroups);
                finish();
                startActivity(intent);
                Toast.makeText(Group_main_page.this,"Leave request is executed", Toast.LENGTH_LONG).show();
            }},
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
    }
    //if a non memebr is vieing the group, and clicks the join button a join request will be sent to the group admin
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
                intent.putExtra("group name", groupName);
                intent.putExtra("user name", userName);
                intent.putExtra("main page", mainpage);
                intent.putExtra("my groups", myGroups);
                startActivity(intent);
                finish();
            }



        }
                ,
                error -> Toast.makeText(Group_main_page.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
    }
    // this checks if a user has already requested to join the group=> to determine if join button should be disabled
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