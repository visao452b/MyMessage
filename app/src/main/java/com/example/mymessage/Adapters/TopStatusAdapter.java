package com.example.mymessage.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymessage.Activity.MainActivity;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Status;
import com.example.mymessage.Models.UserStatus;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ItemStatusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

import static android.content.ContentValues.TAG;


public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewHolder> {

    Context context;
    ArrayList<UserStatus> userStatuses;
    ArrayList<Friends> friendsArrayList;
    FirebaseDatabase database;
    Users user;

    public TopStatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @NonNull
    @Override
    public TopStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false);
        return new TopStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStatusViewHolder holder, int position) {
        try {
            UserStatus userStatus = userStatuses.get(position);
            Status lastStatus = userStatus.getStatuses().get(userStatus.getStatuses().size() - 1);

            Glide.with(context).load(lastStatus.getImageUrl()).into(holder.binding.image);

            holder.binding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());

            String userId = userStatus.getUserId();
            database = FirebaseDatabase.getInstance();
            database.getReference().child("Users").child(userId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            user = snapshot.getValue(Users.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<MyStory> myStories = new ArrayList<>();
                    for (Status status : userStatus.getStatuses()) {
                        Date date = new Date(status.getTimeStamp());
                        myStories.add(new MyStory(status.getImageUrl(), date));
                    }
                    Collections.reverse(myStories);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
//                long time = posts.getTimePost();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

                    new StoryView.Builder(((MainActivity) context).getSupportFragmentManager())
                            .setStoriesList(myStories) // Required
                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                            .setTitleText(user.getUserName()) // Default is Hidden
                            .setSubtitleText("") // Default is Hidden
                            .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                            .setStoryClickListeners(new StoryClickListeners() {
                                @Override
                                public void onDescriptionClickListener(int position) {
                                    //your action
                                }

                                @Override
                                public void onTitleIconClickListener(int position) {
                                    //your action
                                }
                            })// Optional Listeners
                            .build() // Must be called before calling show method
                            .show();
                }
            });
        }catch (Exception e){
            Log.e(TAG, "ERROR", e);
        }



    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class TopStatusViewHolder extends RecyclerView.ViewHolder {

        ItemStatusBinding binding;

        public TopStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStatusBinding.bind(itemView);
        }
    }
}
