package com.example.mymessage.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityFindFriendsBinding;
import com.example.mymessage.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FindFriends extends AppCompatActivity {
    ActivityFindFriendsBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    ArrayList<Users> listFriend = new ArrayList<>();
    Friends friend = new Friends();
    Friends friend2 = new Friends();
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database =FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        final String uId = auth.getUid();


        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user = snapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




        binding.btnFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtFindFriends.getText().toString().isEmpty()){
                    binding.edtFindFriends.setError("Enter Email");
                    return;
                }
                if (binding.edtFindFriends.getText().toString().equals(user.getEmail())){
                    binding.edtFindFriends.setError("Email ERROR");
                    return;
                }
//                String u = binding.edtFindFriends.getText().toString();
                database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listFriend.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Users users = dataSnapshot.getValue(Users.class);
//                            users.setUserId(dataSnapshot.getKey());
//                            if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
//                                listFriend.add(users);
//                            }
                            listFriend.add(users);
//                            binding.tvFriend.append(users.getEmail()+"\n");
                        }
                        int count = 0;
                        for (int i = 0; i < listFriend.size(); i++){
                            if (listFriend.get(i).getEmail()!= null){
                                if (listFriend.get(i).getUserId().equals(uId)){
                                    friend2 = new Friends(listFriend.get(i).getUserName(), listFriend.get(i).getEmail(),listFriend.get(i).getUserId(), listFriend.get(i).getProfilepic());
                                }

                                if (listFriend.get(i).getEmail().equals(binding.edtFindFriends.getText().toString().trim())){
//                                    binding.tvFriend.setText(listFriend.get(i).getEmail());

                                    friend = new Friends(listFriend.get(i).getUserName(), listFriend.get(i).getEmail(),listFriend.get(i).getUserId(), listFriend.get(i).getProfilepic());

                                    binding.userNameFriend.setVisibility(View.VISIBLE);
                                    binding.profileImageFriend.setVisibility(View.VISIBLE);
                                    binding.btnAddFriend.setVisibility(View.VISIBLE);
                                    binding.tvFriend.setVisibility(View.GONE);
                                    Picasso.get()
                                            .load(listFriend.get(i).getProfilepic())
                                            .placeholder(R.drawable.ic_avatar)
                                            .into(binding.profileImageFriend);
                                    binding.userNameFriend.setText(listFriend.get(i).getUserName());
                                    count +=1;
                                }
                            }
//                            binding.tvFriend.append(listFriend.get(i).getEmail());
                        }
                        if (count==0){
                            binding.tvFriend.setVisibility(View.VISIBLE);
                            binding.userNameFriend.setVisibility(View.GONE);
                            binding.profileImageFriend.setVisibility(View.GONE);
                            binding.btnAddFriend.setVisibility(View.GONE);
                            binding.tvFriend.setText("Không tìm thấy bạn bè");
                        }
                        binding.btnAddFriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                reference.child("Friends").child(friend2.getFriendId()).child(friend.getFriendId()).setValue(friend);
                                reference.child("Friends").child(friend.getFriendId()).child(friend2.getFriendId()).setValue(friend2);


                                Toast.makeText(FindFriends.this, "Thêm bạn thành công", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(FindFriends.this, uId, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }



                });
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