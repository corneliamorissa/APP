package com.example.notesapp.userInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class UserLog implements Parcelable {
    protected String username;
    protected String password;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected int id;

    public UserLog(Parcel in) {
        //id = in.readInt();
        username = in.readString();
        password = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        email = in.readString();
    }

    public UserLog(String eUserName, String ePass, String efirstName, String elastName, String eEmail) {
        this.username = eUserName;
        this.password = ePass;
        this.firstname = efirstName;
        this.lastname = elastName;
        this.email = eEmail;
    }


    public String getUserName() {
        return username;
    }

    public void setUserName(String user) {
        this.username = user;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstname(String first_name) {
        this.firstname = first_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname(){
        return lastname;
    }

    public void setLastname(String last_name)
    {
        this.lastname = last_name;
    }


    public static final Parcelable.Creator<UserLog> CREATOR = new Creator<UserLog>() {
        @Override
        public UserLog createFromParcel(Parcel in) {
            return new UserLog(in);
        }

        @Override
        public UserLog[] newArray(int size) {
            return new UserLog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(username);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(password);
        dest.writeString(email);
    }
}
