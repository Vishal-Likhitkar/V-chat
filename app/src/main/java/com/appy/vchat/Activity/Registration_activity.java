package com.appy.vchat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appy.vchat.R;
import com.appy.vchat.Modelclass.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration_activity extends AppCompatActivity {

    TextView signin, signup_btn;
    EditText reg_name,reg_email,reg_pass,reg_cnmpass;
    CircleImageView profile_img;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri imageuri;
    String imageURI;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activity);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        signin = findViewById(R.id.sign_in);
        profile_img = findViewById(R.id.profile_img);
        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.reg_email);
        reg_pass = findViewById(R.id.reg_pass);
        reg_cnmpass= findViewById(R.id.reg_conmpass);
        signup_btn = findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String password = reg_pass.getText().toString();
                String con_password = reg_cnmpass.getText().toString();
                String status = "Hey there I'm using this Application";

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email)  || TextUtils.isEmpty(password) || TextUtils.isEmpty(con_password) )
                {
                    progressDialog.dismiss();
                    Toast.makeText(Registration_activity.this, "Enter the valid data",Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern))
                {
                    progressDialog.dismiss();
                     reg_email.setError("Enter the valid email");
                    Toast.makeText(Registration_activity.this,"Enter the valid email",Toast.LENGTH_SHORT).show();
                } else if(!password.equals(con_password))
                {
                    progressDialog.dismiss();
                    Toast.makeText(Registration_activity.this,"Password does not match",Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6)
                {
                    progressDialog.dismiss();
                    Toast.makeText(Registration_activity.this,"Enter 6 character password",Toast.LENGTH_SHORT).show();
                }
                else  {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());
                                if(imageuri!=null)
                                {
                                    storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                          if(task.isSuccessful())
                                          {
                                              storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                  @Override
                                                  public void onSuccess(Uri uri) {
                                                      imageURI=uri.toString();
                                                      users users = new users(auth.getUid(),name,email,imageURI,status);
                                                      reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<Void> task) {
                                                              if(task.isSuccessful())
                                                              {
                                                                  progressDialog.dismiss();
                                                                  startActivity(new Intent(Registration_activity.this,login_activity.class));
                                                              } else {
                                                                  Toast.makeText(Registration_activity.this,"Error in creating users",Toast.LENGTH_SHORT).show();
                                                              }

                                                          }
                                                      });
                                                  }
                                              });
                                          }
                                        }
                                    });
                                } else {
                                    String status = "Hey there I'm using this Application";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/v-chat-cfea9.appspot.com/o/pppppp.jfif?alt=media&token=46d70946-785b-4491-bb38-8a2add76536d";
                                    users users = new users(auth.getUid(),name,email,imageURI,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                startActivity(new Intent(Registration_activity.this,home_activity.class));
                                            } else {
                                                Toast.makeText(Registration_activity.this,"Error in creating users",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Registration_activity.this,"Something went wrong" ,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),10);

            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration_activity.this,login_activity.class));

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null) {
                imageuri = data.getData();
                profile_img.setImageURI(imageuri);
            }
        }
    }
}