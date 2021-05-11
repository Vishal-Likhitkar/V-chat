package com.appy.vchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appy.vchat.Activity.Chat_activity;
import com.appy.vchat.Modelclass.users;
import com.appy.vchat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class useradapter extends RecyclerView.Adapter<useradapter.Viewholder> {
    Context home_activity;
    ArrayList<users> usersArrayList;
    public useradapter(com.appy.vchat.Activity.home_activity home_activity, ArrayList<users> usersArrayList) {
        this.home_activity= home_activity;
        this.usersArrayList = usersArrayList;


    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(home_activity).inflate(R.layout.item_list_row,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        users user = usersArrayList.get(position);
        holder.user_name.setText(user.name);
        holder.user_status.setText(user.status);
        Picasso.get().load(user.imageuri).into(holder.profile_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_activity, Chat_activity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("ReceiverImage",user.getImageuri());
                intent.putExtra("uid",user.getUid());
                home_activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
    class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView profile_img;
        TextView user_name;
        TextView user_status;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            profile_img = itemView.findViewById(R.id.user_img);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);

        }
    }
}
