package com.example.mymessage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.GroupChat;
import com.example.mymessage.Models.UserGroup;
import com.example.mymessage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolderGroup> {

    ArrayList<UserGroup> list;
    Context context;

    FirebaseDatabase database;
    DatabaseReference reference, reference2;
    FirebaseAuth auth;

    public GroupAdapter(ArrayList<UserGroup> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderGroup onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_group_chat, parent, false);

        return new ViewHolderGroup(view);
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolderGroup holder, int position) {
        auth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        UserGroup userGroup = list.get(position);
//        Picasso.get().load(userGroup.getProfilePicGroup()).placeholder(R.drawable.ic_users).into(holder.image);
        holder.groupName.setText(userGroup.getGroupId());
//        holder.lastmgGroup.setText(friends.getEmailFriend());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolderGroup extends RecyclerView.ViewHolder{

//        ImageView image;
//        TextView groupName, lastmgGroup,time;
        TextView groupName;
        public ViewHolderGroup(View itemView) {
            super(itemView);

//            image = itemView.findViewById(R.id.profile_image_group);
            groupName = itemView.findViewById(R.id.groupName);
//            lastmgGroup = itemView.findViewById(R.id.lastMessageGroup);
//            time = itemView.findViewById(R.id.msgTimeGroup);
        }
    }
}
