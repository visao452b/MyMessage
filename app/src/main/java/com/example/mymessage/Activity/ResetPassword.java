package com.example.mymessage.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityResetPasswordBinding;
import com.example.mymessage.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(ResetPassword.this);
        progressDialog.setTitle("Reset Password Account");
        progressDialog.setMessage("We are reset password your account");

        binding.btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = binding.edtEmailResetPassword.getText().toString().trim();

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPassword.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(ResetPassword.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }
}