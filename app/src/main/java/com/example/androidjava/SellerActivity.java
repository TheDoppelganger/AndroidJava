package com.example.androidjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.SellerFragment.SellerAllProduct;
import com.example.androidjava.SellerFragment.SellerAllProductPending;
import com.example.androidjava.SellerFragment.SellerDashboard;
import com.example.androidjava.SellerFragment.SellerMyEarning;
import com.example.androidjava.SellerFragment.SellerNewOrder;
import com.example.androidjava.SellerFragment.SellerOfflineBilling;
import com.example.androidjava.SellerFragment.SellerOnlineBilling;
import com.example.androidjava.SellerFragment.SellerProfile;
import com.google.android.gms.common.api.Api;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SellerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mtoolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ToggleButton toggleOpenClose;
    private Gson gson;
    private mSeller seller;
    private ToggleButton btnSwitchToCustoemer;
    private mUser muser;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        sharedPreferences = getApplicationContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        gson = new Gson();
        String seller1 = sharedPreferences.getString("seller", "");
        seller = gson.fromJson(seller1, mSeller.class);
        String user = sharedPreferences.getString("user", null);
        muser = gson.fromJson(user, mUser.class);
        drawerLayout = findViewById(R.id.main_drawer_seller);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ImageView imageView = findViewById(R.id.open_drawer);
        ImageView imageViewFav = findViewById(R.id.toolBar_menu_favouite);
        NavigationView navigationView = findViewById(R.id.main_nav_view_seller);
        View headerView=navigationView.getHeaderView(0);
        btnSwitchToCustoemer = headerView.findViewById(R.id.btn_switch_to_customer_account_customer_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        toggleOpenClose = navigationView.getMenu().findItem(R.id.online_offfline_shop_status).getActionView().findViewById(R.id.toggle_menu_online_offline);
        toggleOpenClose.setTextOn("Open");
        toggleOpenClose.setTextOff("Close");
        checkOpenClose();
        toggleOpenClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    toggleOpenClose.setBackgroundResource(R.drawable.button_menu_online);
                    seller.setIsOpen("1");
                    checkOpenClose();
                    new UpdateOpenClose().execute();
                    Toast.makeText(getApplicationContext(), "You are now Online", Toast.LENGTH_LONG).show();
                } else {
                    toggleOpenClose.setBackgroundResource(R.drawable.button_menu_offline);
                    seller.setIsOpen("0");
                    checkOpenClose();
                    new UpdateOpenClose().execute();
                    Toast.makeText(getApplicationContext(), "You are now Offline", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                    if(muser.getUser_type().equals("driver")) {
                        muser.setIsSwitch("0");
                        checkDriverToCustomer();
                        startActivity(new Intent(getApplicationContext(), DeliveryPartner.class));
                    }else if(muser.getUser_type().equals("seller")){
                        muser.setIsSwitch("0");
                        checkDriverToCustomer();
                        startActivity(new Intent(getApplicationContext(), SellerActivity.class));
                    }
                }
            }
        });
        new CacheShopProduct().execute();
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new SellerDashboard()).commit();
    }

    private void checkOpenClose() {
        String driver = gson.toJson(seller);
        editor.putString("seller", driver);
        editor.commit();
        if (seller.getIsOpen().equals("1")) {
            toggleOpenClose.setBackgroundResource(R.drawable.button_menu_online);
            toggleOpenClose.setChecked(true);
        } else {
            toggleOpenClose.setBackgroundResource(R.drawable.button_menu_offline);
            toggleOpenClose.setChecked(false);
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
        switch (menuItem.getItemId()) {
            case R.id.menu_after_log_log_out:
                editor.clear();
                editor.commit();
                startActivity(new Intent(SellerActivity.this, LogIn.class));
                break;
            case R.id.menu_after_log_myproduct:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new SellerAllProduct()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.menu_after_log_myorder:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new SellerNewOrder()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_after_log_offline_billing:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new SellerOfflineBilling()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_after_log_online_billing:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new SellerOnlineBilling()).addToBackStack(null).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_after_log_myearning:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new SellerMyEarning()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.online_offfline_shop_status:
                if (toggleOpenClose.isChecked()) {
                    seller.setIsOpen("0");
                    checkOpenClose();
                    new UpdateOpenClose().execute();
                    Toast.makeText(getApplicationContext(), "You Shop is now Off", Toast.LENGTH_LONG).show();
                } else {
                    seller.setIsOpen("1");
                    checkOpenClose();
                    new UpdateOpenClose().execute();
                    Toast.makeText(getApplicationContext(), "You shop is now On", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_after_log_shopprofile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new SellerProfile()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
    class UpdateOpenClose extends AsyncTask<Void,Void,String>{

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("operation", "Shop_Online/Offline"));
            list.add(new BasicNameValuePair("status", seller.getIsOpen()));
            list.add(new BasicNameValuePair("shopId", seller.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.driverSimpleRequest, list);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    class CacheShopProduct extends  AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("productType","PendingProduct"));
            list.add(new BasicNameValuePair("shopId",seller.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.fetchProduct,list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getApplicationContext(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            }else {
                editor.putString("pendingProduct", s);
                editor.commit();
            }
            new CacheShopPublishedProduct().execute();
        }
    }
    class CacheShopPublishedProduct extends AsyncTask<Void,Void,String>
    {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("productType","PublishedProduct"));
            list.add(new BasicNameValuePair("shopId",seller.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.fetchProduct,list);
        }
        @Override
        protected void onPostExecute(String s) {
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getApplicationContext(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            }else {
                editor.putString("publishedProduct", s);
                editor.commit();
            }
        }
    }
    private void checkDriverToCustomer() {
        String user=gson.toJson(muser);
        editor.putString("user",user);
        editor.commit();
    }
}
