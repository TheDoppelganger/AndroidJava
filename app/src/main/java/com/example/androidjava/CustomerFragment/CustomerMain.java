package com.example.androidjava.CustomerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.androidjava.R;

public class CustomerMain extends Fragment {
    private Button btnShop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_main, container, false);
        findViewById(view);
        /*Location startPoint=new Location("locationA");
        startPoint.setLatitude(23.026426);
        startPoint.setLongitude(72.584343);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(23.079802);
        endPoint.setLongitude(72.522185);

        double distance=distance(23.027204,72.585871,23.079802,72.522185);
        Log.i("response",String.valueOf(distance* 1.609344));
        Toast.makeText(getActivity(),String.valueOf(distance* 1.609344),Toast.LENGTH_LONG).show();*/
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerShopNearMe()).commit();
            }
        });
        return view;
    }


    private void findViewById(View view) {
        btnShop = view.findViewById(R.id.btn_all_shop_near_customer_main);
    }

}
