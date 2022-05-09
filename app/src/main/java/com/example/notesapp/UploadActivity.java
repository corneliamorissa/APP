package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class UploadActivity extends AppCompatActivity {

    private ImageView image;
    private ImageView imageRetrieved;
    private RequestQueue requestQueue;
    private static final String POST_URL = "https://studev.groept.be/api/a21pt103/insertImage/";
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getLastImage/";
    private int PICK_IMAGE_REQUEST = 111;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        image = (ImageView)findViewById(R.id.image);
        imageRetrieved = (ImageView)findViewById(R.id.imageRetrieved);
        requestQueue = Volley.newRequestQueue(this);

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
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Submits a new image to the database
     */
    public void onBtnPostClicked(View caller)
    {
        //Start an animating progress widget
        progressDialog = new ProgressDialog(UploadActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();

        String upload = POST_URL + image;
        //convert image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //Execute the Volley call. Note that we are not appending the image string to the URL, that happens further below
        StringRequest  submitRequest = new StringRequest(Request.Method.POST, upload,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Turn the progress widget off

                progressDialog.dismiss();
                Toast.makeText(UploadActivity.this, "Post request executed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadActivity.this, UserDocument.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UploadActivity.this, "Post request failed", Toast.LENGTH_LONG).show();
            }
        }) { //NOTE THIS PART: here we are passing the parameter to the webservice, NOT in the URL!
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("image", imageString);
                return params;
            }
        };

        requestQueue.add(submitRequest);
    }

    /**
     * Retrieves the most recent image from the DB
     */
    public void onBtnRetrieveClicked( View caller )
    {
        //Standard Volley request. We don't need any parameters for this one
        JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, GET_IMAGE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try
                        {
                            //Check if the DB actually contains an image
                            if( response.length() > 0 ) {
                                JSONObject o = response.getJSONObject(0);

                                //converting base64 string to image
                                String b64String = o.getString("image");
                                byte[] imageBytes = Base64.decode( b64String, Base64.DEFAULT );
                                Bitmap bitmap2 = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );

                                //Link the bitmap to the ImageView, so it's visible on screen
                                imageRetrieved.setImageBitmap( bitmap2 );

                                //Just a double-check to tell us the request has completed
                                Toast.makeText(UploadActivity.this, "Image retrieved from DB", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch( JSONException e )
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadActivity.this, "Unable to communicate with server", Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(retrieveImageRequest);
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



    //TODO back button
}