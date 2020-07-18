package com.example.androidjava.SellerFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.androidjava.CustomerActivity;
import com.example.androidjava.DeliveryPartner;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.example.androidjava.SellerActivity;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SellerProfileCustomer extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    mUser muser;
    private TextView txtId, txtName, txtAadharCard, txtEmail, txtPhoneNo, txtAddress;
    private ToggleButton btnSwitchToCustoemer;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_profile_customer, container, false);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        findViewById(view);
        checkDriverToCustomer();
        if (muser.getUser_type().equals("Driver")) {
            btnSwitchToCustoemer.setVisibility(View.VISIBLE);
            String name=getActivity().getClass().getSimpleName();
            if(name.equals("CustomerActivity")){
                btnSwitchToCustoemer.setChecked(true);
            }
        }else if(muser.getUser_type().equals("Seller")){
            btnSwitchToCustoemer.setTextOn("Switch To Seller Account");
            btnSwitchToCustoemer.setVisibility(View.VISIBLE);
            String name=getActivity().getClass().getSimpleName();
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
                    startActivity(new Intent(getContext(),CustomerActivity.class));
                } else {
                    if(muser.getUser_type().equals("driver")) {
                        muser.setIsSwitch("0");
                        checkDriverToCustomer();
                        startActivity(new Intent(getContext(), DeliveryPartner.class));
                    }else if(muser.getUser_type().equals("seller")){
                        muser.setIsSwitch("0");
                        checkDriverToCustomer();
                        startActivity(new Intent(getContext(), SellerActivity.class));
                    }
                }
            }
        });
        txtName.setText(muser.getName());
        txtPhoneNo.setText(muser.getMobile_no());
        txtEmail.setText(muser.getEmail());
        txtAadharCard.setText(muser.getAdharcardNo());
        txtAddress.setText(muser.getAddress());
        return view;
    }

    private void findViewById(View view) {
        txtId = view.findViewById(R.id.txt_customer_id_customer_profile);
        txtAadharCard = view.findViewById(R.id.txt_customer_aadhar_id_customer_profile);
        txtAddress = view.findViewById(R.id.txt_customer_address_customer_profile);
        txtEmail = view.findViewById(R.id.txt_customer_email_id_customer_profile);
        txtName = view.findViewById(R.id.txt_customer_name_customer_profile);
        txtPhoneNo = view.findViewById(R.id.txt_customer_mobile_no_customer_profile);
        btnSwitchToCustoemer = view.findViewById(R.id.btn_switch_to_customer_account_customer_profile);
        String user = sharedPreferences.getString("user", null);
        gson = new Gson();
        muser = gson.fromJson(user, mUser.class);
    }

    private void checkDriverToCustomer() {
        String user=gson.toJson(muser);
        editor.putString("user",user);
        editor.commit();
    }

}
