package com.example.notesapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final ArrayList<ImageModel> uriArrayList;

    public RecyclerAdapter(ArrayList<ImageModel> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageRetrieved);
            title = itemView.findViewById(R.id.title_img);
            desc = itemView.findViewById(R.id.desc_img);
        }


    }



    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public synchronized void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        ImageModel imageModel = uriArrayList.get(position);
        System.out.println("is this even called?");
        holder.imageView.setImageBitmap(imageModel.getPict());
        holder.title.setText(imageModel.getTitle());
        System.out.println(imageModel.getTitle());
        holder.desc.setText(imageModel.getDesc());
        System.out.println(imageModel.getDesc());
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }


}
