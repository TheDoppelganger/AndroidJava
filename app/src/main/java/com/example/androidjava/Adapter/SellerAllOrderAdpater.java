package com.example.androidjava.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Model.mOrderCustomerOnline;
import com.example.androidjava.R;
import com.example.androidjava.SellerFragment.SellerNewOrder;
import com.example.androidjava.SellerFragment.SellerNewOrderViewOrder;
import com.google.gson.Gson;

import java.util.List;

public class SellerAllOrderAdpater extends RecyclerView.Adapter<SellerAllOrderAdpater.SellerAllOrderAdpaterViewHolder> {
    Activity activity;
    List<mOrderCustomerOnline> list;
    SellerNewOrder sellerNewOrder;
    String TAG = "New Order";
    public SellerAllOrderAdpater(Activity activity, List<mOrderCustomerOnline> list, SellerNewOrder sellerNewOrder) {
        this.activity = activity;
        this.list = list;
        this.sellerNewOrder = sellerNewOrder;
    }

    @NonNull
    @Override
    public SellerAllOrderAdpaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_seller_one_order_details, parent, false);
        return new SellerAllOrderAdpaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerAllOrderAdpaterViewHolder holder, int position) {
        final mOrderCustomerOnline orderCustomerOnline = list.get(position);
        holder.txtOrderAmount.setText(orderCustomerOnline.getTotal_payable_amount());
        holder.txtOrderId.setText(orderCustomerOnline.getOrder_no());
        holder.btnViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerNewOrderViewOrder sellerNewOrderViewOrder = new SellerNewOrderViewOrder();
                Bundle b = new Bundle();
                Gson gson=new Gson();
                String s=gson.toJson(orderCustomerOnline.getOrder_item());
                b.putString("orderItem", s);
                sellerNewOrderViewOrder.setArguments(b);
                ((FragmentActivity) activity).getSupportFragmentManager().beginTransaction()
                        .hide(sellerNewOrder)
                        .addToBackStack(TAG)
                        .add(R.id.container_seller, sellerNewOrderViewOrder)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SellerAllOrderAdpaterViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderDate, txtOrderId, txtOrderPayment, txtOrderAmount;
        Button btnViewOrder;

        public SellerAllOrderAdpaterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderDate = itemView.findViewById(R.id.txt_order_date_custom_seller_one_order);
            txtOrderId = itemView.findViewById(R.id.txt_order_id_custom_seller_one_order);
            txtOrderPayment = itemView.findViewById(R.id.txt_order_payment_custom_seller_one_order);
            txtOrderAmount = itemView.findViewById(R.id.txt_order_amount_custom_seller_one_order);
            btnViewOrder = itemView.findViewById(R.id.btn_view_order_custom_seller_one_order);

        }
    }
}
