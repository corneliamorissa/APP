package com.example.notesapp.userInfo;

public class UserInfo  {

    private static UserInfo INSTANCE = null;

    private int id;
    private String name;
    private String pass;
    private String email;
    private String user;

    private UserInfo() {};

    public static synchronized UserInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserInfo();
        }
        return(INSTANCE);
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // other instance methods can follow
}


