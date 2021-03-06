package com.example.notesapp.adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.RecyclerViewInterface;
import com.example.notesapp.appObjects.ImageModel;

import java.io.File;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<ImageModel> uriArrayList;
    private final RecyclerViewInterface recyclerViewInterface;
    private File imagesFile;

    public RecyclerAdapter(ArrayList<ImageModel> uriArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.uriArrayList = uriArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView topic;
        TextView title;
        TextView desc;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageRetrieved);
            title = itemView.findViewById(R.id.title_img);
            desc = itemView.findViewById(R.id.desc_img);
            topic = itemView.findViewById(R.id.topicName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!= null)
                    {
                        int pos;
                        pos = getBindingAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(pos);
                        }
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

        return new ViewHolder(view, recyclerViewInterface);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public synchronized void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        ImageModel imageModel = uriArrayList.get(position);
        holder.imageView.setImageBitmap(imageModel.getPict());
        holder.title.setText(imageModel.getTitle());
        holder.desc.setText(imageModel.getDesc());
        holder.topic.setText(imageModel.getTopic());




    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }



}
