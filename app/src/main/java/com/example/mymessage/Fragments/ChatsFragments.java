//package com.example.mymessage.Fragments;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.mymessage.Adapters.UsersAdapter;
//import com.example.mymessage.Models.Friends;
//import com.example.mymessage.Models.Users;
//import com.example.mymessage.databinding.FragmentChatsBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//
//public class ChatsFragments extends Fragment {
//
//    public ChatsFragments() {
//        // Required empty public constructor
//    }
//
//    FragmentChatsBinding binding;
//    ArrayList<Users> list = new ArrayList<>();
//    FirebaseDatabase database;
//    Users me = new Users();
//    ArrayList<Friends> friendsArrayList = new ArrayList<>();
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        binding = FragmentChatsBinding.inflate(inflater, container, false);
//        database =FirebaseDatabase.getInstance();
//
//        UsersAdapter adapter = new UsersAdapter(list, getContext());
//        binding.chatRecyclarView.setAdapter(adapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        binding.chatRecyclarView.setLayoutManager(layoutManager);
//
//
//
//
//        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Users users = dataSnapshot.getValue(Users.class);
//                    users.setUserId(dataSnapshot.getKey());
//                    if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
//                        list.add(users);
//                    }
//
//
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//        return binding.getRoot();
//    }
//}

package com.example.mymessage.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mymessage.Adapters.UsersAdapter;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Users;
import com.example.mymessage.databinding.FragmentChatsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ChatsFragments extends Fragment {

    public ChatsFragments() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    ArrayList<Friends> listFriend = new ArrayList<>();

    FirebaseDatabase database;
    FirebaseAuth auth;
    Users me = new Users();
//    ArrayList<Friends> friendsArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
//        database1 = FirebaseDatabase.getInstance();
//        database2 = FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        UsersAdapter adapter1 = new UsersAdapter(list, getContext());
        binding.chatRecyclarView.setAdapter(adapter1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclarView.setLayoutManager(layoutManager);


        database.getReference().child("Friends").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listFriend.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friends friends = dataSnapshot.getValue(Friends.class);
                    listFriend.add(friends);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
//                    if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
//                        list.add(users);
//                    }

                    for (int i = 0; i < listFriend.size(); i++) {
                        if (listFriend.get(i).getFriendId() != null) {
                            if (listFriend.get(i).getFriendId().equals(users.getUserId())) {
                                list.add(users);
                            }
                        }
                    }
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}