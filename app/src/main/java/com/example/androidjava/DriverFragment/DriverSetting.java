package com.example.androidjava.DriverFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DeliveryPartner;
import com.example.androidjava.LogIn;
import com.example.androidjava.Model.mDriver;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DriverSetting extends Fragment {
    private ToggleButton toggleButtonBulkDelivery;
    private mDriver driver;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_setting, container, false);
        findViewById(view);
        toggleButtonBulkDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    toggleButtonBulkDelivery.setBackgroundResource(R.drawable.button_menu_online);
                    driver.setIsBulkDelivery("1");
                    checkBulkDeliveyOnOff();
                    new UpdateBulkDeliveryOnOff().execute();
                    Toast.makeText(getActivity(), "Your bulk delivery is now On", Toast.LENGTH_LONG).show();
                } else {
                    toggleButtonBulkDelivery.setBackgroundResource(R.drawable.button_menu_offline);
                    driver.setIsBulkDelivery("0");
                    checkBulkDeliveyOnOff();
                    new UpdateBulkDeliveryOnOff().execute();
                    Toast.makeText(getActivity(), "You bulk delivery is now Off", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
    private void findViewById(View view){
        toggleButtonBulkDelivery=view.findViewById(R.id.toggle_btn_bulk_delivery_driver_setting);
        sharedPreferences=getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        gson=new Gson();
        String strDriver=sharedPreferences.getString("driver","");
        if(!strDriver.equals("")){
            driver=gson.fromJson(strDriver,mDriver.class);
            checkBulkDeliveyOnOff();
        }else{
            editor.remove("driver");
            editor.remove("user");
            editor.commit();
            Toast.makeText(getActivity(), "Please Re-Log In\nSome thing Went Wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LogIn.class));
        }

    }
    private void checkBulkDeliveyOnOff(){
        String strDriver = gson.toJson(driver);
        editor.putString("driver", strDriver);
        editor.commit();
        if (driver.getIsBulkDelivery().equals("1")) {
            toggleButtonBulkDelivery.setBackgroundResource(R.drawable.button_menu_online);
           toggleButtonBulkDelivery.setChecked(true);
        } else {
            toggleButtonBulkDelivery.setBackgroundResource(R.drawable.button_menu_offline);
            toggleButtonBulkDelivery.setChecked(false);
        }
    }
    private class UpdateBulkDeliveryOnOff extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("operation", "Bulk Delivery"));
            list.add(new BasicNameValuePair("status", driver.getIsBulkDelivery()));
            list.add(new BasicNameValuePair("driverId", driver.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.driverSimpleRequest, list);
        }
    }
}