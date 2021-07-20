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
import androidx.core.app.ComponentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessage.Activity.ChatDetailActivity;
import com.example.mymessage.Activity.CreateGroupChat;
import com.example.mymessage.Activity.SettingGroupChat;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Groups;
import com.example.mymessage.Models.Participants;
import com.example.mymessage.Models.UserGroups;
import com.example.mymessage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import static com.example.mymessage.Function.RandomString.randomAlphaNumeric;

public class FriendGroupAdapter extends RecyclerView.Adapter<FriendGroupAdapter.ViewHolderFriendGroup>{
    ArrayList<Friends> listfg;
    Context context;
    String member;

    String groupId = CreateGroupChat.retunrStringGroup();


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
        FirebaseDatabase database, database1;
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
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

        Groups groupChat = new Groups();
        groupChat.setUserIdCreated(auth.getUid());
        groupChat.setGroupId(groupId);
        groupChat.setTimestamp(timestamp);

        Participants participant = new Participants("participant", friends.getFriendId(),friends.getNameFriend(),timestamp);
//        participant.setRole("Participant");
//        participant.setTimestamp(timestamp);
//        participant.setUserId(friends.getFriendId());


//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("userId", participant.getUserId());
//        hashMap.put("role",participant.getRole());
//        hashMap.put("timestamp", participant.getTimestamp().toString());

//        HashMap<String, String> hashMap2 = new HashMap<>();
//        hashMap2.put("userId", uId);
//        hashMap2.put("role","admin");
//        hashMap2.put("timestamp", participant.getTimestamp().toString());

//        database1.getReference()
//                .child("GroupChat")
//                .child(groupId)
//                .child("Paticipant")
//                .child(uId)
//                .setValue(hashMap2);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database, database1;
                database = FirebaseDatabase.getInstance();
                database1 = FirebaseDatabase.getInstance();

                database1.getReference()
                        .child("Groups")
                        .child(groupId)
                        .child("participants")
                        .child(participant.getUserId())
                        .setValue(participant);

//                database.getReference().child("UserGroups").child(participant.getUserId()).child(groupId).setValue();

                member = member +friends.getNameFriend();
                Toast.makeText(context, "Thêm thành công thành công!!! "+ friends.getNameFriend(), Toast.LENGTH_SHORT).show();

                database.getReference().child("Groups").child(groupId).child("groupName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(Task<DataSnapshot> task) {
                        String groupName = task.getResult().getValue().toString();
                        UserGroups userGroup = new UserGroups(groupId, groupName, uId);
                        database.getReference().child("UserGroups").child(participant.getUserId()).child(groupId).setValue(userGroup);
                    }
                });

//                UserGroups userGroup = new UserGroups(groupId, , uId);

            }
        });
    }

    public String returnMember(){
        return member;
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
