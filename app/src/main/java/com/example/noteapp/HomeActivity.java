package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.Fragment.HomeScreenFragment;

public class HomeActivity extends AppCompatActivity {
    String uuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            uuid = extras.getString("uuid");
        }

        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid );

        Fragment fragment = new HomeScreenFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed()
    {

    }


}