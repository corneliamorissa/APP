package com.example.notesapp.appObjects;

public class Group {
    private int id;
    private String name;
    private int a_id;
    private String date;

    public Group(int i,String n,int a, String d)
    {
        id = i;
        name = n;
        a_id =a;

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

    public int getA_id() {
        return a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

