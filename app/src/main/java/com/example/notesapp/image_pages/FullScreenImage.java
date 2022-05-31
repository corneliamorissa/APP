package com.example.notesapp.image_pages;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
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
import com.example.notesapp.R;

import org.json.JSONArray;


public class FullScreenImage extends AppCompatActivity {

    Button btnClose;
    ImageButton btnDelete;
    Integer imgId;
    Bitmap bmp;
    private RequestQueue requestQueue;
    private static final String DELETE = "https://studev.groept.be/api/a21pt103/deleteImage/";
    boolean mainpage, mygroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnDelete = (ImageButton) findViewById(R.id.delete_image);
        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imgId = extras.getInt("image id");
            bmp = extras.getParcelable("image");
            mainpage = extras.getBoolean("main page");
            mygroup = extras.getBoolean("my groups");
            //The key argument that match that used in the other activity
        }


        /***close button listener after user viewed image in full-screen mode***/
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenImage.this.finish();
            }
        });

        fullScreenImageView.setImageBitmap(bmp);
    }

    /***delete button for specific images that is being viewed***/
    public void onDeleteBtn_Click(View v)
    {
        String delete = DELETE + imgId;
        System.out.println("try to delete url :"+ delete);
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest;
        queueRequest = new JsonArrayRequest(Request.Method.GET, delete, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


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