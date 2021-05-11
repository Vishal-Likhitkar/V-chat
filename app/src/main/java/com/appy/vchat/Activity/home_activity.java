package com.appy.vchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appy.vchat.R;
import com.appy.vchat.Adapter.useradapter;
import com.appy.vchat.Modelclass.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class home_activity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView recyclerView;
    useradapter adapter;
    FirebaseDatabase database;
    ArrayList<users> usersArrayList;
    ImageView imglogout;
    ImageView settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activity);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        usersArrayList = new ArrayList<>();
        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(home_activity.this,Registration_activity.class));
        }
        Toast.makeText(this, "Data is Fetching...", Toast.LENGTH_SHORT).show();

        imglogout = findViewById(R.id.logout);
        settings = findViewById(R.id.settings);
        DatabaseReference reference = database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    users user = dataSnapshot.getValue(users.class);
                    usersArrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(home_activity.this,R.style.Dialoge);
                dialog.setContentView(R.layout.dialog_layout);
                TextView btnno, btnyes;
                btnno = dialog.findViewById(R.id.no);
                btnyes = dialog.findViewById(R.id.yes);
                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(home_activity.this,login_activity.class));
                    }
                });
                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new useradapter(home_activity.this,usersArrayList);
        recyclerView.setAdapter(adapter);


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home_activity.this,Settings.class));
            }
        });
    }
}