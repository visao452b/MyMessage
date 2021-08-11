package com.example.mymessage.Fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mymessage.Adapters.PostAdapter;
import com.example.mymessage.Adapters.TopStatusAdapter;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Posts;
import com.example.mymessage.Models.Status;
import com.example.mymessage.Models.UserStatus;
import com.example.mymessage.Models.Users;
import com.example.mymessage.Notifications.MyApplication;
import com.example.mymessage.R;
import com.example.mymessage.databinding.FragmentStoryBinding;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class StoryFragments extends Fragment {

    private static final String TAG = Context.class.getName();

    private static final int NOTIFICATION_ID = 1;

    public StoryFragments() {
        // Required empty public constructor
    }

    FragmentStoryBinding binding;
    FirebaseDatabase database;
    FirebaseDatabase database1;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ArrayList<Posts> listPost = new ArrayList<>();
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
//    ArrayList<Friends> list = new ArrayList<>();
    ProgressDialog dialog;
    Users user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStoryBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);




        userStatuses = new ArrayList<>();
        statusAdapter = new TopStatusAdapter(getContext(), userStatuses);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.statusList.setLayoutManager(layoutManager);
        binding.statusList.setAdapter(statusAdapter);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.e(TAG, token);
                    }
                });



        database.getReference().child("stories").orderByChild("lastUpdated").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userStatuses.clear();
                    for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                        UserStatus status = new UserStatus();
                        status.setUserId(storySnapshot.child("userId").getValue(String.class));
                        status.setProfileImage(storySnapshot.child("profileImage").getValue(String.class));
                        status.setLastUpdated(storySnapshot.child("lastUpdated").getValue(Long.class));

                        ArrayList<Status> statuses = new ArrayList<>();

                        for (DataSnapshot statusSnapshot : storySnapshot.child("statuses").getChildren()) {
                            Status sampleStatus = statusSnapshot.getValue(Status.class);
                            statuses.add(sampleStatus);
                        }
                        status.setStatuses(statuses);

//                        for (int i = 0; i < list.size(); i++) {
//                            if (list.get(i).getNameFriend().equals(status.getName())) {
                        userStatuses.add(status);
//                            }
//                        }
                    }
                    Collections.reverse(userStatuses);

                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Calendar c1 = Calendar.getInstance();
        Date date = new Date();
        Long time = date.getTime();
        c1.setTime(date);
        c1.roll(Calendar.DATE, -1);



        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Users.class);
                        Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.ic_avatar).into(binding.profileImagePost);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        PostAdapter postAdapter = new PostAdapter(getContext(), listPost);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        binding.listPost.setLayoutManager(layoutManager1);
        binding.listPost.setAdapter(postAdapter);

        database.getReference().child("Posts").orderByChild("timePost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listPost.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Posts posts = dataSnapshot.getValue(Posts.class);
                    listPost.add(posts);
                }
                Collections.reverse(listPost);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




        binding.circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 75);
            }
        });


        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Posts posts = new Posts();
                Date date = new Date();
                posts.setUserPost(user.getUserName());
                posts.setUserIdPost(user.getUserId());
                posts.setTimePost(date.getTime());
                posts.setContentPost(binding.edtPost.getText().toString());
                posts.setFeeling(0);

                database.getReference().child("Posts").push().setValue(posts).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "On Successful", Toast.LENGTH_SHORT).show();
                        binding.edtPost.setText("");
                    }
                });

                sendNotification(binding.edtPost.getText().toString());
            }
        });


        return binding.getRoot();
    }

    private void sendNotification(String toString) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_send);
        Notification notification = new NotificationCompat.Builder(getContext(), MyApplication.CHANNEL_ID)
                .setContentText(toString)
                .setContentTitle(user.getUserName()+" post:")
                .setSmallIcon(R.drawable.ic_send)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLargeIcon(bitmap)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
        notificationManagerCompat.notify(getNotificationId(), notification);

//        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        if (notificationManager != null){
//            notificationManager.notify(getNotificationId(), notification);
//        }
    }

    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                dialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference = storage.getReference().child("status").child(date.getTime() + "");

                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setUserId(user.getUserId());
                                    userStatus.setProfileImage(user.getProfilepic());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("userId", userStatus.getUserId());
                                    obj.put("profileImage", userStatus.getProfileImage());
                                    obj.put("lastUpdated", userStatus.getLastUpdated());

                                    String imageUrl = uri.toString();
                                    Status status = new Status(imageUrl, userStatus.getLastUpdated());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference().child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);

                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}