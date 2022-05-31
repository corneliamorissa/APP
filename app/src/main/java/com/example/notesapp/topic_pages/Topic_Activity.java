package com.example.notesapp.topic_pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.image_pages.FullScreenImage;
import com.example.notesapp.R;
import com.example.notesapp.adapters.RecyclerAdapter;
import com.example.notesapp.RecyclerViewInterface;
import com.example.notesapp.appObjects.ImageModel;
import com.example.notesapp.appObjects.Topic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//this page shows all the pictures in a topic
public class Topic_Activity extends AppCompatActivity implements RecyclerViewInterface {
    private RequestQueue requestQueue;
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getImagewithTopic/";
    private static final String DELETE = "https://studev.groept.be/api/a21pt103/deleteTopic/";
    private int group_id;
    private int user_id;
    private int topic_id, image_id;
    private ArrayList<Topic> topics;
    RecyclerView recyclerView;
    ArrayList<ImageModel> imgList;
    RecyclerAdapter adapter;
    ImageView imageView;
    Bitmap image;
    AlertDialog dialog;
    String topicName, userName, groupName;
    FloatingActionButton delete;
    boolean frommygroups, mainpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        requestQueue = Volley.newRequestQueue(this);
        imgList = new ArrayList<ImageModel>();
        imageView = findViewById(R.id.imageRetrieved);
        delete = findViewById(R.id.delete_a_topic);
        Bundle extras = getIntent().getExtras();
        topics = new ArrayList<Topic>();
        if (extras != null) {
            user_id = extras.getInt("user id");
            group_id = extras.getInt("group id");
            topic_id =extras.getInt("topic id");
            topicName = extras.getString("topic name");
            userName = extras.getString("user name");
            groupName = extras.getString("group name");
            frommygroups = extras.getBoolean("my groups");
            mainpage = extras.getBoolean("main page");

            //The key argument here must match that used in the other activity
        }


        /***start of retrieved all notes for specific topic***/


        // calling the action bar
        ActionBar actionBar = getSupportActionBar();


        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("Topic Notes");

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
/////end something??
        //buildDialog();

    }
//this pulls all the pictures from database and the button avoid the page from crashing
    public void onButtonRetrieveNote(View view)
    {
        imgList.clear();
        String url = GET_IMAGE_URL + topic_id + "/" + group_id;
        //Standard Volley request. We don't need any parameters for this one
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, url , null,
                response -> {
                    //Check if the DB actually contains an image
                    if (response.length() == 0) {
                        Intent intent = new Intent(Topic_Activity.this,Topic_Activity.class);
                        intent.putExtra("user id", user_id );
                        intent.putExtra("user name", userName);
                        intent.putExtra("group id", group_id);
                        intent.putExtra("group name", groupName);
                        intent.putExtra("topic id", topic_id);
                        intent.putExtra("my groups", frommygroups);
                        intent.putExtra("main page", mainpage);
                        startActivity(intent);
                        Toast.makeText(Topic_Activity.this, "No Topic Documents were found", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        this.finish();
                    }
                    else{
                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject o;
                            try {
                                o = response.getJSONObject(i);

                                //converting base64 string to image
                                String b64String = o.getString("image");
                                String titleI = o.getString("title");
                                String descI = o.getString("description");
                                String topicI = o.getString("topic");
                                this.image_id = o.getInt("id");
                                byte[] imageBytes = Base64.decode(b64String, Base64.DEFAULT);
                                Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


                                this.image = bitmap2;
                                System.out.println(titleI);
                                System.out.println(descI);
                                ImageModel imageModel = new ImageModel(bitmap2,titleI,descI,topicI,image_id);
                                imgList.add(imageModel);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Just a double-check to tell us the request has completed
                        Toast.makeText(Topic_Activity.this, "Image retrieved from DB", Toast.LENGTH_SHORT).show();
                        adapter = new RecyclerAdapter(imgList, this);
                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_topic);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Topic_Activity.this));
                    }
                    adapter.notifyDataSetChanged();

                },
                error -> Toast.makeText(Topic_Activity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()
        );
        requestQueue.add(retrieveImageRequest);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Topic_Activity.this, Topic_Main_Page.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", userName);
                intent.putExtra("group id", group_id);
                intent.putExtra("topic name", topicName);
                intent.putExtra("group name", groupName);
                intent.putExtra("my groups", frommygroups);
                intent.putExtra("main page", mainpage);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(Topic_Activity.this, FullScreenImage.class);
        intent.putExtra("image", imgList.get(position).getPict());
        intent.putExtra("image id", imgList.get(position).getImageId());
        intent.putExtra("my groups", frommygroups);
        intent.putExtra("main page", mainpage);
        intent.putExtra("user id", user_id );
        intent.putExtra("user name",userName);
        intent.putExtra("group id", group_id );
        intent.putExtra("group name", groupName);
        startActivity(intent);

    }
// to delete a topic
    public void onDeleteTopic_Click(View view)
    {
        String delete = DELETE + topic_id;
        System.out.println("try to delete topic :"+ delete);
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest;
        queueRequest = new JsonArrayRequest(Request.Method.GET, delete, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Toast.makeText(Topic_Activity.this,"This topic is deleted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Topic_Activity.this,Topic_Activity.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name",userName);
                intent.putExtra("group id", group_id );
                intent.putExtra("group name", groupName);
                intent.putExtra("my groups", frommygroups);
                intent.putExtra("main page", mainpage);
                startActivity(intent);
                finish();
            }

        }
                ,
                error -> Toast.makeText(Topic_Activity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
    }
    }




