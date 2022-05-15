package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.userInfo.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class UserDocument extends AppCompatActivity {

    private ImageView image;
    private ImageView imageRetrieved;
    private RequestQueue requestQueue;
    private static final String POST_URL = "https://studev.groept.be/api/a21pt103/insertImage/";
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getLastImage/";
    private int PICK_IMAGE_REQUEST = 111;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private String userName;
    RecyclerView recyclerView;
    ArrayList<Bitmap> uri= new ArrayList<>();
    RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_document);
        image = (ImageView)findViewById(R.id.image);
        imageRetrieved = (ImageView)findViewById(R.id.imageRetrieved);
        requestQueue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recyclerView_Gallery_Images);
        Button button1 = (Button) findViewById(R.id.btnRetrieve);
        recyclerView.setLayoutManager(new GridLayoutManager(UserDocument.this,1));
        recyclerView.setAdapter(adapter);
        //button1.setSelected(true);
    }

    public void onBtnRetrievedImg (View caller)
    {
        userName = "mae";
        //Standard Volley request. We don't need any parameters for this one
        JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, GET_IMAGE_URL + userName, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Check if the DB actually contains an image
                        if( response.length() > 0 ) {
                            for (int i = 0; i < response.length(); ++i) {
                                JSONObject o = null;
                                try {
                                    System.out.println("this code");
                                    o = response.getJSONObject(0);

                                    //converting base64 string to image
                                    String b64String = o.getString("image");
                                    byte[] imageBytes = Base64.decode(b64String, Base64.DEFAULT);
                                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                    //Link the bitmap to the ImageView, so it's visible on screen

                                    //imageRetrieved.setImageBitmap(bitmap2);
                                    //System.out.println(bitmap2);
                                    uri.add(bitmap2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //Just a double-check to tell us the request has completed
                            Toast.makeText(UserDocument.this, "Image retrieved from DB", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserDocument.this, "Unable to communicate with server", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(retrieveImageRequest);

    }








}