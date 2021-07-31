package com.example.mymessage.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class ChangePassword extends AppCompatActivity {
    ActivityChangePasswordBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    Users user;
    ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(ChangePassword.this);
        progressDialog.setTitle("Change Password");
        progressDialog.setMessage("Changing Your Password");

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String email = firebaseUser.getEmail();


        database.getReference().child("Users").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Users.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.backChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
//        String pass = user.getPassword();
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtCurrentPassword.getText().toString().isEmpty()){
                    binding.edtCurrentPassword.setError("Enter your current password");
                    return;
                }

                if (binding.edtNewPassword.getText().toString().isEmpty()){
                    binding.edtNewPassword.setError("Enter your new password");
                    return;
                }

                if (binding.edtConfirmPassword.getText().toString().isEmpty()){
                    binding.edtConfirmPassword.setError("Enter your confirm password");
                    return;
                }

                if (binding.edtNewPassword.getText().toString().length()<6){
                    binding.edtNewPassword.setError("Min password length should be 6 characters!");
                    binding.edtNewPassword.requestFocus();
                    return;
                }
                if (!binding.edtConfirmPassword.getText().toString().equals(binding.edtNewPassword.getText().toString())){
                    binding.edtConfirmPassword.setError("Passwords are not the same");
                    return;
                }

                progressDialog.show();

                AuthCredential credential = EmailAuthProvider.getCredential(email,binding.edtCurrentPassword.getText().toString());
                firebaseUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    firebaseUser.updatePassword(binding.edtConfirmPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                binding.edtCurrentPassword.setText("");
                                                binding.edtNewPassword.setText("");
                                                binding.edtConfirmPassword.setText("");
                                                Toast.makeText(ChangePassword.this, "Password updated successful", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "Password updated");
                                            } else {
                                                Toast.makeText(ChangePassword.this, "Password updated failed", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "Error password not updated");
                                            }
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "Error auth failed");
                                    binding.edtCurrentPassword.setError("Entered wrong password");
                                    binding.edtCurrentPassword.requestFocus();
                                }
                            }
                        });
            }
        });



    }
}