package com.example.notesapp;

import android.graphics.Bitmap;

public class ImageModel {
    Bitmap pict;
    String title;
    String desc;
    String topic;
    Integer imageId;

    public ImageModel(Bitmap pict, String title, String desc, String topic, int id) {
        this.pict = pict;
        this.title = title;
        this.desc = desc;
        this.topic = topic;
        this.imageId = id;
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

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String t)
    {
        this.topic = t;
    }

    public int getImageId()
    {
        return imageId;
    }

    public void setImageId(int i)
    {
        this.imageId = i;
    }
}

