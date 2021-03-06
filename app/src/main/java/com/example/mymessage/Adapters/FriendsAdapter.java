package com.example.mymessage.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessage.Activity.ChatDetailActivity;
import com.example.mymessage.Activity.Profile;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.R;
import com.example.mymessage.databinding.DeleteChatBinding;
import com.example.mymessage.databinding.DeleteFriendBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

    public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolderFriends>{

    ArrayList<Friends> list;
    Context context;

    FirebaseDatabase database;
    DatabaseReference reference, reference2;
    FirebaseAuth auth;

    public FriendsAdapter(ArrayList<Friends> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderFriends onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_friend, parent, false);

        return new ViewHolderFriends(view);
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolderFriends holder, int position) {

//        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        Friends friends = list.get(position);
        Picasso.get().load(friends.getProfilepic()).placeholder(R.drawable.ic_avatar).into(holder.image);
        holder.userName.setText(friends.getNameFriend());
        holder.email.setText(friends.getEmailFriend());

        holder.removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.delete_friend, null);
                DeleteFriendBinding binding = DeleteFriendBinding.bind(view);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Chat")
                        .setView(binding.getRoot())
                        .create();
                binding.deleteFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reference =FirebaseDatabase.getInstance().getReference()
                                .child("Friends")
                                .child(uId)
                                .child(friends.getFriendId());
                        reference2 =FirebaseDatabase.getInstance().getReference()
                                .child("Friends")
                                .child(friends.getFriendId())
                                .child(uId);
                        reference.removeValue();
                        reference2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "X??a b???n b?? th??nh c??ng!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();

                    }
                });
                binding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();



            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentP = new Intent(context, Profile.class);
                intentP.putExtra("uId", friends.getFriendId());
                context.startActivity(intentP);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderFriends extends RecyclerView.ViewHolder{

        ImageView image, removeFriend;
        TextView userName, email;
        public ViewHolderFriends(View itemView) {
            super(itemView);

            removeFriend = itemView.findViewById(R.id.removeFriend);
            image = itemView.findViewById(R.id.profile_imageShowFriend);
            userName = itemView.findViewById(R.id.userNameListShowFriend);
            email = itemView.findViewById(R.id.emailShowFriend);
        }
    }


}
