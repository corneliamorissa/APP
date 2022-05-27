package com.example.notesapp.appObjects;

import android.widget.ImageView;

public class Member {

    private String image;
    private int id;
    private String lastName;
    private String firstName;
    private String email;
    private String user_name;

    public Member(int i,String un,String image,String email )
    {
        id = i;
        user_name = un;
        this.email = email;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


}
