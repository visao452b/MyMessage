package com.example.mymessage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mymessage.Activity.ChatDetailActivity;
import com.example.mymessage.Activity.GroupChatActivity;
import com.example.mymessage.Models.UserGroups;
import com.example.mymessage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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

        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(userGroups.getGroupId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                holder.lassMg.setText(snapshot1.child("message").getValue().toString());
                                long time = snapshot1.child("timestamp").getValue(Long.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                holder.mgTime.setText(dateFormat.format(new Date(time)));

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((userGroups.getGroupName()!= null) &&(userGroups.getGroupId()!=null)){
                    Intent intent = new Intent(context, GroupChatActivity.class);
                    intent.putExtra("groupId", userGroups.getGroupId());
                    intent.putExtra("groupName", userGroups.getGroupName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView groupName, lassMg, mgTime;

        public ViewHolder(View itemView) {
            super(itemView);
            lassMg = itemView.findViewById(R.id.lastMessageGroup);
            mgTime = itemView.findViewById(R.id.msgTimeGroup);

            image = itemView.findViewById(R.id.profile_image_group);
            groupName = itemView.findViewById(R.id.groupName);
        }
    }
}
