package com.example.mymessage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mymessage.Adapters.FriendGroupAdapter;
import com.example.mymessage.Function.RandomString;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivitySettingGroupChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

import static com.example.mymessage.Function.RandomString.randomAlphaNumeric;

public class SettingGroupChat extends AppCompatActivity {

    ActivitySettingGroupChatBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database, database1, database2;
//    public static String idGroup = randomAlphaNumeric(16);


    Intent intent = getIntent();
//    String goupId = intent.getStringExtra("groupId");
//    String groupName = intent.getStringExtra("groupName");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        database2 = FirebaseDatabase.getInstance();


        String uId = auth.getUid();
        Date date = new Date();

        final ArrayList<Friends> friendsArrayList = new ArrayList<>();
        final FriendGroupAdapter friendGroupAdapter = new FriendGroupAdapter(friendsArrayList, getApplicationContext());

        binding.settingGroupChatRecyclarView.setAdapter(friendGroupAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.settingGroupChatRecyclarView.setLayoutManager(linearLayoutManager);

//        idGroup = randomAlphaNumeric(16);

//        binding.groupNameSetting.setText(groupName);

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

                Intent intent2 = new Intent(SettingGroupChat.this, ShowGroupChat.class);
//                String groupId = FriendGroupAdapter.returnData();
//                idGroup = RandomString.randomAlphaNumeric(16);
//                intent.putExtra("GroupId", groupId);
                startActivity(intent2);
            }
        });

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