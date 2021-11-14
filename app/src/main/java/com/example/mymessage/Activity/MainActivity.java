package com.example.mymessage.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mymessage.Adapters.FragmentsAdapter;
import com.example.mymessage.Adapters.TopStatusAdapter;
import com.example.mymessage.Adapters.UsersAdapter;
import com.example.mymessage.Models.Friends;
import com.example.mymessage.Models.Users;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import static com.example.mymessage.Function.RandomString.randomAlphaNumeric;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    private long backPressedTime;

    private static final String TAG = Context.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");
        auth = FirebaseAuth.getInstance();

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.viewPager);

//        String idGroup = randomAlphaNumeric(16);

    }

    @Override
    protected void onResume() {
        super.onResume();
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

        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent1 = new Intent(this, SettingsActivity.class);
                startActivity(intent1);
                break;

            case R.id.findFriends:
                Intent intent4 = new Intent(this, FindFriends.class);
                startActivity(intent4);
                break;

            case R.id.createGroupChat:
                Intent intent2 = new Intent(this, CreateGroupChat.class);
                startActivity(intent2);
                break;
            case R.id.GroupChat:
                Intent intent5 = new Intent(this, ShowGroupChat.class);
                startActivity(intent5);
                break;

            case R.id.logout:
                database.getReference().child("Token").child(auth.getUid()).setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("Token", "onSuccess");
                    }
                });
                Intent intent3 = new Intent(this, LogoutActivity.class);
                startActivity(intent3);
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
//            super.onBackPressed();
            finishAffinity();
            return;
        } else {
            Toast.makeText(this, "Press back again to exit the application", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}