package com.example.notesapp.userInfo;

public class UserInfo  {
    protected int id;
    protected String first_name;
    protected String last_name;
    protected String pass;
    protected String email;
    protected String user;

    private static UserInfo INSTANCE = new UserInfo();

   /*
    private UserInfo(int i, String fn, tring ln, String p, String u, String e)
    {
        id = i;
        first_name = fn;
        last_name = ln;
        pass = p;
        user =u;
        email =e;
    };*/

    public static synchronized UserInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserInfo();
            //getInstance().getId(), getInstance().first_name, getInstance().last_name, getInstance().getPass(), getInstance().getUser(), getInstance().getEmail());
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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


