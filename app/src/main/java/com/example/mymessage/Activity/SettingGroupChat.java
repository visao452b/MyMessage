package com.example.mymessage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivitySettingGroupChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SettingGroupChat extends AppCompatActivity {

    ActivitySettingGroupChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting_group_chat);
        binding = ActivitySettingGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        String groupId = intent.getStringExtra("GroupId");



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
            }
        });



    }
}