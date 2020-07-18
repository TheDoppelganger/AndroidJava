package com.example.androidjava;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.BroadcastReceiver.UpdateDeliveryPartnerLocation;
import com.example.androidjava.CustomerFragment.CustomerJobForJobless;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DriverFragment.CommonHelpCenter;
import com.example.androidjava.DriverFragment.DriverDashboard;
import com.example.androidjava.DriverFragment.DriverEarning;
import com.example.androidjava.DriverFragment.DriverKnowMoreRating;
import com.example.androidjava.DriverFragment.DriverMyTask;
import com.example.androidjava.DriverFragment.DriverProfile;
import com.example.androidjava.DriverFragment.DriverSetting;
import com.example.androidjava.Model.mDriver;
import com.example.androidjava.Model.mUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPartner extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static DeliveryPartner instance;
    Toolbar mtoolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    private SharedPreferences sharedPreferences;
    String latitude, longitude;
    String driver;
    mDriver mdriver;
    private SharedPreferences.Editor editor;
    private ToggleButton btnSwitchToCustoemer;
    public static DeliveryPartner getInstance() {
        return instance;
    }
    private mUser muser;
    private Switch aSwitch;
    private Gson gson;
    private ToggleButton toggleOnlineOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_partner);
        instance = this;
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        gson = new Gson();
        drawerLayout = findViewById(R.id.main_drawer_deliver_partner);
        sharedPreferences = getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        driver = sharedPreferences.getString("driver", null);
        Gson gson = new Gson();
        mdriver = gson.fromJson(driver, mDriver.class);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        String user = sharedPreferences.getString("user", null);
        muser = gson.fromJson(user, mUser.class);
        ImageView imageView = findViewById(R.id.open_drawer);
        NavigationView navigationView = findViewById(R.id.main_nav_view_deliver_partner);
        View headerView=navigationView.getHeaderView(0);
        btnSwitchToCustoemer = headerView.findViewById(R.id.btn_switch_to_customer_account_customer_profile);

        TextView txtKnowMoreRating=headerView.findViewById(R.id.txt_know_more_about_rating_header_navigation);
        txtKnowMoreRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverKnowMoreRating()).commit();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        toggleOnlineOffline = navigationView.getMenu().findItem(R.id.menu_delivery_partner_online_offline).getActionView().findViewById(R.id.toggle_menu_online_offline);
        aSwitch = navigationView.getMenu().findItem(R.id.show_secure).getActionView().findViewById(R.id.drawer_switch_auto_accepted);
        checkAutoAccpetedOnOff();
        checkOnlineOffline();
        toggleOnlineOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    toggleOnlineOffline.setBackgroundResource(R.drawable.button_menu_online);
                    mdriver.setIsAvailable("1");
                    checkOnlineOffline();
                    new UpdateOnlineOffline().execute();
                    Toast.makeText(getApplicationContext(), "You are now Online", Toast.LENGTH_LONG).show();
                } else {
                    toggleOnlineOffline.setBackgroundResource(R.drawable.button_menu_offline);
                    mdriver.setIsAvailable("0");
                    checkOnlineOffline();
                    new UpdateOnlineOffline().execute();
                    Toast.makeText(getApplicationContext(), "You are now Offline", Toast.LENGTH_LONG).show();
                }
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mdriver.setIsAutoAccepted("1");
                    checkAutoAccpetedOnOff();
                    new UpdateAutoAccepted().execute();
                    Toast.makeText(getApplicationContext(), "Auto Accepted On", Toast.LENGTH_LONG).show();
                } else {
                    mdriver.setIsAutoAccepted("0");
                    checkAutoAccpetedOnOff();
                    new UpdateAutoAccepted().execute();
                    Toast.makeText(getApplicationContext(), "Auto Accepted Off", Toast.LENGTH_LONG).show();
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
        if (savedInstanceState == null) ;
        getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverDashboard()).commit();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        upadateLocation();
                        countDownTime();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(DeliveryPartner.this, "You must Accepted this Permisson", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void upadateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_LONG).show();
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, UpdateDeliveryPartnerLocation.class);
        intent.setAction(UpdateDeliveryPartnerLocation.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setSmallestDisplacement(10f);
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
            case R.id.menu_delivery_partner_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverDashboard()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_delivery_partner_my_task:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverMyTask()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_delivery_partner_my_earning:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverEarning()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_delivery_partner_my_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverProfile()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_delivery_partner_log_out:
                editor.remove("driver");
                editor.remove("user");
                editor.commit();
                startActivity(new Intent(DeliveryPartner.this, LogIn.class));
                break;
            case R.id.show_secure:
                if (aSwitch.isChecked()) {
                    mdriver.setIsAutoAccepted("0");
                    checkAutoAccpetedOnOff();
                    new UpdateAutoAccepted().execute();
                    Toast.makeText(getApplicationContext(), "Auto Accepted Off", Toast.LENGTH_LONG).show();
                } else {
                    mdriver.setIsAutoAccepted("1");
                    checkAutoAccpetedOnOff();
                    new UpdateAutoAccepted().execute();
                    Toast.makeText(getApplicationContext(), "Auto Accepted On", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_delivery_partner_online_offline:
                if (toggleOnlineOffline.isChecked()) {
                    mdriver.setIsAvailable("0");
                    checkOnlineOffline();
                    new UpdateOnlineOffline().execute();
                    Toast.makeText(getApplicationContext(), "You are now Offline", Toast.LENGTH_LONG).show();
                } else {
                    mdriver.setIsAvailable("1");
                    checkOnlineOffline();
                    new UpdateOnlineOffline().execute();
                    Toast.makeText(getApplicationContext(), "You are now Online", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_delivery_partner_job_for_jobless:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new CustomerJobForJobless()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_delivery_partner_setting:
     /*           getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverSetting()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);*/
                break;
            case R.id.menu_delivery_partner_Helpcenter:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new CommonHelpCenter()).commit();
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

    public void updateLatLong(final String lat, final String longi) {
        DeliveryPartner.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                latitude = lat;
                longitude = longi;
                new UpdateDeliverPartnerLocationToDatabase().execute();
            }
        });
    }

    private void countDownTime() {
        final CountDownTimer countDownTimer = new CountDownTimer(500000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                upadateLocation();
            }

            @Override
            public void onFinish() {
                countDownTime();
            }
        }.start();
    }

    class UpdateDeliverPartnerLocationToDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            mdriver.setLatitude(latitude);
            mdriver.setLongitude(longitude);
            String driver = gson.toJson(mdriver);
            editor.putString("driver", driver);
            editor.commit();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();

            if (latitude != null && longitude != null) {
                list.add(new BasicNameValuePair("driverOperation", "UpdateLocation"));
                list.add(new BasicNameValuePair("driverId", mdriver.getId()));
                list.add(new BasicNameValuePair("latitude", latitude));
                list.add(new BasicNameValuePair("longitude", longitude));
            }
            return JsonParse.getJsonStringFromUrl(ApiUrls.updateDriverLocation, list);
        }
    }

    private void checkAutoAccpetedOnOff() {
        String driver = gson.toJson(mdriver);
        editor.putString("driver", driver);
        editor.commit();
        if (mdriver.getIsAutoAccepted().equals("1")) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
    }

    private void checkOnlineOffline() {
        String driver = gson.toJson(mdriver);
        editor.putString("driver", driver);
        editor.commit();
        if (mdriver.getIsAvailable().equals("1")) {
            toggleOnlineOffline.setBackgroundResource(R.drawable.button_menu_online);
            toggleOnlineOffline.setChecked(true);
        } else {
            toggleOnlineOffline.setBackgroundResource(R.drawable.button_menu_offline);
            toggleOnlineOffline.setChecked(false);
        }
    }

    private class UpdateAutoAccepted extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("operation", "auto accepted"));
            list.add(new BasicNameValuePair("status", mdriver.getIsAutoAccepted()));
            list.add(new BasicNameValuePair("driverId", mdriver.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.driverSimpleRequest, list);
        }
    }

    private class UpdateOnlineOffline extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("operation", "Online/Offline"));
            list.add(new BasicNameValuePair("status", mdriver.getIsAvailable()));
            list.add(new BasicNameValuePair("driverId", mdriver.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.driverSimpleRequest, list);
        }
    }
    private void checkDriverToCustomer() {
        String user=gson.toJson(muser);
        editor.putString("user",user);
        editor.commit();
    }
}
