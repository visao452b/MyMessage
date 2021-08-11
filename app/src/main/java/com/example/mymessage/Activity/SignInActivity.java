package com.example.mymessage.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;


    private static final String TAG = Context.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtEmailSignIn.getText().toString().isEmpty()){
                    binding.edtEmailSignIn.setError("Enter your email");
                    return;
                }

                if (binding.edtPasswordSignIn.getText().toString().isEmpty()){
                    binding.edtPasswordSignIn.setError("Enter your password");
                    return;
                }


                if(!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmailSignIn.getText().toString()).matches()){
                    binding.edtEmailSignIn.setError("Please enter a valid email!");
                    binding.edtEmailSignIn.requestFocus();
                    return;
                }

                if (binding.edtPasswordSignIn.getText().toString().length()<6){
                    binding.edtPasswordSignIn.setError("Min password length should be 6 characters!");
                    binding.edtPasswordSignIn.requestFocus();
                    return;
                }

                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.edtEmailSignIn.getText().toString().trim(), binding.edtPasswordSignIn.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                 if (task.isSuccessful()){
                                     FirebaseMessaging.getInstance().getToken()
                                             .addOnCompleteListener(new OnCompleteListener<String>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<String> task) {
                                                     if (!task.isSuccessful()) {
                                                         Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                         return;
                                                     }

                                                     // Get new FCM registration token
                                                     String token = task.getResult();



                                                     // Log and toast
                                                     Log.e(TAG, token);
                                                     database.getReference().child("Token").child(auth.getUid()).setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                         @Override
                                                         public void onSuccess(Void unused) {
                                                             Log.e("Token", "onSuccess");
                                                         }
                                                     });
                                                 }
                                             });

                                     Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                     startActivity(intent);
                                 }else {
                                     Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                 }
                            }
                        });


            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });

        binding.clickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        if (auth.getCurrentUser()!= null){
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    int RC_SIGN_IN = 65;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
//                            updateUI(user);
                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());

                            database.getReference().child("Users").child(user.getUid()).setValue(users);



                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignInActivity.this, "Sign in with Google", Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            updateUI(null);

                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Snackbar.make(binding.getRoot(), "Authentication Failed.",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}