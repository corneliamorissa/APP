package com.example.notesapp.image_pages;

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

import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.MainPageActivity;
import com.example.notesapp.NaviagtionPage;
import com.example.notesapp.R;
import com.example.notesapp.adapters.RecyclerAdapter;
import com.example.notesapp.RecyclerViewInterface;
import com.example.notesapp.appObjects.ImageModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

public class UserDocument extends AppCompatActivity implements RecyclerViewInterface {


    private RequestQueue requestQueue;
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getLastImage/";
    RecyclerView recyclerView;
    ArrayList<ImageModel> imgList;
    RecyclerAdapter adapter;
    ImageView imageView;
    String user_name, email;
    int user_id;
    Bitmap image;
    Integer image_id;
    boolean mainpage;
    FloatingActionButton upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_document);
        requestQueue = Volley.newRequestQueue(this);
        imgList = new ArrayList<ImageModel>();
        imageView = findViewById(R.id.imageRetrieved);
        upload = findViewById(R.id.upload_float);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            email = extras.getString("email");
            mainpage = extras.getBoolean("main page");
            //The key argument here must match that used in the other activity
        }



        // calling the action bar
        ActionBar actionBar = getSupportActionBar();


        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("My Notes");

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        grabNotes();

    }

    public void onFloatingBtn (View v)
    {
        Intent intent = new Intent(UserDocument.this, UploadActivity.class);
        intent.putExtra("user id", user_id);
        intent.putExtra("user name", user_name);
        intent.putExtra("email", email);
        intent.putExtra("main page", mainpage);
        startActivity(intent);
        this.finish();
    }

    public void grabNotes()
    {
        imgList.clear();

        //Standard Volley request. We don't need any parameters for this one
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, GET_IMAGE_URL + user_name, null,
                response -> {
                    //Check if the DB actually contains an image
                    if (response.length() == 0) {

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
                                ImageModel imageModel = new ImageModel(bitmap2,titleI,descI,topicI,image_id);
                                imgList.add(imageModel);





                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Just a double-check to tell us the request has completed
                        Toast.makeText(UserDocument.this, "Image retrieved from DB", Toast.LENGTH_SHORT).show();
                        if(imgList.isEmpty())
                        {

                        }
                        else {
                            adapter = new RecyclerAdapter(imgList, this);
                            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Gallery_Images);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(UserDocument.this));
                            adapter.notifyDataSetChanged();

                        }

                    }
                    //adapter.notifyDataSetChanged();

                },
                error -> Toast.makeText(UserDocument.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()
        );
        requestQueue.add(retrieveImageRequest);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mainpage) {
                    Intent intent = new Intent(UserDocument.this, MainPageActivity.class);
                    intent.putExtra("user id", user_id);
                    intent.putExtra("user name", user_name);
                    intent.putExtra("email", email);
                    intent.putExtra("main page", mainpage);
                    startActivity(intent);
                    this.finish();
                    return true;
                }
                else{
                    Intent intent = new Intent(UserDocument.this, NaviagtionPage.class);
                    intent.putExtra("user id", user_id);
                    intent.putExtra("user name", user_name);
                    intent.putExtra("email", email);
                    intent.putExtra("main page", mainpage);
                    startActivity(intent);
                    this.finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(UserDocument.this, FullScreenImage.class);
        intent.putExtra("image", imgList.get(position).getPict());
        intent.putExtra("image id", imgList.get(position).getImageId());
        startActivity(intent);

    }

}


