package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
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
import com.example.notesapp.group_pages.Group_main_page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemberList extends AppCompatActivity {
    ImageView imageView;
    String email, getUsername, username, groupname;
    Integer getId;
    private RequestQueue requestQueue;
    private static final String MEMBERS_URL = "https://studev.groept.be/api/a21pt103/check_if_member/";
    private static final String GET_IMAGE_URL = "https://studev.groept.be/api/a21pt103/getUserPict/";
    private int group_id;
    private ArrayList<Member> members;
    LinearLayout layout;
    private int user_id;
    private Bitmap bitmap2;
    boolean mainpage, frommygroup ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            group_id = extras.getInt("group id");
            user_id = extras.getInt("user id");
            frommygroup = extras.getBoolean("my groups");
            mainpage = extras.getBoolean("main page");
            username = extras.getString("user name");
            groupname = extras.getString("group name");
        }
        members = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        layout = findViewById(R.id.container_memeber);

        populateMembers();

        ActionBar actionBar = getSupportActionBar();
        // Customize the back button
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_west_24);
        actionBar.setTitle("Members");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MemberList.this, Group_main_page.class);
                intent.putExtra("user id", user_id );
                intent.putExtra("user name", username);
                intent.putExtra("group id", group_id);
                intent.putExtra("group name", groupname);
                intent.putExtra("my groups", frommygroup);
                intent.putExtra("main page", mainpage);
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

                                Member m = new Member(o.getInt("user_id"),
                                        o.getString("user_name"),
                                        o.getString("user_pict"),
                                        o.getString("email"));
                                members.add(m);

                                getUsername = o.getString("user_name");
                                getId = o.getInt("user_id");

                                String b64String = o.getString("user_pict");
                                byte[] imageBytes = Base64.decode(b64String, Base64.DEFAULT);
                                Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                email = o.getString("email");
                                @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.member_row, null);

                                imageView = (ImageView) view.findViewById(R.id.user_pic_list);
                                Button member_btn = view.findViewById(R.id.button_user_name);

                                member_btn.setText(m.getUser_name());
                                imageView.setImageBitmap(bitmap2);

                                member_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        /*startActivity(new Intent(MemberList.this, UserProfileActivity.class)
                                                .putExtra("Visiting user id ",user_id)
                                                .putExtra("user id ", m.getId())
                                                .putExtra("user name", m.getUser_name())
                                                .putExtra("email", m.getEmail())

                                        );*/
                                    }
                                });
                                layout.addView(view);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        Toast.makeText(MemberList.this, "Image retrieved from DB", Toast.LENGTH_SHORT).show();

                    }
                },
                error -> Toast.makeText(MemberList.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(queueRequest);
    }




}