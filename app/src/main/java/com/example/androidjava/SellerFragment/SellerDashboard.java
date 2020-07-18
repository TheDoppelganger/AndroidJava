package com.example.androidjava.SellerFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidjava.Model.mSeller;
import com.example.androidjava.R;
import com.google.gson.Gson;

public class SellerDashboard extends Fragment {
    private TextView txtTotalOrder, txtTotalSellOrder, txtTotalReturnOrder, txtTotalInventory;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_dashboard, container, false);
        findViewById(view);
        String user = sharedPreferences.getString("seller", "");
        if (!user.equals("")) {
            Gson gson = new Gson();
            mSeller seller=gson.fromJson(user,mSeller.class);
            txtTotalOrder.setText(seller.getShopTotalOrder());
            txtTotalSellOrder.setText(seller.getShopTotalSell());
            txtTotalReturnOrder.setText(seller.getShopTotalReturnOrder());
            txtTotalInventory.setText(seller.getShopLowInventory());
        }
        return view;
    }

    private void findViewById(View view) {
        txtTotalOrder = view.findViewById(R.id.txt_total_order_seller_dashboard);
        txtTotalReturnOrder = view.findViewById(R.id.txt_total_return_seller_dashboard);
        txtTotalSellOrder = view.findViewById(R.id.txt_total_sell_seller_dashboard);
        txtTotalInventory = view.findViewById(R.id.txt_low_inventory_seller_dashboard);
        sharedPreferences = getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
    }
}
