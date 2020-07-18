package com.example.androidjava;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.ApiCalling.GooglePlacesAutoComplete;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.Model.mUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleMap extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    com.google.android.gms.maps.GoogleMap googleMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment supportMapFragment;
    Button btnDone, btnAddAddressHere;
    String latitude, longitude;
    Intent io;
    String s = "S2";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    private List<mSeller> mainList;
    Marker[] m;
    List<Marker> list;
    SearchView edtSearchLocation;
    mUser user;
    //GooglePlacesAutoComplete dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        io = getIntent();
        s = io.getStringExtra("Activity");
        findViewById();
        Log.i("Response", s);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        if (s.equals("123")) {
            editor.remove("user");
            editor.remove("seller");
            editor.remove("driver");
            editor.commit();
            Toast.makeText(this, "Some Thing Went Wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GoogleMap.this, LogIn.class));
            finish();
        }
        edtSearchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = edtSearchLocation.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(GoogleMap.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        latitude = String.valueOf(latLng.latitude);
                        longitude = String.valueOf(latLng.longitude);
                        updateUserString();
                        btnAddAddressHere.setVisibility(View.VISIBLE);
                        if (list.size() > 0) {
                            for (int j = 0; j < list.size(); j++) {
                                Marker m = list.get(j);
                                m.remove();
                            }
                            m[0].remove();
                            list.clear();
                        }
                        list.add(googleMap.addMarker(new MarkerOptions().position(latLng).title(location)));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
                        new GetShopsAllNearByMe().execute();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Please Enter Proper Address or Keyword to search\nThank you",Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        btnAddAddressHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.equals("S2")) {
                    editor.remove("user");
                    editor.remove("seller");
                    editor.remove("driver");
                    editor.commit();
                    Toast.makeText(GoogleMap.this, "Some Thing Went Wrong", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GoogleMap.this, LogIn.class));
                    finish();
                } else {

                }
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s.equals("S2")) {
                    Intent io = new Intent(GoogleMap.this, MainActivity.class);
                    io.putExtra("lat", latitude);
                    io.putExtra("lang", longitude);
                    startActivity(io);
                    finish();
                } else {
                    Intent io = new Intent(GoogleMap.this, CustomerActivity.class);
                    startActivity(io);
                }
            }
        });

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GoogleMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(GoogleMap.this);

                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onMapReady(final com.google.android.gms.maps.GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            latitude = String.valueOf(currentLocation.getLatitude());
            longitude = String.valueOf(currentLocation.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
             m = new Marker[]{googleMap.addMarker(new MarkerOptions().position(latLng).title("Here"))};
            googleMap.setOnMapClickListener(new com.google.android.gms.maps.GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    m[0].remove();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
                    latitude = String.valueOf(latLng.latitude);
                    longitude = String.valueOf(latLng.longitude);
                    updateUserString();
                    m[0] = googleMap.addMarker(new MarkerOptions().position(latLng).title("Here"));
                    if (!s.equals("S2")) {
                        btnAddAddressHere.setVisibility(View.VISIBLE);
                        if (list.size() > 0) {
                            for (int j = 0; j < list.size(); j++) {
                                Marker m = list.get(j);
                                m.remove();
                            }
                            list.clear();
                        }
                        new GetShopsAllNearByMe().execute();
                    }
                }
            });
            if (!s.equals("S2")) {
                UpdateShopInMap(s);
            }
        }
    }

    private void findViewById() {
        btnDone = findViewById(R.id.google_map_btn_done);
        mainList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("Database", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
        String user1=sharedPreferences.getString("user","");
        Log.i("Response",user1);
        user=gson.fromJson(user1,mUser.class);
        list = new ArrayList<>();
        btnAddAddressHere = findViewById(R.id.add_address_this_location_google_map);
        edtSearchLocation = findViewById(R.id.edt_search_location_google_map);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    class GetShopsAllNearByMe extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(GoogleMap.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("!!Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("latitude", latitude));
            list.add(new BasicNameValuePair("longitude", longitude));
            return JsonParse.getJsonStringFromUrl(ApiUrls.getShopAllNearBy, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            UpdateShopInMap(s);
        }
    }

    private void UpdateShopInMap(String s1) {
        try {
            mainList.clear();
            JSONArray jsonArray = new JSONArray(s1);
            for (int i = 0; i < jsonArray.length(); i++) {
                String seller = jsonArray.getString(i);
                mSeller seller1 = gson.fromJson(seller, mSeller.class);
                mainList.add(seller1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mainList.size() > 0) {
            Log.i("Response", String.valueOf(list.size()));
            for (int i = 0; i < mainList.size(); i++) {
                mSeller seller = mainList.get(i);
                LatLng latLng1 = new LatLng(Double.parseDouble(seller.getShop_latitude()), Double.parseDouble(seller.getShop_longitude()));
                list.add(googleMap.addMarker(new MarkerOptions().position(latLng1).title(seller.getShop_name()).icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_store_black_18dp))));
            }
        } else {
            Toast.makeText(getApplicationContext(), "No any Shop Here!!", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateUserString(){
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        editor.putString("user",gson.toJson(user));
        editor.commit();
    }
}
