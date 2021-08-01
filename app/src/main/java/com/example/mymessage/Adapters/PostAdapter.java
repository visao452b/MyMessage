package com.example.mymessage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessage.Activity.Profile;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Posts;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.SamplePostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context contex;
    ArrayList<Posts> listPosts;

    Users users;

    FirebaseDatabase database;

    public PostAdapter(Context contex, ArrayList<Posts> listPosts) {
        this.contex = contex;
        this.listPosts = listPosts;
    }

    @NonNull
    @NotNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contex).inflate(R.layout.sample_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostAdapter.PostViewHolder holder, int position) {
        database =FirebaseDatabase.getInstance();
        Posts posts = listPosts.get(position);

        if (posts.getUserIdPost()!= null){
            database.getReference().child("Users").child(posts.getUserIdPost()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_avatar).into(holder.binding.profileImageShowUserPost);
                    holder.binding.userNamePost.setText(users.getUserName());
                    long time = posts.getTimePost();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                    holder.binding.timePostStatus.setText(dateFormat.format(new Date(time)));
                    holder.binding.contentPost.setText(posts.getContentPost());
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    holder.binding.profileImageShowUserPost.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentP = new Intent(contex, Profile.class);
            intentP.putExtra("uId", posts.getUserIdPost());
            contex.startActivity(intentP);
        }
    });





    }

    @Override
    public int getItemCount() {
        return listPosts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        SamplePostBinding binding;

        public PostViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            binding = SamplePostBinding.bind(itemView);
        }
    }
}
