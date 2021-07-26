package com.example.mymessage.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mymessage.Function.RandomString;
import com.example.mymessage.Models.Groups;
import com.example.mymessage.Models.Participants;
import com.example.mymessage.Models.UserGroups;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityCreateGroupChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static android.content.ContentValues.TAG;

public class CreateGroupChat extends AppCompatActivity {

    ActivityCreateGroupChatBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database, database1, database2;

    public static String groupId = RandomString.randomAlphaNumeric(16);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        database2 = FirebaseDatabase.getInstance();

        String uId = auth.getUid();
        Date date = new Date();

        database.getReference().child("Users").child(uId).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(CreateGroupChat.this, "ERROR", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    Toast.makeText(CreateGroupChat.this, String.valueOf(task.getResult().getValue()), Toast.LENGTH_SHORT).show();
//                    String userName = String.valueOf(task.getResult().getValue();
                    String userName = task.getResult().getValue().toString();

                    Participants participant = new Participants("admin", uId, userName, date.getTime());



                    binding.nextCreateGroupChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String groupName = binding.edtNameCreateGroup.getText().toString();
                            Groups group = new Groups(groupId, groupName, uId, date.getTime());


                            UserGroups userGroup = new UserGroups(groupId, groupName, uId);


                            database1.getReference()
                                    .child("Groups")
                                    .child(groupId).setValue(group);
                            database.getReference()
                                    .child("Groups")
                                    .child(groupId)
                                    .child("participants")
                                    .child(participant.getUserId()).setValue(participant);
                            database2.getReference()
                                    .child("UserGroups")
                                    .child(uId)
                                    .child(groupId).setValue(userGroup);


                            Intent intent = new Intent(CreateGroupChat.this, SettingGroupChat.class);
                            intent.putExtra("groupId", groupId);
                            intent.putExtra("groupName", groupName);
//                            groupId = RandomString.randomAlphaNumeric(16);
                            startActivity(intent);

//                            Toast.makeText(CreateGroupChat.this, "Created Group Succcessful "+groupName, Toast.LENGTH_SHORT).show();

//                        HashMap<String, String> hashMap = new HashMap<>();
//                        hashMap.put("userId", participant.getUserId());
//                        hashMap.put("role",participant.getRole());
//                        hashMap.put("timestamp", participant.getTimestamp().toString());

                        }
                    });
                }


            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        groupId = RandomString.randomAlphaNumeric(16);
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }

    public static String retunrStringGroup() {
        return groupId;
    }
}