package com.example.mymessage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mymessage.Adapters.FriendGroupAdapter;
import com.example.mymessage.Adapters.GroupsAdapter;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.UserGroups;
import com.example.mymessage.databinding.ActivityShowGroupChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowGroupChat extends AppCompatActivity {

    ActivityShowGroupChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        final ArrayList<UserGroups> userGroups = new ArrayList<>();
        final GroupsAdapter groupsAdapter = new GroupsAdapter(userGroups, getApplicationContext());

        binding.showGroupRecyclarView.setAdapter(groupsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.showGroupRecyclarView.setLayoutManager(linearLayoutManager);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowGroupChat.this, MainActivity.class);
                startActivity(intent);
            }
        });

        database.getReference().child("UserGroups").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userGroups.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserGroups userGroup = dataSnapshot.getValue(UserGroups.class);
                    userGroups.add(userGroup);
                }
                groupsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

//        final ArrayList<UserGroup> userGroups = new ArrayList<>();


//        final GroupAdapter groupAdapter = new GroupAdapter(userGroups, getApplicationContext());

//        binding.showGroupRecyclarView.setAdapter(groupAdapter);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        binding.showGroupRecyclarView.setLayoutManager(linearLayoutManager);
//
//
//
//
//        database.getReference().child("UserGroup").child(uId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                userGroups.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    UserGroup userGroup = dataSnapshot.getValue(UserGroup.class);
//                    userGroups.add(userGroup);
//                }
//                groupAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }


}