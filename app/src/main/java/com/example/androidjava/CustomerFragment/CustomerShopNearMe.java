package com.example.androidjava.CustomerFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.AllShopNearMeAdapter;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CustomerShopNearMe extends Fragment {
    private List<mSeller> mainList;
    private RecyclerView recyclerView;
    private CustomerShopNearMe customerShopNearMe;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private mUser user;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_shop_near_me, container, false);
        findViewById(view);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Location");
        dialog.setMessage("Do you Want current location?? /n Otherwise you get Previous seted location...");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
                fetchLastLocation();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new GetShopsAllNearByMe().execute();
            }
        }).show();

        return view;
    }


    private void findViewById(View view) {
        recyclerView = view.findViewById(R.id.recyle_all_shop_customer_shop_near_me);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        mainList = new ArrayList<>();
        customerShopNearMe = this;
        sharedPreferences = getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String user1 = sharedPreferences.getString("user", "");
        gson = new Gson();
        if (!user1.equals("")) {
            user = gson.fromJson(user1, mUser.class);
        }

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    if (currentLocation != null) {
                        user.setLatitude(String.valueOf(currentLocation.getLatitude()));
                        user.setLongitude(String.valueOf(currentLocation.getLongitude()));
                        editor.putString("user", gson.toJson(user));
                        editor.commit();
                        new GetShopsAllNearByMe().execute();
                    } else
                        Toast.makeText(getActivity(), "Location Must be on...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    class GetShopsAllNearByMe extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

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
            list.add(new BasicNameValuePair("latitude", user.getLatitude()));
            list.add(new BasicNameValuePair("longitude", user.getLongitude()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.getShopAllNearBy, list);
        }

        @Override
        protected void onPostExecute(String s) {
            editor.putString("allShop", s);
            editor.commit();
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String oneShop = jsonArray.getString(i);
                    Gson gson = new Gson();
                    mSeller oneSeller = gson.fromJson(oneShop, mSeller.class);
                    mainList.add(oneSeller);
                }
                AllShopNearMeAdapter allShopNearMeAdapter = new AllShopNearMeAdapter(getActivity(), mainList, customerShopNearMe);
                recyclerView.setAdapter(allShopNearMeAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

}
