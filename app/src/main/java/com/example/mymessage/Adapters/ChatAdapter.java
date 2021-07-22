package com.example.mymessage.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymessage.Models.MessageModel;
import com.example.mymessage.R;
import com.example.mymessage.databinding.DeleteDialogBinding;
import com.example.mymessage.databinding.SampleReciverBinding;
import com.example.mymessage.databinding.SampleSenderBinding;
import com.github.pgreze.reactions.Reaction;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class ChatAdapter extends  RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;
    String recId, senderId;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId, String senderId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
        this.senderId = senderId;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewVolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciver, parent, false);
            return new ReceiverViewVolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageModels.get(position);
        if (messageModels.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String senderRoom = senderId+ recId;
        String receiverRoom = recId + senderId;
        MessageModel message = messageModels.get(position);

        int reactions[] = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            try {
                if(holder.getClass() == SenderViewVolder.class) {
                    SenderViewVolder viewHolder = (SenderViewVolder) holder;
                    viewHolder.binding.feelingSender.setImageResource(reactions[pos]);
                    viewHolder.binding.feelingSender.setVisibility(View.VISIBLE);
                } else {
                    ReceiverViewVolder viewHolder = (ReceiverViewVolder) holder;
                    viewHolder.binding.feelingReciver.setImageResource(reactions[pos]);
                    viewHolder.binding.feelingReciver.setVisibility(View.VISIBLE);


                }

                message.setFeeling(pos);

                FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child(message.getMessageId()).setValue(message);

                FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(receiverRoom)
                        .child(message.getMessageId()).setValue(message);

            }catch (Exception e){
                Log.e(TAG, "onBindViewHolder: ",e );
            }
            finally {
                return true; // true is closing popup, false is requesting a new selection
            }

        });


        if(holder.getClass() == SenderViewVolder.class) {
            SenderViewVolder viewHolder = (SenderViewVolder) holder;

            if(message.getMessage().equals("photo")) {
                viewHolder.binding.imageSender.setVisibility(View.VISIBLE);
                viewHolder.binding.senderText.setVisibility(View.GONE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.imageSender);
            }

            long time = message.getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            viewHolder.binding.senderTime.setText(dateFormat.format(new Date(time)));

            viewHolder.binding.senderText.setText(message.getMessage());

            if(message.getFeeling() >= 0) {
                viewHolder.binding.feelingSender.setImageResource(reactions[message.getFeeling()]);
                viewHolder.binding.feelingSender.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.feelingSender.setVisibility(View.GONE);
            }

            viewHolder.binding.senderText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });

            viewHolder.binding.imageSender.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            message.setFeeling(-1);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child(message.getMessageId()).setValue(message);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child(message.getMessageId()).setValue(message);
                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child(message.getMessageId()).setValue(null);
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

                    return false;
                }
            });
        } else {
            ReceiverViewVolder viewHolder = (ReceiverViewVolder) holder;
            if(message.getMessage().equals("photo")) {
                viewHolder.binding.imageReciver.setVisibility(View.VISIBLE);
                viewHolder.binding.reciverText.setVisibility(View.GONE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.imageReciver);
            }
            viewHolder.binding.reciverText.setText(message.getMessage());

            long time = message.getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            viewHolder.binding.reciverTime.setText(dateFormat.format(new Date(time)));

            if(message.getFeeling() >= 0) {
                //message.setFeeling(reactions[message.getFeeling()]);
                viewHolder.binding.feelingReciver.setImageResource(reactions[message.getFeeling()]);
                viewHolder.binding.feelingReciver.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.feelingReciver.setVisibility(View.GONE);
            }

            viewHolder.binding.reciverText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });

            viewHolder.binding.imageReciver.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            message.setFeeling(-1);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child(message.getMessageId()).setValue(message);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child(message.getMessageId()).setValue(message);
                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child(message.getMessageId()).setValue(null);
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

                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewVolder extends RecyclerView.ViewHolder {

        SampleReciverBinding binding;


        public ReceiverViewVolder(@NonNull View itemView) {
            super(itemView);

            binding = SampleReciverBinding.bind(itemView);


        }
    }

    public class SenderViewVolder extends RecyclerView.ViewHolder {
        SampleSenderBinding binding;
        public SenderViewVolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleSenderBinding.bind(itemView);
        }
    }

}
