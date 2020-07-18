package com.example.androidjava.SellerFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.SellerAllOrderOneViewOrderAdapter;
import com.example.androidjava.Model.mOrderItem;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class SellerNewOrderViewOrder extends Fragment {
    private String orderItem;
    private List<mOrderItem> mainList;
    private RecyclerView recycleViewOrder;
    private Button btnReadyToDispatch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_new_order_view_order, container, false);
        findViewById(view);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if( keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    getActivity().getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
        Bundle b = getArguments();
        orderItem = b.getString("orderItem", "");
        Log.i("response", orderItem);
        if (!orderItem.equals("")) {

            try {
                JSONArray jsonArray = new JSONArray(orderItem);
                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String orderItem = jsonArray.getString(i);
                    mOrderItem orderItem1 = gson.fromJson(orderItem, mOrderItem.class);
                        mainList.add(orderItem1);
                }
                Toast.makeText(getActivity(), String.valueOf(mainList.size()), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        }
        SellerAllOrderOneViewOrderAdapter sellerAllOrderOneViewOrderAdapter = new SellerAllOrderOneViewOrderAdapter(mainList, getActivity());
        recycleViewOrder.setAdapter(sellerAllOrderOneViewOrderAdapter);
        return view;
    }

    private void findViewById(View view) {
        mainList = new ArrayList<>();
        recycleViewOrder = view.findViewById(R.id.recycle_seller_new_view_order);
        recycleViewOrder.setHasFixedSize(true);
        recycleViewOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
