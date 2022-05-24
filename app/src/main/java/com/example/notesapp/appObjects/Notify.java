package com.example.notesapp.appObjects;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notesapp.GroupList;
import com.example.notesapp.MainPageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Notify {


    private int group_id;
    private int user_id;

    private int request_id;



    private String user_name;

    private RequestQueue requestQueue;


    public Notify(int r_id,int g_id,int u_id)
    {
        group_id =g_id;
        user_id = u_id;
        request_id = r_id;
    }


    public int getRequest_id() {
        return request_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
