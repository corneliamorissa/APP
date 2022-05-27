package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.notesapp.appObjects.Member;
import com.example.notesapp.appObjects.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MemberList extends AppCompatActivity {
    ImageView imageView;
    private RequestQueue requestQueue;
    private static final String MEMBERS_URL = "https://studev.groept.be/api/a21pt103/check_if_member/";
    private int group_id;
    private ArrayList<Member> members;
    LinearLayout layout;
    private int user_id;
    private Bitmap bitmap2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            group_id = extras.getInt("group id");
            user_id = extras.getInt("user_id");

        }
        members = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        layout = findViewById(R.id.container_memeber);

        populateMembers();


    }
    public void populateMembers()
    {
        String url = MEMBERS_URL + group_id;

        JSONObject p = new JSONObject();

        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int p;
                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject o = null;
                            layout = findViewById(R.id.container_memeber);
                            try {
                                o = response.getJSONObject(i);

                                Member m = new Member(o.getInt("user_id"),o.getString("user_name"),o.getString("user_pict"));
                                members.add(m);
                                @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.member_row, null);


                                Button member_btn = view.findViewById(R.id.button_user_name);
                                imageView = (ImageView) findViewById(R.id.user_pic_list);

                                member_btn.setText(m.getUser_name());
                                byte[] imageBytes = Base64.decode(o.getString("user_pict"), Base64.DEFAULT);
                                Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                imageView.setImageBitmap(bitmap2);

                                member_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(MemberList.this, UserProfileActivity.class)
                                                .putExtra("Visiting user id ",user_id)
                                                .putExtra("Profile id ",m.getId())

                                        );
                                    }
                                });

                                layout.addView(view);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                error -> Toast.makeText(MemberList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);
    }




}