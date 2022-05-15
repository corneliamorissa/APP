package com.example.notesapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.appObjects.Group;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupsView> {

    ArrayList<Group> GroupList = new ArrayList<>();

    public GroupAdapter(ArrayList<Group> GroupList) {
        this.GroupList = GroupList;
    }

    @NonNull
    @Override
    public GroupsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group,parent,false);

        return new GroupsView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsView holder, int position) {
        Group group = GroupList.get(position);
        holder.textGroupName.setText(group.getName());

    }





    @Override
    public int getItemCount() {
        return GroupList.size();
    }

    public class GroupsView extends RecyclerView.ViewHolder{

        Button textGroupName;

        public GroupsView(@NonNull View itemView) {
            super(itemView);

            textGroupName = (Button) itemView.findViewById(R.id.text_group_name);

    }
}}
