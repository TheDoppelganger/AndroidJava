package com.example.androidjava.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DriverFragment.DriverRegistration2;
import com.example.androidjava.Model.mVarient;
import com.example.androidjava.R;
import com.example.androidjava.SellerFragment.AddProduct;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.example.androidjava.DatabaseConnection.JsonParse.getStringImage;

public class AddProductVarient extends RecyclerView.Adapter<AddProductVarient.AddProductVarientViewHolde> {
    List<mVarient> listProduct;
    Activity activity;
    AddProduct addProduct;
    public AddProductVarient(List<mVarient> listProduct, Activity activity, AddProduct addProduct) {
        this.listProduct = listProduct;
        this.activity = activity;
        this.addProduct=addProduct;
    }

    @NonNull
    @Override
    public AddProductVarientViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_variant_add_product, parent, false);
        return new AddProductVarientViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddProductVarientViewHolde holder, final int position) {
        final mVarient varient = listProduct.get(position);
        holder.txtImgName.setText(varient.getProductvariantImageName());
        holder.edtBarCode.setText(varient.getProductBarCode());
        holder.btnCancelVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listProduct.remove(position);
                notifyDataSetChanged();
            }
        });
        if (varient.getProductMrp() != null) {
            holder.edtMrp.setText(varient.getProductMrp());
            holder.edtPrice.setText(varient.getProductPrice());
        }
        holder.edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                varient.setProductPrice(holder.edtPrice.getText().toString().trim());
            }
        });
        holder.edtMrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                varient.setProductMrp(holder.edtMrp.getText().toString().trim());
            }
        });

        holder.rbPacked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.rbPacked.isChecked()) {
                    varient.setProudctPacked("Packed");
                    holder.linearUnitChoose.setVisibility(View.GONE);
                } else {
                    varient.setProudctPacked("Loose");
                    holder.linearUnitChoose.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.btnKgUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnKgUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button));
                holder.btnLiterUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button1));
                holder.btnOtherUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button1));
                holder.edtOtherUnit.setVisibility(View.GONE);
                varient.setProductUnit("K.G.");
            }
        });
        holder.btnLiterUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnKgUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button1));
                holder.btnLiterUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button));
                holder.btnOtherUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button1));
                holder.edtOtherUnit.setVisibility(View.GONE);
                varient.setProductUnit("Liter");
            }
        });
        holder.btnOtherUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnKgUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button1));
                holder.btnLiterUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button1));
                holder.btnOtherUnit.setBackground(ContextCompat.getDrawable(activity, R.drawable.button));
                holder.edtOtherUnit.setVisibility(View.VISIBLE);
            }
        });
        holder.edtOtherUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                varient.setProductUnit(editable.toString());
            }
        });

        holder.edtStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                varient.setProductStock(editable.toString());
            }
        });
        holder.edtBarCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                varient.setProductBarCode(editable.toString());
            }
        });
        holder.imgOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(addProduct.getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    cameraIntent.setType("image/*");
                    if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
                        addProduct.startActivityForResult(cameraIntent, 5000);
                    }

                } else {
                    ActivityCompat.requestPermissions(addProduct.getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
            }
        });

        holder.btnBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setRequestCode(1210).initiateScan();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    class AddProductVarientViewHolde extends RecyclerView.ViewHolder {
        EditText edtMrp, edtPrice, edtBarCode, edtOtherUnit, edtStock, edtVariantSize;
        RadioButton rbPacked;
        LinearLayout linearUnitChoose;
        Button btnKgUnit, btnLiterUnit, btnOtherUnit, btnCancelVariant;
        ImageView imgOpenGallery, imgOpenCamera,btnBarcodeScan;
        TextView txtImgName;
        public AddProductVarientViewHolde(@NonNull View itemView) {
            super(itemView);
            edtVariantSize = itemView.findViewById(R.id.edt_variant_size_custom_add_product);
            edtMrp = itemView.findViewById(R.id.edt_variant_mrp_custom_add_product);
            edtPrice = itemView.findViewById(R.id.edt_variant_price_custom_add_product);
            rbPacked = itemView.findViewById(R.id.rb_variant_packed_custom_add_product);
            linearUnitChoose = itemView.findViewById(R.id.linear_unit_add_product);
            edtBarCode = itemView.findViewById(R.id.edt_variant_barcode_custom_add_product);
            edtStock = itemView.findViewById(R.id.edt_variant_stock_custom_add_product);
            edtOtherUnit = itemView.findViewById(R.id.edt_variant_other_unit_custom_add_product);
            btnKgUnit = itemView.findViewById(R.id.btn_variant_kg_unit_custom_add_product);
            btnLiterUnit = itemView.findViewById(R.id.btn_variant_liter_unit_custom_add_product);
            btnOtherUnit = itemView.findViewById(R.id.btn_variant_other_unit_custom_add_product);
            btnCancelVariant = itemView.findViewById(R.id.btn_cancel_varient_custom_add_product);
            imgOpenCamera = itemView.findViewById(R.id.btn_select_image_camera_varient_custom_add_product);
            imgOpenGallery = itemView.findViewById(R.id.btn_select_image_varient_custom_add_product);
            txtImgName=itemView.findViewById(R.id.edt_choose_Image_custom_add_product);
            btnBarcodeScan=itemView.findViewById(R.id.btnBarcodeScan);
        }

    }

    private void startGallery() {

    }



}
