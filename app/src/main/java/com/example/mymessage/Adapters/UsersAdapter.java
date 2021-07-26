package com.example.mymessage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessage.Activity.ChatDetailActivity;
import com.example.mymessage.Activity.MainActivity;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{


    ArrayList<Friends> list;
    Context context;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public UsersAdapter(ArrayList<Friends> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {

        Friends friend = list.get(position);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Picasso.get().load(friend.getProfilepic()).placeholder(R.drawable.ic_avatar).into(holder.image);
        holder.userName.setText(friend.getNameFriend());

        database.getReference().child("presence").child(friend.getFriendId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if(!status.isEmpty()) {
                        if(status.equals("Offline")) {
                            holder.status.setVisibility(View.GONE);
                        } else {
                            holder.status.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid() + friend.getFriendId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                holder.lassMessage.setText(snapshot1.child("message").getValue().toString());
                                long time = snapshot1.child("timestamp").getValue(Long.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aaa");
                                holder.msgTime.setText(dateFormat.format(new Date(time)));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", friend.getFriendId());
                intent.putExtra("profilePic", friend.getProfilepic());
                intent.putExtra("userName", friend.getNameFriend());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, status;
        TextView userName, lassMessage, msgTime;


        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNameList);
            lassMessage = itemView.findViewById(R.id.lastMessage);
            msgTime = itemView.findViewById(R.id.msgTime);
            status = itemView.findViewById(R.id.statusUser);
        }
    }


}
