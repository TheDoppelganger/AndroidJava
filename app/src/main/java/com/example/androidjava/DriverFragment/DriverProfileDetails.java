package com.example.androidjava.DriverFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidjava.DeliveryPartner;
import com.example.androidjava.LogIn;
import com.example.androidjava.Model.mDriver;
import com.example.androidjava.R;
import com.google.gson.Gson;

public class DriverProfileDetails extends Fragment {
    private TextView txtDriverId,txtVehicleType,txtVehicleNo,txtLicenceNo;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private mDriver driver=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_driver_profile_details, container, false);
        findViewById(view);
        if(driver!=null){
            txtDriverId.setText(driver.getId());
            txtVehicleType.setText(driver.getVehicleType());
            txtVehicleNo.setText(driver.getDriverNumberPlate());
            txtLicenceNo.setText(driver.getDriverLinceneceno());
        }else{
            editor.remove("driver");
            editor.remove("user");
            editor.commit();
            startActivity(new Intent(getActivity(), LogIn.class));
        }
        return view;
    }
    private void findViewById(View view){
        txtDriverId=view.findViewById(R.id.txt_driver_id_driver_profile);
        txtVehicleType=view.findViewById(R.id.txt_driver_vehicle_type_driver_profile);
        txtVehicleNo=view.findViewById(R.id.txt_driver_vehicle_no_driver_profile);
        txtLicenceNo=view.findViewById(R.id.txt_driver_licence_no_driver_profile);
        sharedPreferences=getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String user=sharedPreferences.getString("driver","");
        if(!user.equals("")){
            gson=new Gson();
            driver=gson.fromJson(user,mDriver.class);
        }
    }
}
