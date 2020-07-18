package com.example.androidjava.SellerFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidjava.Model.mSeller;
import com.example.androidjava.R;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SellerProfileShop extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView txtShopName, txtShopAddress, txtShopId, txtShopContactNumber, txtShopEmail;
    private mSeller seller;
    private Button btnShopProfile, btnShopInventory;
    private String strShopInventoryImage, strShopFrontImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_profile_shop, container, false);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        findViewById(view);
        String user = sharedPreferences.getString("seller", null);
        Gson gson = new Gson();
        seller = gson.fromJson(user, mSeller.class);
        txtShopId.setText(seller.getId());
        txtShopEmail.setText(seller.getShop_contact_email());
        txtShopContactNumber.setText(seller.getShop_contact_number());
        txtShopAddress.setText(seller.getShop_pincode());
        txtShopName.setText(seller.getShop_name());
        strShopFrontImage = seller.getShop_front_image();
        strShopInventoryImage = seller.getShop_inventory_image();
        btnShopProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_view_image, null);
                builder.setView(view1);
                ImageView imageView = view1.findViewById(R.id.img_for_view_profile);
                byte[] decodedString1 = Base64.decode(strShopFrontImage, Base64.DEFAULT);
                Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                imageView.setImageBitmap(decodedByte1);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        btnShopInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_view_image, null);
                builder.setView(view1);
                ImageView imageView = view1.findViewById(R.id.img_for_view_profile);
                byte[] decodedString1 = Base64.decode(strShopInventoryImage, Base64.DEFAULT);
                Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                imageView.setImageBitmap(decodedByte1);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    private void findViewById(View view) {
        txtShopId = view.findViewById(R.id.txt_shop_id_shop_profile);
        txtShopName = view.findViewById(R.id.txt_shop_name_shop_profile);
        txtShopAddress = view.findViewById(R.id.txt_shop_address_shop_profile);
        txtShopContactNumber = view.findViewById(R.id.txt_shop_contact_number_shop_profile);
        txtShopEmail = view.findViewById(R.id.txt_shop_email_shop_profile);
        btnShopProfile = view.findViewById(R.id.btn_view_front_image_shop_profile);
        btnShopInventory = view.findViewById(R.id.btn_view_inventory_image_shop_profile);
    }
}
