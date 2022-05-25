package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.notesapp.userInfo.UserLog;

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
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.example.notesapp.userInfo.UserInfo;
import com.example.notesapp.userInfo.UserLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ImageView image;
    private ImageView imageRetrieved;
    private RequestQueue requestQueue;
    private static final String POST_URL = "https://studev.groept.be/api/a21pt103/insertImage/";
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getLastImage/";
    private int PICK_IMAGE_REQUEST = 111;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private EditText title, desc;
    private CheckBox check_g, check_p;
    private String etitle, edesc;
    private int isGroup = 0 ;
    private Spinner stopic, sgroup;
    ArrayList<String> topicList = new ArrayList<>();
    ArrayList<Integer> groupIdList = new ArrayList<>();
    ArrayList<String> groupList = new ArrayList<>();
    ArrayAdapter<String> topicAdapter;
    ArrayAdapter<String> groupAdapter;
    private String selectedTopic;
    private String selectedGroup;
    private int groupId = 0;
    private TextView groupSel, topicSel;
    String user_name;
    int user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        imageRetrieved = (ImageView)findViewById(R.id.imageRetrieved);
        requestQueue = Volley.newRequestQueue(this);
        title = (EditText) findViewById(R.id.title_img);
        desc = (EditText) findViewById(R.id.desc_img);
        check_g = (CheckBox) findViewById(R.id.checkBoxg);
        check_p = (CheckBox) findViewById(R.id.checkBoxp);
        stopic = (Spinner) findViewById(R.id.spinner_topic);
        sgroup = (Spinner) findViewById(R.id.spinner_group);
        groupSel = (TextView)findViewById(R.id.selected_group);
        topicSel = (TextView)findViewById(R.id.selected_topic);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            //The key argument here must match that used in the other activity
        }

        String url = "https://studev.groept.be/api/a21pt103/grabGroupName";
        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i=0; i<response.length();i++){
                            try {
                                JSONObject o = null;
                                o = response.getJSONObject(i);
                                String groupName = o.get("group_name").toString();
                                Integer groupId = o.getInt("group_id");
                                //Integer group_id = o.getInt("group_id");
                                groupList.add(groupName);
                                groupIdList.add(groupId);
                                //groupIdList.add(group_id);
                                groupAdapter = new ArrayAdapter<String>(UploadActivity.this,
                                        android.R.layout.simple_spinner_item, groupList);
                                groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sgroup.setAdapter(groupAdapter);
                                groupAdapter.notifyDataSetChanged();

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
        requestQueue.add(queueRequest);
        sgroup.setOnItemSelectedListener(this);
        topicSel.setText("Select Your Topic");




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
    @SuppressLint("NonConstantResourceId")
    public void onBtnPostClicked(View caller)
    {
        // Is the view now checked?
        if(check_g.isChecked()){
            isGroup = 1;
        }
        System.out.println(isGroup);
        etitle = title.getText().toString();

        edesc = desc.getText().toString();

        //Start an animating progress widget
        progressDialog = new ProgressDialog(UploadActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();

        String upload = POST_URL;
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
                Intent intent = new Intent(UploadActivity.this, UserDocument.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name",user_name);
                startActivity(intent);
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
                params.put("un", user_name);
                params.put("title", etitle);
                params.put("descr", edesc);
                params.put("ig", String.valueOf(isGroup));
                params.put("top", selectedTopic);
                params.put("gi", String.valueOf(groupId));
                return params;
            }


        };



        requestQueue.add(submitRequest);
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        if(adapterView.getId() == R.id.spinner_group)
        {
            topicList.clear();
            selectedGroup = adapterView.getSelectedItem().toString();
            groupSel.setText(selectedGroup);
            System.out.println(selectedGroup);
            for(int j = 0; j<groupList.size();j++)
            {
                if(groupList.get(j).equals(selectedGroup))
                {
                    groupId = groupIdList.get(j);
                }
            }

            System.out.println(groupId);
            String url = "https://studev.groept.be/api/a21pt103/grabTopicGroup/" + groupId;
            System.out.println(url);
            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            for(int i=0; i<response.length();i++){
                                try {
                                    JSONObject o = null;
                                    o = response.getJSONObject(i);
                                    String topicName = o.get("topic_name").toString();
                                    topicList.add(topicName);
                                    topicAdapter = new ArrayAdapter<String>(UploadActivity.this,
                                            android.R.layout.simple_spinner_item, topicList);
                                    topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    topicAdapter.notifyDataSetChanged();
                                    stopic.setAdapter(topicAdapter);

                                    System.out.println("this is a test:"+adapterView.getSelectedItem().toString());




                                    System.out.println("topics:"+ topicName);
                                }

                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //selectedTopic = adapterView.getSelectedItem().toString();
                            //topicSel.setText(selectedTopic);


                        }
                    } , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(queueRequest);
            stopic.setOnItemSelectedListener(this);

        }
        selectedTopic = adapterView.getSelectedItem().toString();
        topicSel.setText(selectedTopic);

    }




    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


    //TODO back button
}