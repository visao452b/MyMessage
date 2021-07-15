package com.example.mymessage.Fragments;

import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymessage.Adapters.FriendsAdapter;
import com.example.mymessage.Adapters.UsersAdapter;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.FragmentChatsBinding;
import com.example.mymessage.databinding.FragmentFriendsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FriendsFragments extends Fragment {

    public FriendsFragments() {
        // Required empty public constructor
    }

    FragmentFriendsBinding binding;
    ArrayList<Friends> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentFriendsBinding.inflate(inflater, container, false);
        FriendsAdapter adapter = new FriendsAdapter(list, getContext());
        binding.friendsRecyclarView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.friendsRecyclarView.setLayoutManager(layoutManager);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        database.getReference().child("Friends").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friends friends = dataSnapshot.getValue(Friends.class);
                    list.add(friends);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        return binding.getRoot();

    }
}