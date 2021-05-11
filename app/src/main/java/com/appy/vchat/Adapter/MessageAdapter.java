package com.appy.vchat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appy.vchat.Modelclass.Messages;
import com.appy.vchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.appy.vchat.Activity.Chat_activity.rimage;
import static com.appy.vchat.Activity.Chat_activity.simage;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECIEV=2;

    public MessageAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND)
        {
           View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_ptn,parent,false);
           return  new SenderViewholder(view);
        } else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_ptn,parent,false);
            return  new ReceiverViewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);

        if(holder.getClass()== SenderViewholder.class)
        {
            SenderViewholder viewholder = (SenderViewholder) holder;

            viewholder.txtmsg.setText(messages.getMessage());
            Picasso.get().load(simage).into(viewholder.cirprofileimg);
        } else
        {
            ReceiverViewholder viewholder = (ReceiverViewholder) holder;

            viewholder.txtmsg.setText(messages.getMessage());
            Picasso.get().load(rimage).into(viewholder.cirprofileimg);
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderID()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIEV;
        }
    }

    class SenderViewholder extends RecyclerView.ViewHolder {
        CircleImageView cirprofileimg;
        TextView txtmsg;

        public SenderViewholder(@NonNull View itemView) {
            super(itemView);
            cirprofileimg = itemView.findViewById(R.id.profile_img);
            txtmsg = itemView.findViewById(R.id.txtmessage);
        }
    }
    class ReceiverViewholder extends RecyclerView.ViewHolder {
        CircleImageView cirprofileimg;
        TextView txtmsg;

        public ReceiverViewholder(@NonNull View itemView) {
            super(itemView);
            cirprofileimg = itemView.findViewById(R.id.profile_img);
            txtmsg = itemView.findViewById(R.id.txtmessage);
        }
    }
}
