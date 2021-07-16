package com.example.mymessage.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mymessage.Fragments.GroupChatFragments;
import com.example.mymessage.Models.GroupChat;
import com.example.mymessage.Models.Image;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivitySettingGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingGroupChat extends AppCompatActivity {

    ActivitySettingGroupChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting_group_chat);
        binding = ActivitySettingGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        Intent intent2 = getIntent();
        String groupId = intent2.getStringExtra("GroupId");



        binding.nextSettingGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameGroup =  binding.edtNameGroupSetting.getText().toString();
                database.getReference()
                        .child("GroupChat")
                        .child(groupId)
                        .child("GroupName")
                        .setValue(binding.edtNameGroupSetting.getText().toString());
                Toast.makeText(SettingGroupChat.this, "Nhóm "+ nameGroup + " đã được tạo", Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(SettingGroupChat.this, MainActivity.class);
                startActivity(intent2);
            }
        });



    }


}