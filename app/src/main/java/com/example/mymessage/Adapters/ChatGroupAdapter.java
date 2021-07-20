package com.example.mymessage.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessage.Models.MessageModel;
import com.example.mymessage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatGroupAdapter extends  RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatGroupAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatGroupAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender_group, parent, false);
            return new SenderViewVolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciver_group, parent, false);
            return new ReceiverViewVolder(view);
        }


    }

    @Override
    public int getItemViewType(int position) {

        if (messageModels.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);
//        Date date = new Date();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want delete this message")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database= FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messageModel.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });

        if (holder.getClass() == SenderViewVolder.class){
            ((SenderViewVolder)holder).senderMsg.setText(messageModel.getMessage());
            ((SenderViewVolder)holder).nameSender.setText(messageModel.getSenderId());

//            long time = messageModel.getTimestamp();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//            ((SenderViewVolder)holder).senderMsg.setText(dateFormat.format(new Date(time)));
        }else {
            ((ReceiverViewVolder)holder).receverMsg.setText(messageModel.getMessage());
            ((ReceiverViewVolder)holder).nameReciver.setText(messageModel.getSenderId());
//            long time =messageModel.getTimestamp();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//            ((ReceiverViewVolder)holder).receiverTime.setText(dateFormat.format(new Date(time)));
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewVolder extends RecyclerView.ViewHolder {

        TextView receverMsg, nameReciver;


        public ReceiverViewVolder(@NonNull View itemView) {
            super(itemView);

            receverMsg = itemView.findViewById(R.id.reicverTextChatGroup);
            nameReciver = itemView.findViewById(R.id.nameReciverChatGroup);


        }
    }

    public class SenderViewVolder extends RecyclerView.ViewHolder {

        TextView senderMsg, nameSender;

        public SenderViewVolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderTextChatGroup);
            nameSender = itemView.findViewById(R.id.nameSenderChatGroup);

        }
    }

}
