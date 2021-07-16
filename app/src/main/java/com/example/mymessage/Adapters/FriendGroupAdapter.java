package com.example.mymessage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessage.Activity.ChatDetailActivity;
import com.example.mymessage.Activity.CreateGroupChat;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.GroupChat;
import com.example.mymessage.Models.Participant;
import com.example.mymessage.Models.UserGroup;
import com.example.mymessage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.mymessage.Activity.CreateGroupChat.returnData;
import static com.example.mymessage.Function.RandomString.randomAlphaNumeric;

public class FriendGroupAdapter extends RecyclerView.Adapter<FriendGroupAdapter.ViewHolderFriendGroup>{



    String idGroup = returnData();
    ArrayList<Friends> listfg;
    Context context;
    String member;


//    DatabaseReference reference;
//    FirebaseAuth auth;
//    FirebaseDatabase database;

    Date date = new Date();

    public FriendGroupAdapter(ArrayList<Friends> list, Context context) {
        this.listfg = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderFriendGroup onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_friend_create_group_chat, parent, false);

        return new ViewHolderFriendGroup(view);
    }

    @Override
    public void onBindViewHolder(FriendGroupAdapter.ViewHolderFriendGroup holder, int position) {
        DatabaseReference reference;
        FirebaseAuth auth;
        FirebaseDatabase database, database1, database4;
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        database4 = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String uId = auth.getUid();
        Friends friends = listfg.get(position);
        Picasso.get().load(friends.getProfilepic()).placeholder(R.drawable.ic_avatar).into(holder.image);
        holder.userName.setText(friends.getNameFriend());
        holder.email.setText(friends.getEmailFriend());

//        holder.tvMember.setText("");
//        holder.addFriendGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Thêm thành công thành công!!! "+ friends.getNameFriend(), Toast.LENGTH_SHORT).show();
//            }
//        });
        Long timestamp = date.getTime();

        GroupChat groupChat = new GroupChat();
        groupChat.setCreatedBy(auth.getUid());
        groupChat.setGroupId(returnData());
        groupChat.setTimestamp(timestamp);

        Participant participant = new Participant();
        participant.setRole("Participant");
        participant.setTimestamp(timestamp);
        participant.setUserId(friends.getFriendId());


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", participant.getUserId());
        hashMap.put("role",participant.getRole());
        hashMap.put("timestamp", participant.getTimestamp().toString());

        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("userId", uId);
        hashMap2.put("role","admin");
        hashMap2.put("timestamp", participant.getTimestamp().toString());

        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("CreatedBy",groupChat.getCreatedBy());
        hashMap1.put("GroupId", groupChat.getGroupId());
        hashMap1.put("GroupName", groupChat.getGroupId());
        hashMap1.put("Timestamp", groupChat.getTimestamp().toString());

        UserGroup userGroup1 = new UserGroup(uId, idGroup);

        database.getReference()
                .child("GroupChat")
                .child(idGroup)
                .setValue(hashMap1);

        database1.getReference()
                .child("GroupChat")
                .child(idGroup)
                .child("Paticipant")
                .child(uId)
                .setValue(hashMap2);
        database4.getReference()
                .child("UserGroup")
                .child(uId)
                .child(idGroup)
                .setValue(userGroup1);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database3, database1;
                database3 = FirebaseDatabase.getInstance();
                database1 = FirebaseDatabase.getInstance();

                UserGroup userGroup = new UserGroup(participant.getUserId(),idGroup);

                database1.getReference()
                        .child("GroupChat")
                        .child(idGroup)
                        .child("Paticipant")
                        .child(participant.getUserId())
                        .setValue(hashMap);
                database3.getReference()
                        .child("UserGroup")
                        .child(participant.getUserId())
                        .child(idGroup)
                        .setValue(userGroup);

                member = member +friends.getNameFriend();
                Toast.makeText(context, "Thêm thành công thành công!!! "+ friends.getNameFriend(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listfg.size();
    }



    public class ViewHolderFriendGroup extends RecyclerView.ViewHolder{
        ImageView image, addFriendGroup;
        TextView userName, email, tvMember;

        public ViewHolderFriendGroup(View itemView) {
            super(itemView);

            addFriendGroup = itemView.findViewById(R.id.addFriendCreateGroupChat);
            image = itemView.findViewById(R.id.profile_imageShowFriendCreateGroupChat);
            userName = itemView.findViewById(R.id.userNameListShowFriendCreateGroupChat);
            email = itemView.findViewById(R.id.emailShowFriendCreateGroupChat);
            tvMember = itemView.findViewById(R.id.tvMember);
        }

    }





}
