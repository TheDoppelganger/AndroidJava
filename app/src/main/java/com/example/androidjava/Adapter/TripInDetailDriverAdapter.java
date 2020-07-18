package com.example.androidjava.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DriverFragment.DriverMyTask;
import com.example.androidjava.Model.mDeliveryInstrusction;
import com.example.androidjava.Model.mDriver;
import com.example.androidjava.R;
import com.google.gson.Gson;

import java.util.List;

public class TripInDetailDriverAdapter extends RecyclerView.Adapter<TripInDetailDriverAdapter.TripInDetailDriverAdapterViewHolder> {
    private Activity activity;
    private List<mDeliveryInstrusction> list;
    private SharedPreferences sharedPreferences;
    private String userDriver;
    private Gson gson;

    public TripInDetailDriverAdapter(Activity activity, List<mDeliveryInstrusction> list) {
        this.activity = activity;
        this.list = list;
        sharedPreferences = activity.getSharedPreferences("Database", Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public TripInDetailDriverAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_driver_delivery_details, parent, false);
        return new TripInDetailDriverAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripInDetailDriverAdapterViewHolder holder, int position) {
        final mDeliveryInstrusction deliveryInstrusction = list.get(position);
        if (deliveryInstrusction.getShopOrCustomer().equals("Customer")) {
            holder.linearShopDetails.setVisibility(View.GONE);
            holder.txtCustomerName.setText(deliveryInstrusction.getName());
            holder.txtCustomerNumber.setText(deliveryInstrusction.getContactNumber());
            holder.txtCustomerAddress.setText(deliveryInstrusction.getAddress());
            userDriver = sharedPreferences.getString("driver", null);
            gson = new Gson();
            mDriver instrusction = gson.fromJson(userDriver, mDriver.class);
            String distance = String.valueOf(Math.round(JsonParse.distance(Double.parseDouble(instrusction.getLatitude()), Double.parseDouble(instrusction.getLongitude()), Double.parseDouble(deliveryInstrusction.getLatitude()), Double.parseDouble(deliveryInstrusction.getLongitude()))));
            holder.txtCustomerDistance.setText(distance);
            if (deliveryInstrusction.getDirectionShow().equals("0")) {
                holder.btnDirectionToCustomer.setVisibility(View.GONE);
            } else if (deliveryInstrusction.getDirectionShow().equals("1")) {
                holder.btnDirectionToCustomer.setVisibility(View.VISIBLE);
            }

            holder.btnDirectionToCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String geoUri = "http://maps.google.com/maps?q=loc:" + String.valueOf(deliveryInstrusction.getLatitude()) + "," + String.valueOf(deliveryInstrusction.getLongitude()) + " (" + deliveryInstrusction.getName() + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    DriverMyTask.isClickDrication=true;
                    activity.startActivity(intent);
                }
            });
        } else if (deliveryInstrusction.getShopOrCustomer().equals("Shop")) {
            holder.linearCustomerDetails.setVisibility(View.GONE);
            holder.txtShopName.setText(deliveryInstrusction.getName());
            holder.txtShopNumber.setText(deliveryInstrusction.getContactNumber());
            holder.txtShopAddress.setText(deliveryInstrusction.getAddress());
            userDriver = sharedPreferences.getString("driver", null);
            gson = new Gson();
            mDriver instrusction = gson.fromJson(userDriver, mDriver.class);
            String distance = String.valueOf(Math.round(JsonParse.distance(Double.parseDouble(instrusction.getLatitude()), Double.parseDouble(instrusction.getLongitude()), Double.parseDouble(deliveryInstrusction.getLatitude()), Double.parseDouble(deliveryInstrusction.getLongitude()))));
            holder.txtShopDistance.setText(distance);
            byte[] decodedString1 = Base64.decode(deliveryInstrusction.getShopFrontImage(), Base64.DEFAULT);
            Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
            holder.imgShopFront.setImageBitmap(decodedByte1);
            if (deliveryInstrusction.getDirectionShow().equals("0")) {
                holder.btnDirectionToShop.setVisibility(View.GONE);
            } else if (deliveryInstrusction.getDirectionShow().equals("1")) {
                holder.btnDirectionToShop.setVisibility(View.VISIBLE);
            }
        }
        holder.btnDirectionToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + String.valueOf(deliveryInstrusction.getLatitude()) + "," + String.valueOf(deliveryInstrusction.getLongitude()) + " (" + deliveryInstrusction.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                DriverMyTask.isClickDrication=true;
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TripInDetailDriverAdapterViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearShopDetails, linearCustomerDetails;
        private TextView txtShopName, txtShopNumber, txtShopDistance, txtShopAddress;
        private TextView txtCustomerName, txtCustomerNumber, txtCustomerAddress, txtCustomerDistance;
        private Button btnDirectionToShop, btnDirectionToCustomer;
        private ImageView imgShopFront;

        public TripInDetailDriverAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            linearShopDetails = itemView.findViewById(R.id.linear_shop_custom_driver_delivery_details);
            linearCustomerDetails = itemView.findViewById(R.id.linear_customer_custom_driver_delivery_details);
            imgShopFront = itemView.findViewById(R.id.img_shop_front_custom_driver_delivery_details);
            txtShopName = itemView.findViewById(R.id.txt_shop_name_custom_driver_delivery_details);
            txtShopNumber = itemView.findViewById(R.id.txt_shop_number_custom_driver_delivery_details);
            txtShopAddress = itemView.findViewById(R.id.txt_shop_address_custom_driver_delivery_details);
            txtShopDistance = itemView.findViewById(R.id.txt_shop_distance_custom_driver_delivery_details);
            btnDirectionToShop = itemView.findViewById(R.id.btn_shop_direction_custom_driver_delivery_details);
            txtCustomerName = itemView.findViewById(R.id.txt_customer_name_custom_driver_delivery_details);
            txtCustomerNumber = itemView.findViewById(R.id.txt_customer_phone_custom_driver_delivery_details);
            txtCustomerAddress = itemView.findViewById(R.id.txt_customer_address_custom_driver_delivery_details);
            txtCustomerDistance = itemView.findViewById(R.id.txt_customer_distance_custom_driver_delivery_details);
            btnDirectionToCustomer = itemView.findViewById(R.id.btn_customer_direction_custom_driver_delivery_details);
        }
    }
}
