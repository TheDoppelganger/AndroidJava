package com.example.androidjava.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DeliveryPartner;
import com.google.android.gms.location.LocationResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class UpdateDeliveryPartnerLocation extends BroadcastReceiver {
    public static final String ACTION_PROCESS_UPDATE = "com.example.androidjava.BroadcastReceiver.UPDATE_LOCATION";
    String longitude, latitude;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (action.equals(ACTION_PROCESS_UPDATE)) {

                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    Location location = result.getLastLocation();
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    try {
                        DeliveryPartner.getInstance().updateLatLong(latitude, longitude);
                    } catch (Exception e) {

                    }

                } else {

                }
            } else {
                Toast.makeText(context, "", Toast.LENGTH_LONG).show();
            }
        }
    }
}
