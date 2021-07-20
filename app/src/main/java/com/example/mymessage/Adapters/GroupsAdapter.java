package com.example.mymessage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mymessage.Models.UserGroups;
import com.example.mymessage.R;


import java.util.ArrayList;


public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder>{

    ArrayList<UserGroups> list;
    Context context;

    public GroupsAdapter(ArrayList<UserGroups> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.example.mymessage.R.layout.sample_show_group_chat, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupsAdapter.ViewHolder holder, int position) {

        UserGroups userGroups = list.get(position);

        holder.groupName.setText(userGroups.getGroupName());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView groupName;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image_group);
            groupName = itemView.findViewById(R.id.groupName);
        }
    }
}
