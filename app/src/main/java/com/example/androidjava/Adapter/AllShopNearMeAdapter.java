package com.example.androidjava.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.CustomerFragment.CustomerOneShop;
import com.example.androidjava.CustomerFragment.CustomerShopNearMe;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.R;

import java.util.List;

public class AllShopNearMeAdapter extends RecyclerView.Adapter<AllShopNearMeAdapter.AllShopNearMeAdapterViewHolder> {
    private Activity activity;
    private List<mSeller> sellers;
    String TAG = "AllShopNearMe";
private CustomerShopNearMe customerShopNearMe;

    public AllShopNearMeAdapter(Activity activity, List<mSeller> sellers, CustomerShopNearMe customerShopNearMe) {
        this.activity = activity;
        this.sellers = sellers;
        this.customerShopNearMe = customerShopNearMe;
    }

    @NonNull
    @Override
    public AllShopNearMeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_customer_one_shop_near_by_details, parent, false);
        return new AllShopNearMeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllShopNearMeAdapterViewHolder holder, int position) {
        final mSeller seller = sellers.get(position);
        holder.shopRate.setRating(Float.parseFloat(seller.getShop_rate()));
        holder.txtShopCategory.setText(seller.getShop_category());
        holder.txtShopAddress.setText(seller.getShop_pincode());
        holder.txtShopName.setText(seller.getShop_name());
        holder.imgFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Favourite", Toast.LENGTH_LONG).show();
            }
        });
        byte[] decodedString = Base64.decode(seller.getShop_front_image(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imgFrontImage.setImageBitmap(decodedByte);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerOneShop customerOneShop = new CustomerOneShop();
                Bundle b = new Bundle();
                b.putString("id", seller.getId());
                b.putString("shopName", seller.getShop_name());
                b.putString("shopContact", seller.getShop_contact_number());
                b.putString("shopRate", seller.getShop_rate());
                b.putString("shopFrontImage", seller.getShop_front_image());
                b.putString("shopLat",seller.getShop_latitude());
                b.putString("shopLong",seller.getShop_longitude());
                customerOneShop.setArguments(b);
                ((FragmentActivity) activity).getSupportFragmentManager().beginTransaction()
                        .hide(customerShopNearMe)
                        .add(R.id.container_customer, customerOneShop,"")
                        .addToBackStack(TAG)
                        .commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return sellers.size();
    }

    class AllShopNearMeAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFrontImage, imgFavourite;
        TextView txtShopName, txtShopAddress, txtShopCategory;
        RatingBar shopRate;

        public AllShopNearMeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFrontImage = itemView.findViewById(R.id.img_fornt_img_one_shop_details);
            imgFavourite = itemView.findViewById(R.id.img_shop_fav_one_shop_details);
            txtShopName = itemView.findViewById(R.id.txt_shop_name_one_shop_details);
            txtShopAddress = itemView.findViewById(R.id.txt_shop_address_one_shop_details);
            txtShopCategory = itemView.findViewById(R.id.txt_shop_category_one_shop_details);
            shopRate = itemView.findViewById(R.id.rate_of_shop_one_shop_details);
        }
    }
}
