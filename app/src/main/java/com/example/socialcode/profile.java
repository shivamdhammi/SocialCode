package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class profile extends AppCompatActivity{
    private DrawerLayout drawer;
    private LinearLayout logout;
    private ImageView retrive;
//    public String rating="";
    home Home;
    updateProfile UpdateProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        retrive = findViewById(R.id.profile_retrive);
//        retrive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                retriveData();
//            }
//        });
//        Toast.makeText(getApplicationContext(),getIntent().getExtras().getString("rating"),Toast.LENGTH_LONG).show();
        Home = new home();
        UpdateProfile = new updateProfile();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                Home).commit();
                        populate();
                        break;
                    case R.id.nav_upadteprofile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                UpdateProfile).commit();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,Home).commit();
            populate();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        logout = findViewById(R.id.nav_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                eraseDataFromSharedPrefrence();
                finish();
                startActivity(new Intent(getApplicationContext(),login.class));
                Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_LONG).show();
            }
        });
    }

//    @Override
//    public void onInputHomeSent(String input) {
//
//    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void populate(){
        Bundle args = new Bundle();
        String rating = "";
        try {
            rating = getIntent().getExtras().getString("rating");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!rating.isEmpty()){
            args.putString("rating",rating);
            Home.setArguments(args);
        }
    }

    public void eraseDataFromSharedPrefrence(){
        SharedPreferences sharedPref=getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString("Email","");
        editor.putString("Password","");
        // editor.putString("State",state);
        editor.commit();
    }


}
