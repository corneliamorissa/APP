package com.example.notesapp;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        boolean isImageFitToScreen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageRetrieved);
            title = itemView.findViewById(R.id.title_img);
            desc = itemView.findViewById(R.id.desc_img);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isImageFitToScreen) {
                        isImageFitToScreen=false;
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        imageView.setAdjustViewBounds(true);
                    }

                    else{
                        isImageFitToScreen=true;
                        Intent intent = new Intent (v.getContext(), FullScreenImage.class);
                        v.getContext().startActivity(intent);

                        //imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }
            });
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
