package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    Button saveBtn;
    ImageView imageView;
    TextView userName, totalNote, totalGrp, email;
    String user_name,my_email;
    Integer user_id, visit_user;
    private static final String POST_URL = "https://studev.groept.be/api/a21pt103/insertProfilePict/";
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getUserPict/";
    private static final String SUM_NOTES = "https://studev.groept.be/api/a21pt103/getTotalNotes/";
    private static final String SUM_GROUPS = "https://studev.groept.be/api/a21pt103/getTotalGroup/";
    private int PICK_IMAGE_REQUEST = 111;
    private RequestQueue requestQueue;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("My Profile");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        saveBtn = (Button) findViewById(R.id.button10);
        Bundle extras = getIntent().getExtras();
        visit_user = user_id;
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            my_email = extras.getString("email");
            visit_user = extras.getInt("Visiting user id");
            System.out.println(visit_user);
            //The key argument here must match that used in the other activity
        }

        imageView = (ImageView) findViewById(R.id.my_pict);
        userName = (TextView) findViewById(R.id.my_username);
        totalGrp = (TextView) findViewById(R.id.sum_groups);
        totalNote = (TextView) findViewById(R.id.sum_notes);
        email = (TextView) findViewById(R.id.my_email);
        userName.setText(user_name);
        email.setText(my_email);

        grabSumNotes();
        grabPProfilePict();
        grabSumGroups();

    }

    public void grabSumNotes()
    {
        String url = SUM_NOTES + user_name;
        System.out.println(url);
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest;
        queueRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                for (int i = 0; i < response.length(); ++i) {

                    System.out.println("test wednesday");
                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(0);
                        String totalN = o.getString("sum");
                        System.out.println("total notes :"+ totalN);
                        totalNote.setText(totalN.toString());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }}},
                error -> Toast.makeText(UserProfileActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest);
    }

    public void grabPProfilePict()
    {
        JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, GET_IMAGE_URL + user_name, null,
                response -> {
                    //Check if the DB actually contains an image
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject o;
                            try {
                                o = response.getJSONObject(0);
                                //converting base64 string to image
                                String b64String = o.getString("user_pict");
                                email.setText(o.getString("email"));

                                if(b64String.equals("null"))
                                {
                                    imageView.setImageDrawable(getDrawable(R.drawable.user));
                                }
                                else {
                                    byte[] imageBytes = Base64.decode(b64String, Base64.DEFAULT);
                                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                    imageView.setImageBitmap(bitmap2);
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Just a double-check to tell us the request has completed
                        Toast.makeText(UserProfileActivity.this, "User Profile Refreshed", Toast.LENGTH_SHORT).show();

                    }
                    else{

                    }

                },
                error -> Toast.makeText(UserProfileActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()
        );
        requestQueue.add(retrieveImageRequest);

    }

    public void grabSumGroups()
    {
        totalGrp.setText("0");
        String url1 = SUM_GROUPS + user_id;
        System.out.println(url1);
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest1;
        queueRequest1 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                for (int i = 0; i < response.length(); ++i) {

                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(0);
                        String totalG = o.getString("sum");
                        totalGrp.setText(totalG.toString());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }}},
                error -> Toast.makeText(UserProfileActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show()

        );
        requestQueue.add(queueRequest1);
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(UserProfileActivity.this,NaviagtionPage.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name",user_name);
                intent.putExtra("email", my_email);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Submits a new image to the database
     */
    @SuppressLint("NonConstantResourceId")
    public void onBtnPostClicked(View caller)
    {

        //Start an animating progress widget
        progressDialog = new ProgressDialog(UserProfileActivity.this);
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
                Toast.makeText(UserProfileActivity.this, "Post request failed", Toast.LENGTH_LONG).show();
            }
        }) { //NOTE THIS PART: here we are passing the parameter to the webservice, NOT in the URL!
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("img", imageString);
                params.put("id", String.valueOf(user_id));
                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    public void onBtnShowNotes(View caller) {
        Intent intent = new Intent(UserProfileActivity.this, UserDocument.class);
        intent.putExtra("user id", user_id );
        intent.putExtra("user name",user_name);
        startActivity(intent);
        this.finish();
        super.onBackPressed();
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

}

