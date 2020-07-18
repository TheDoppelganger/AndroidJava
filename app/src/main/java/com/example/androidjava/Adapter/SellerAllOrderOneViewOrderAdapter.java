package com.example.androidjava.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Model.mOrderItem;
import com.example.androidjava.R;

import java.util.List;

public class SellerAllOrderOneViewOrderAdapter extends RecyclerView.Adapter<SellerAllOrderOneViewOrderAdapter.SellerAllOrderOneViewOrderAdapterViewHolder> {
    List<mOrderItem> list;
    Activity activity;

    public SellerAllOrderOneViewOrderAdapter(List<mOrderItem> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SellerAllOrderOneViewOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_seller_one_order_view_order, parent, false);
        return new SellerAllOrderOneViewOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SellerAllOrderOneViewOrderAdapterViewHolder holder, int position) {
        final mOrderItem item = list.get(position);
        byte[] decodedString = Base64.decode(item.getImg_product(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imgProduct.setImageBitmap(decodedByte);
        holder.txtProductName.setText(item.getProduct_name());
        holder.txtProductQut.setText(item.getProduct_qut());
        holder.txtProductPrice.setText(item.getProduct_price());
        holder.ckReadyToDispatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.ckReadyToDispatch.isChecked()) {
                    item.setOrder_status("Ready To Dispatch");
                } else {
                    item.setOrder_status("Pending");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SellerAllOrderOneViewOrderAdapterViewHolder extends RecyclerView.ViewHolder {
        CheckBox ckReadyToDispatch;
        ImageView imgProduct;
        TextView txtProductName, txtProductQut, txtProductPrice;

        public SellerAllOrderOneViewOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ckReadyToDispatch = itemView.findViewById(R.id.ck_is_ready_dispatch_custom_seller_one_view_order);
            imgProduct = itemView.findViewById(R.id.img_product_custom_seller_one_view_order);
            txtProductName = itemView.findViewById(R.id.txt_product_name_custom_seller_one_view_order);
            txtProductPrice = itemView.findViewById(R.id.txt_product_price_custom_seller_one_view_order);
            txtProductQut = itemView.findViewById(R.id.txt_product_qut_custom_seller_one_view_order);
        }
    }
}
