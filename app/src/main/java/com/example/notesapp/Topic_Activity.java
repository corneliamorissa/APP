package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.appObjects.Group;
import com.example.notesapp.appObjects.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Topic_Activity extends AppCompatActivity implements RecyclerViewInterface{
    private RequestQueue requestQueue;
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getImagewithTopic/";
    private int group_id;
    private int user_id;
    private int topic_id;
    private ArrayList<Topic> topics;
    RecyclerView recyclerView;
    ArrayList<ImageModel> imgList;
    RecyclerAdapter adapter;
    ImageView imageView;
    Bitmap image;
    String topicName, userName, groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        requestQueue = Volley.newRequestQueue(this);
        imgList = new ArrayList<ImageModel>();
        imageView = findViewById(R.id.imageRetrieved);
        Bundle extras = getIntent().getExtras();
        topics = new ArrayList<Topic>();
        if (extras != null) {
            user_id = extras.getInt("user id");
            group_id = extras.getInt("group id");
            topic_id =extras.getInt("topic id");
            topicName = extras.getString("topic name");
            userName = extras.getString("user name");
            groupName = extras.getString("group name");
            //The key argument here must match that used in the other activity
        }



        imgList.clear();
        //Standard Volley request. We don't need any parameters for this one
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, GET_IMAGE_URL + topicName, null,
                response -> {
                    //Check if the DB actually contains an image
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject o;
                            try {
                                o = response.getJSONObject(i);

                                //converting base64 string to image
                                String b64String = o.getString("image");
                                String titleI = o.getString("title");
                                String descI = o.getString("description");
                                String topicI = o.getString("topic");
                                byte[] imageBytes = Base64.decode(b64String, Base64.DEFAULT);
                                Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                this.image = bitmap2;
                                System.out.println(titleI);
                                System.out.println(descI);
                                ImageModel imageModel = new ImageModel(bitmap2,titleI,descI,topicI);
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

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();


        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("Topic Notes");

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Topic_Activity.this,Topic_Main_Page.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", userName);
                intent.putExtra("group id", group_id);
                intent.putExtra("topic name", topicName);
                intent.putExtra("group name", groupName);
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
        startActivity(intent);

    }
    }
    //TODO method to grab all docs in the topic




