package com.example.mymessage.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mymessage.Adapters.ChatAdapter;
import com.example.mymessage.Adapters.FragmentsAdapter;
import com.example.mymessage.Adapters.FriendsAdapter;
import com.example.mymessage.Models.MessageModel;
import com.example.mymessage.Models.UserStatus;
import com.example.mymessage.Models.Users;
import com.example.mymessage.Notifications.FcmNotificationsSender;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ProgressDialog dialog;
    ArrayList<MessageModel> messages;

    String tk;
    Users user;
    String uName;

    private static final String TAG = Context.class.getName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        String recieveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);



        messages = new ArrayList<>();

        final String senderId = auth.getUid();
        Date date = new Date();



        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_avatar).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, recieveId, senderId);

        binding.chatRecyclarView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chatRecyclarView.setLayoutManager(linearLayoutManager);


        final String senderRoom = senderId + recieveId;
        final String receiverRoom = recieveId + senderId;

        database.getReference().child("presence").child(recieveId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if(!status.isEmpty()) {
                        if(status.equals("Offline")) {
                            binding.status.setVisibility(View.GONE);
                        } else {
                            binding.status.setText(status);
                            binding.status.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Token").child(recieveId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    tk = snapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Users.class);
                        uName = user.getUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messageModels.clear();

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessageModel model = snapshot1.getValue(MessageModel.class);

                            model.setMessageId(snapshot1.getKey());

                            messageModels.add(model);
                        }

                        chatAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = binding.edtMessage.getText().toString();

                Date date = new Date();
                MessageModel message = new MessageModel(messageTxt, auth.getUid(), date.getTime());
                binding.edtMessage.setText("");

                String randomKey = database.getReference().push().getKey();
                message.setMessageId(randomKey);


                database.getReference().child("Friends")
                        .child(senderId)
                        .child(recieveId)
                        .child("msgTimeLast").setValue(message.getTimestamp()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Friends")
                                .child(recieveId)
                                .child(senderId)
                                .child("msgTimeLast").setValue(message.getTimestamp());
                    }
                });

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child(message.getMessageId())
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child(message.getMessageId())
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });

                String title = "New message from "+uName;
                Log.e(TAG, tk);
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(tk, title, messageTxt, getApplication(), ChatDetailActivity.this);
                notificationsSender.SendNotifications();

//                FirebaseMessaging.getInstance().getToken()
//                        .addOnCompleteListener(new OnCompleteListener<String>() {
//                            @Override
//                            public void onComplete(@NonNull Task<String> task) {
//                                if (!task.isSuccessful()) {
//                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                                    return;
//                                }
//
//                                // Get new FCM registration token
//                                String token = task.getResult();
//
//
//
//                                // Log and toast
//                                String title = "New message from "+userName;
//                                Log.e(TAG, token);
//                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token, title, messageTxt, getApplication(), ChatDetailActivity.this);
//                                notificationsSender.SendNotifications();
//                            }
//                        });



            }
        });

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });

//        final Handler handler = new Handler();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 25) {
            if(data != null) {
                if(data.getData() != null) {
                    Uri selectedImage = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    dialog.show();
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();

                                        String recieveId = getIntent().getStringExtra("userId");
                                        String userName = getIntent().getStringExtra("userName");
                                        String profilePic = getIntent().getStringExtra("profilePic");

                                        final String senderRoom = auth.getUid() + recieveId;
                                        final String receiverRoom = recieveId + auth.getUid();

                                        String messageTxt = binding.edtMessage.getText().toString();

                                        Date date = new Date();
                                        MessageModel message = new MessageModel(messageTxt, auth.getUid(), date.getTime());
                                        message.setMessage("photo");
                                        message.setImageUrl(filePath);
                                        binding.edtMessage.setText("");

//                                        String randomKey = database.getReference().push().getKey();
//
//                                        HashMap<String, Object> lastMsgObj = new HashMap<>();
//                                        lastMsgObj.put("lastMsg", message.getMessage());
//                                        lastMsgObj.put("lastMsgTime", date.getTime());
//
//                                        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
//                                        database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                                        database.getReference().child("chats")
                                                .child(senderRoom)
                                                .push()
                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                database.getReference().child("chats")
                                                        .child(receiverRoom)
                                                        .push()
                                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
                                            }
                                        });

                                        //Toast.makeText(ChatActivity.this, filePath, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
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