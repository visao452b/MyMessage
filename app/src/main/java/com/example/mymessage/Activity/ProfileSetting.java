package com.example.mymessage.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mymessage.Models.ProfileUser;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityProfileBinding;
import com.example.mymessage.databinding.ActivityProfileSettingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ProfileSetting extends AppCompatActivity {
    ActivityProfileSettingBinding binding;
    ProfileUser profileUser;
    String gender;
    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String uId = auth.getUid();

        database.getReference().child("Profiles").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ProfileUser profileUser = snapshot.getValue(ProfileUser.class);
                binding.edtWork.setText(profileUser.getWork());
                binding.edtLive.setText(profileUser.getLive());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        database.getReference().child("Users").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                Picasso.get()
                        .load(user.getProfilepic())
                        .placeholder(R.drawable.ic_avatar)
                        .into(binding.profileSetting);
                binding.userNameProfile.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




        binding.btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                        String[] arr = getResources().getStringArray(R.array.Gender);
////                        gender = arr[position];
//                        gender = parent.getItemAtPosition(position).toString();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
                profileUser = new ProfileUser(binding.edtLive.getText().toString(), binding.edtWork.getText().toString());
                database.getReference().child("Profiles").child(uId).setValue(profileUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfileSetting.this, "Updated Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



}