package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.util.Base64;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.userInfo.UserInfo;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

public class UserDocument extends AppCompatActivity {


    private RequestQueue requestQueue;
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getLastImage/";
    RecyclerView recyclerView;
    ArrayList<ImageModel> imgList;
    RecyclerAdapter adapter;
    ImageView imageView;
    boolean isImageFitToScreen;
    String user_name;
    int user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_document);
        requestQueue = Volley.newRequestQueue(this);
        imgList = new ArrayList<ImageModel>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            //The key argument here must match that used in the other activity
        }

    }





    public void onBtnRetrievedImg(View caller) {
        imgList.clear();
        String userName = "mae3";
        //Standard Volley request. We don't need any parameters for this one
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, GET_IMAGE_URL + userName, null,
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
                                byte[] imageBytes = Base64.decode(b64String, Base64.DEFAULT);
                                Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                System.out.println(titleI);
                                System.out.println(descI);
                                ImageModel imageModel = new ImageModel(bitmap2,titleI,descI);
                                imgList.add(imageModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Just a double-check to tell us the request has completed
                        Toast.makeText(UserDocument.this, "Image retrieved from DB", Toast.LENGTH_SHORT).show();
                        adapter = new RecyclerAdapter(imgList);
                        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_Gallery_Images);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(UserDocument.this));
                    }
                    adapter.notifyDataSetChanged();

                },
                error -> Toast.makeText(UserDocument.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()
        );
        requestQueue.add(retrieveImageRequest);
    }
}


