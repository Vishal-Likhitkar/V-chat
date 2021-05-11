package com.appy.vchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appy.vchat.Adapter.MessageAdapter;
import com.appy.vchat.Modelclass.Messages;
import com.appy.vchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_activity extends AppCompatActivity {
      String ReceiverImage,ReceiverUID,SenderUID,ReceiverName;
      CircleImageView profileimg;
      TextView receivname;
      FirebaseAuth firebaseAuth;
      FirebaseDatabase database;
      public static String simage;
      public static String rimage;
      CardView sendbtn;
      EditText etdmessage;
      MessageAdapter adapter;
      String senderroom,receiverroom;
      RecyclerView messageadapter;
      ArrayList<Messages> messagesArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ReceiverName = getIntent().getStringExtra("name");
        ReceiverImage = getIntent().getStringExtra("ReceiverImage");
        ReceiverUID = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();

        profileimg = findViewById(R.id.profile_img);
        receivname = findViewById(R.id.receivername);
        messageadapter = findViewById(R.id.messageadapter);

        messagesArrayList = new ArrayList<>();
        adapter = new MessageAdapter(Chat_activity.this,messagesArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageadapter.setLayoutManager(linearLayoutManager);
        messageadapter.setAdapter(adapter);

        sendbtn = findViewById(R.id.sendbtn);
        etdmessage = findViewById(R.id.edtmessage);

        Picasso.get().load(ReceiverImage).into(profileimg);
        receivname.setText(" " + ReceiverName);

        SenderUID = firebaseAuth.getUid();

        senderroom = SenderUID + ReceiverUID;
        receiverroom = ReceiverUID + SenderUID;

        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderroom).child("messages");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               simage = snapshot.child("imageuri").getValue().toString();
               rimage = ReceiverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message =etdmessage.getText().toString();
                if(message.isEmpty())
                {
                    Toast.makeText(Chat_activity.this,"Please enter valid Message",Toast.LENGTH_SHORT).show();
                    return;
                }
                etdmessage.setText("");
                Date date = new Date();
                Messages messages = new Messages(message,SenderUID,date.getTime());

                database.getReference().child("chats")
                        .child(senderroom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(receiverroom)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                    }
                });
            }
        });
    }
}