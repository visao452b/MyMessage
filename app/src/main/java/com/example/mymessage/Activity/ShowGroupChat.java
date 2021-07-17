package com.example.mymessage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityShowGroupChatBinding;

public class ShowGroupChat extends AppCompatActivity {

    ActivityShowGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}