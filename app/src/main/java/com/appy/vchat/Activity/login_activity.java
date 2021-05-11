package com.appy.vchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appy.vchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {

    TextView sign_up;
    EditText login_email,loginn_pass;
    TextView btn;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Pleae wait....");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        sign_up = findViewById(R.id.sign_up);
        login_email = findViewById(R.id.login_email);
        loginn_pass = findViewById(R.id.login_pass);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        btn = findViewById(R.id.loginbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = login_email.getText().toString();
                String password = loginn_pass.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    progressDialog.dismiss();
                    Toast.makeText(login_activity.this,"Enter the valid data", Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern))
                {
                    progressDialog.dismiss();
                    login_email.setError("Invalid email");
                    Toast.makeText(login_activity.this,"Invalid email",Toast.LENGTH_SHORT).show();
                }
                  else if(password.length() < 6)
                {
                    progressDialog.dismiss();
                    loginn_pass.setError("Invalid password");
                    Toast.makeText(login_activity.this,"enter a valid password",Toast.LENGTH_SHORT).show();
                }
                  else
                {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                startActivity(new Intent(login_activity.this,home_activity.class));
                            } else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(login_activity.this,"Error in login",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_activity.this,Registration_activity.class));
            }
        });

    }
}