package com.example.notesapp.appObjects;

public class Topic {


    private int id;
    private String name;

    private int g_id;

    public Topic(int i, String n,int g)
    {
        id = i;
        name = n;

        g_id = g;
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


    public int getG_id() {
        return g_id;
    }

    public void setG_id(int g_id) {
        this.g_id = g_id;
    }
}