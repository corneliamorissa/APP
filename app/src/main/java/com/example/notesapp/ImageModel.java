package com.example.notesapp;

import android.graphics.Bitmap;

public class ImageModel {
    Bitmap pict;
    String title;
    String desc;

    public ImageModel(Bitmap pict, String title, String desc) {
        this.pict = pict;
        this.title = title;
        this.desc = desc;
    }

    public Bitmap getPict() {
        return pict;
    }

    public void setPict(Bitmap b)
    {
        this.pict = b;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String t)
    {
        this.title = t;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String d)
    {
        this.desc = d;
    }
}
