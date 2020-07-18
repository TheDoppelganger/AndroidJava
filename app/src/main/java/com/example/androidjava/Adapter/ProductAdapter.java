package com.example.androidjava.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Model.mProduct;
import com.example.androidjava.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterViewHolder> {
    static Intent dataImage;
    List<mProduct> listProduct;
    Activity activity;

    public ProductAdapter(List<mProduct> listProduct, Activity activity) {
        this.listProduct = listProduct;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProductAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_pending_product_list, viewGroup, false);
        return new ProductAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterViewHolder productAdapterViewHolder, int i) {
        final mProduct product = listProduct.get(i);
        productAdapterViewHolder.txtProductName.setText(product.getProductName());
        productAdapterViewHolder.txtProductPrice.setText(product.getProductPrice());
        productAdapterViewHolder.txtProductMrp.setText(product.getProductMrp());
        productAdapterViewHolder.txtProductDiscription.setText(product.getProductCategory());
        productAdapterViewHolder.txtProductId.setText(product.getId());
        productAdapterViewHolder.txtProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        productAdapterViewHolder.txtProductId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_product_details, null);
                builder.setView(view1);
                EditText edtProductName, edtProductBarcode, edtProductShortDis, edtProductDis;
                ImageView imgProduct;
                Button btnChangeImg, btnChangeProduct;
                edtProductName = view1.findViewById(R.id.edt_product_name_custom_product_detail);
                edtProductBarcode = view1.findViewById(R.id.edt_product_barcode_custom_product_detail);
                edtProductShortDis = view1.findViewById(R.id.edt_product_short_description_custom_product_detail);
                edtProductDis = view1.findViewById(R.id.edt_product_description_custom_product_detail);
                imgProduct = view1.findViewById(R.id.img_product_custom_product_detail);
                btnChangeImg = view1.findViewById(R.id.btn_change_image_custom_product_detail);
                btnChangeProduct = view1.findViewById(R.id.btn_change_product_custom_product_detail);
                edtProductName.setText(product.getProductName());
                edtProductBarcode.setText(product.getProductBarcode());
                edtProductShortDis.setText(product.getProductShortDescription());
                edtProductDis.setText(product.getProductDescription());
                byte[] decodedString1 = Base64.decode(product.getImgProduct(), Base64.DEFAULT);
                Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                imgProduct.setImageBitmap(decodedByte1);
                btnChangeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startGallery();
                    }
                });
                btnChangeProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(cameraIntent, 1000);
        }
    }

    class ProductAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductId, txtProductDiscription, txtProductMrp;
        EditText txtProductPrice;

        public ProductAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txt_product_name_custom_product_list);
            txtProductId = itemView.findViewById(R.id.txt_product_id_custom_product_list);
            txtProductDiscription = itemView.findViewById(R.id.txt_product_dis_custom_product_list);
            txtProductMrp = itemView.findViewById(R.id.txt_mrp_custom_product_list);
            txtProductPrice = itemView.findViewById(R.id.txt_price_custom_product_list);

        }
    }


}

