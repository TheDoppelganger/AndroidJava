package com.example.androidjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidjava.CustomerFragment.CustomerJobForJobless;
import com.example.androidjava.CustomerFragment.CustomerMyCart;
import com.example.androidjava.CustomerFragment.CustomerMyFavourite;
import com.example.androidjava.CustomerFragment.CustomerOrderHistory;
import com.example.androidjava.CustomerFragment.CustomerReferEarn;
import com.example.androidjava.CustomerFragment.CustomerShopNearMe;
import com.example.androidjava.CustomerFragment.CustomerSuggestEarn;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.SellerFragment.SellerProfileCustomer;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class CustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar mtoolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView imgViewCart,imgChangeLocation;
    private Gson gson;
    private mUser muser;
    private ToggleButton btnSwitchToCustoemer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        sharedPreferences = getApplicationContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        drawerLayout = findViewById(R.id.main_drawer_customer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        gson = new Gson();
        String user = sharedPreferences.getString("user", null);
        muser = gson.fromJson(user, mUser.class);


        ImageView imageView = findViewById(R.id.open_drawer);
        ImageView imageViewFav = findViewById(R.id.toolBar_menu_favouite);

        NavigationView navigationView = findViewById(R.id.main_nav_view_customer);
        View headerView=navigationView.getHeaderView(0);
        btnSwitchToCustoemer = headerView.findViewById(R.id.btn_switch_to_customer_account_customer_profile);

        imgViewCart = findViewById(R.id.cart_customer_tool_bar);
        imgChangeLocation=findViewById(R.id.location_customer_tool_bar);

        imgChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CustomerActivity.class.getSimpleName().equals("CustomerActivity")) {
                    Intent io=new Intent(CustomerActivity.this,GoogleMap.class);
                    String shop=sharedPreferences.getString("allShop","123");
                    io.putExtra("Activity",shop);
                    startActivity(io);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        imageViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CustomerActivity.class.getSimpleName().equals("CustomerActivity")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerMyFavourite()).addToBackStack(null).commit();
                }
            }
        });

        imgViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CustomerActivity.class.getSimpleName().equals("CustomerActivity")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerMyCart()).addToBackStack(null).commit();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        checkDriverToCustomer();

        if (muser.getUser_type().equals("Driver")) {
            btnSwitchToCustoemer.setVisibility(View.VISIBLE);
            String name=getClass().getSimpleName();
            if(name.equals("CustomerActivity")){
                btnSwitchToCustoemer.setChecked(true);
            }
        }else if(muser.getUser_type().equals("Seller")){
            btnSwitchToCustoemer.setTextOn("Switch To Seller Account");
            btnSwitchToCustoemer.setVisibility(View.VISIBLE);
            String name=getClass().getSimpleName();
            if(name.equals("CustomerActivity")){
                btnSwitchToCustoemer.setChecked(true);
            }
        }
        btnSwitchToCustoemer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    muser.setIsSwitch("1");
                    checkDriverToCustomer();
                    startActivity(new Intent(getApplicationContext(),CustomerActivity.class));
                } else {
                    if(muser.getUser_type().equals("Driver")) {
                        muser.setIsSwitch("0");
                        checkDriverToCustomer();
                        startActivity(new Intent(getApplicationContext(), DeliveryPartner.class));
                    }else if(muser.getUser_type().equals("Seller")){
                        muser.setIsSwitch("0");
                        checkDriverToCustomer();
                        startActivity(new Intent(getApplicationContext(), SellerActivity.class));
                    }
                }
            }
        });

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerShopNearMe()).commit();
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
        switch (menuItem.getItemId()) {
            case R.id.customer_menu_after_log_myaccount:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new SellerProfileCustomer()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.customer_menu_after_log_mycart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerMyCart()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.customer_menu_after_log_myfavourite:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerMyFavourite()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.customer_menu_after_log_referearn:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerReferEarn()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.customer_menu_after_log_suggest_earn:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerSuggestEarn()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.customer_menu_after_log_job_for_jobless:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerJobForJobless()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.customer_menu_after_order_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerOrderHistory()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.customer_menu_after_log_out:
                editor.remove("user");
                editor.remove("seller");
                editor.commit();
                startActivity(new Intent(CustomerActivity.this, LogIn.class));
                finish();
                break;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    private void checkDriverToCustomer() {
        String user=gson.toJson(muser);
        editor.putString("user",user);
        editor.commit();
    }
}
