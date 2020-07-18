package com.example.androidjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidjava.CustomerFragment.CustomerRegistration;
import com.example.androidjava.SellerFragment.SellerRegistration2;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar mtoolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Intent io;
    String latitude, longitude;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getApplicationContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        drawerLayout = findViewById(R.id.main_drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ImageView imageView = findViewById(R.id.open_drawer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        io = getIntent();
        if (io.getStringExtra("lat") != null && io.getStringExtra("lang") != null) {
            latitude = io.getStringExtra("lat");
            longitude = io.getStringExtra("lang");
            SellerRegistration2 sellerRegistration2 = new SellerRegistration2();
            Bundle b = new Bundle();
            b.putString("lat", latitude);
            b.putString("lang", longitude);
            sellerRegistration2.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, sellerRegistration2).commit();
        } else {
            if (savedInstanceState == null) ;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CustomerRegistration()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
