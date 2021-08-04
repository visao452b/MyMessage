package com.example.mymessage.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymessage.Adapters.UsersAdapter;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;


public class ChatsFragments extends Fragment {

    public ChatsFragments() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<Friends> list = new ArrayList<>();



    FirebaseDatabase database;
    FirebaseAuth auth;
//    ArrayList<Friends> friendsArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final String uId = auth.getUid();

        UsersAdapter adapter1 = new UsersAdapter(list, getContext());





        binding.chatRecyclarView.setAdapter(adapter1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclarView.setLayoutManager(layoutManager);



        database.getReference().child("Friends").child(uId).orderByChild("msgTimeLast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friends friends = dataSnapshot.getValue(Friends.class);
                    list.add(friends);
                }

                Collections.reverse(list);

                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });





        return binding.getRoot();
    }

}