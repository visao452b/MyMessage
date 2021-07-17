package com.example.mymessage.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymessage.Adapters.GroupAdapter;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.UserGroup;
import com.example.mymessage.R;
import com.example.mymessage.databinding.FragmentGroupChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupChatFragments extends Fragment {
    FragmentGroupChatBinding binding;
    ArrayList<UserGroup> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;



    public GroupChatFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupChatBinding.inflate(inflater, container,false);
        GroupAdapter adapter = new GroupAdapter(list, getContext());
        binding.groupRecyclarView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.groupRecyclarView.setLayoutManager(layoutManager);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        database.getReference().child("UserGroup").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserGroup userGroup = dataSnapshot.getValue(UserGroup.class);
                    list.add(userGroup);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }
}