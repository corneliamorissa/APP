package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ImageView image;
    private ImageView imageRetrieved;
    private RequestQueue requestQueue;
    private static final String POST_URL = "https://studev.groept.be/api/a21pt103/insertImage/";
    private int PICK_IMAGE_REQUEST = 111;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private EditText title, desc;
    private CheckBox check_g, check_p;
    private String etitle , edesc;
    private int isGroup = 0 ;
    private Spinner stopic, sgroup;
    ArrayList<String> topicList = new ArrayList<>();
    ArrayList<Integer> topicIdList = new ArrayList<>();
    ArrayList<Integer> groupIdList = new ArrayList<>();
    ArrayList<String> groupList = new ArrayList<>();
    ArrayAdapter<String> topicAdapter;
    ArrayAdapter<String> groupAdapter;
    private String selectedTopic ;
    private String selectedGroup ;
    private int groupId = 0 , topicId ;
    private TextView groupSel, topicSel;
    String user_name;
    int user_id;
    boolean mainpage;



    @SuppressLint("SetTextI18n")
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
        groupSel.setText("Select");


        /***if private checkbox checked, spinner is invisible, user cannot upload images with topic***/
        check_p.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    isGroup = 0;
                    selectedGroup = "0";
                    selectedTopic ="null";
                    groupId = 0;
                    topicId = 0;
                    groupSel.setVisibility(View.INVISIBLE);
                    topicSel.setVisibility(View.INVISIBLE);
                    sgroup.setVisibility(View.INVISIBLE);
                    stopic.setVisibility(View.INVISIBLE);

                }
            }
        }
        );
        /***if group checkbox checked, spinner is visible, user can upload images with topic according to specific group***/

        check_g.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    isGroup = 1;
                    topicId = 0;
                    groupId = 0;
                    selectedTopic = "";
                    selectedGroup="";
                    grabGroupsforSpinner();
                    stopic.setVisibility(View.VISIBLE);
                    sgroup.setVisibility(View.VISIBLE);
                    groupSel.setVisibility(View.VISIBLE);
                    topicSel.setVisibility(View.VISIBLE);
                }


            }
        }
        );


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user name");
            user_id = extras.getInt("user id");
            mainpage = extras.getBoolean("main page");
            //The key argument here must match that used in the other activity
        }



        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("Upload");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    /***grabbing groups name from databse and populate them into spinner***/
    public void grabGroupsforSpinner()
    {
        String url = "https://studev.groept.be/api/a21pt103/grabGroupName/" + user_id;
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
                                groupList.add(groupName);
                                groupIdList.add(groupId);
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
    }

    //back button functionality if clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(UploadActivity.this,MainPageActivity.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", user_name);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /***when user pick images from the storage***/
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
        // If group checked, this will set default value 1 which return true in database
        if(check_g.isChecked()){
            isGroup = 1;
        }

        //if private is checked, this will set default value to 0 for database
        if(check_p.isChecked()){
            isGroup = 0;
            selectedTopic = "";
            groupId = 0;
        }

        //initialized value for database for not null data input
        etitle ="";
        edesc="";
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
                intent.putExtra("main page", mainpage);
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
                params.put("topid", String.valueOf(topicId));
                params.put("uid", String.valueOf(user_id));
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

/***when group is automatically selected, the listener will grab topics from selected group id to populate topic spinner***/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getId() == R.id.spinner_group)
        {
            topicList.clear();
            topicIdList.clear();
            selectedGroup = adapterView.getSelectedItem().toString();
            groupSel.setText(selectedGroup);

            /***to convert from group name to group id for database***/
            for(int j = 0; j<groupList.size();j++)
            {
                if(groupList.get(j).equals(selectedGroup))
                {
                    groupId = groupIdList.get(j);
                }
            }
            String url = "https://studev.groept.be/api/a21pt103/grabTopicGroup/" + groupId;
            JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response.length() ==0)
                            {
                                topicId = 0;
                                selectedTopic = "";
                            }
                            else{
                            for(int i=0; i<response.length();i++){
                                try {
                                    JSONObject o = null;
                                    o = response.getJSONObject(i);
                                    String topicName = o.get("topic_name").toString();
                                    Integer topicId = o.getInt("topic_id");

                                    topicList.add(topicName);
                                    topicIdList.add(topicId);
                                    topicAdapter = new ArrayAdapter<String>(UploadActivity.this,
                                            android.R.layout.simple_spinner_item, topicList);
                                    topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    topicAdapter.notifyDataSetChanged();
                                    stopic.setAdapter(topicAdapter);
                                }

                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            }



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

        /***to convert topic name to topic id for database***/
        for(int j = 0; j<topicList.size();j++)
        {
            if(topicList.get(j).equals(selectedTopic))
            {
                topicId = topicIdList.get(j);
            }
        }
        topicSel.setText(selectedTopic);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

}