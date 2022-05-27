package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FullScreenImage extends AppCompatActivity {

    Button btnClose;
    ImageButton btnDelete;
    Integer imgId;
    private RequestQueue requestQueue;
    private static final String DELETE = "https://studev.groept.be/api/a21pt103/deleteImage/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("image");
        imgId = extras.getInt("image id");
        btnClose = (Button) findViewById(R.id.btnClose);
        btnDelete = (ImageButton) findViewById(R.id.delete_image);

        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenImage.this.finish();
            }
        });

        fullScreenImageView.setImageBitmap(bmp);
    }

    public void onDeleteBtn_Click(View v)
    {
        String delete = DELETE + imgId;
        System.out.println("try to delete url :"+ delete);
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
                        System.out.println("test111111");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

                Toast.makeText(FullScreenImage.this,"Note Deleted", Toast.LENGTH_LONG).show();
                finish();

            }



        }
        ,
                error -> Toast.makeText(FullScreenImage.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
    }

}