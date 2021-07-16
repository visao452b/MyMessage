package com.example.mymessage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mymessage.Adapters.ChatAdapter;
import com.example.mymessage.Adapters.FriendGroupAdapter;
import com.example.mymessage.Adapters.FriendsAdapter;
import com.example.mymessage.Function.RandomString;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.MessageModel;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityCreateGroupChatBinding;
import com.example.mymessage.databinding.ActivityGroupChatBinding;
import com.example.mymessage.databinding.FragmentFriendsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import static com.example.mymessage.Function.RandomString.randomAlphaNumeric;

public class CreateGroupChat extends AppCompatActivity {
    ActivityCreateGroupChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public static String idGroup = randomAlphaNumeric(16);
//    ArrayList<Friends> listfg = new ArrayList<>();


    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9
//    private static final String specials = "~=+%^*/()[]{}/!@#$?|";
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
//    private static final String ALL = alpha + alphaUpperCase + digits + specials;

    private static Random generator = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

//        FriendGroupAdapter adapter = new FriendGroupAdapter(listfg, getApplicationContext());
//        binding.createGroupChatRecyclarView.setAdapter(adapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        binding.createGroupChatRecyclarView.setLayoutManager(layoutManager);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupChat.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<Friends> friendsArrayList = new ArrayList<>();
        final FriendGroupAdapter friendGroupAdapter = new FriendGroupAdapter(friendsArrayList, getApplicationContext());

        binding.createGroupChatRecyclarView.setAdapter(friendGroupAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.createGroupChatRecyclarView.setLayoutManager(linearLayoutManager);



//        String groupId = RandomString.randomAlphaNumeric(16);
//        binding.tvMember.setText(groupId);


        database.getReference().child("Friends").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                friendsArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friends friends = dataSnapshot.getValue(Friends.class);
                    friendsArrayList.add(friends);
                }
                friendGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        binding.nextCreateGroupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupChat.this, SettingGroupChat.class);
                intent.putExtra("GroupId", returnData());
                onRestart();
                idGroup = randomAlphaNumeric(16);
                startActivity(intent);
            }
        });


    }

    public static String returnData() {
        return idGroup;
    }
}