package com.example.notesapp.appObjects;

import com.android.volley.RequestQueue;

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
