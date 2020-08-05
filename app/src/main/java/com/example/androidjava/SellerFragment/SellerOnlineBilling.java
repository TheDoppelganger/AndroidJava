package com.example.androidjava.SellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidjava.R;

public class SellerOnlineBilling extends Fragment {

    RecyclerView rv_onlinebillingitems;


    public SellerOnlineBilling() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_seller_online_billing, container, false);
        rv_onlinebillingitems = view.findViewById(R.id.rv_onlinebillingitems);


        return view;
    }
}