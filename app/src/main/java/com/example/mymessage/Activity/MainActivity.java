package com.example.mymessage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymessage.Adapters.FragmentsAdapter;
import com.example.mymessage.R;
import com.example.mymessage.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.mymessage.Function.RandomString.randomAlphaNumeric;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
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
                auth.signOut();
                Intent intent3 = new Intent(this, SignInActivity.class);
                startActivity(intent3);
                break;

        }
        return true;
    }
}