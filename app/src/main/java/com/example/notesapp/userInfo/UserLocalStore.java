package com.example.notesapp.userInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_NAME ="userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context)
    {
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeUserData(UserLog user)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("first name", user.firstname);
        spEditor.putString("last name", user.lastname);
        spEditor.putString("username", user.username);
        spEditor.putInt("user id", user.id);
        spEditor.putString("password", user.password);
;    }

    public void getLoggedInUser()
    {
        String firstname= userLocalDatabase.getString("first name", "");
        int id = userLocalDatabase.getInt("user id", -1);
        String lastname = userLocalDatabase.getString("last name","" );
        String password = userLocalDatabase.getString("password","");
        String email = userLocalDatabase.getString("email","");
    }

    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
