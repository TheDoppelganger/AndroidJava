package com.example.androidjava.Adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Model.mVarient;
import com.example.androidjava.R;

import java.util.List;

public class AddProductVarient extends RecyclerView.Adapter<AddProductVarient.AddProductVarientViewHolde> {
    List<mVarient> listProduct;
    Activity activity;

    public AddProductVarient(List<mVarient> listProduct, Activity activity) {
        this.listProduct = listProduct;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AddProductVarientViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_variant_add_product, parent, false);
        return new AddProductVarientViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddProductVarientViewHolde holder, int position) {
        final mVarient varient = listProduct.get(position);
        if (varient.getProductMrp() != null) {
            holder.edtVarientName.setText(varient.getProductVarient());
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
        holder.edtVarientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                varient.setProductVarient(holder.edtVarientName.getText().toString().trim());
            }
        });
        holder.rbPacked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.rbPacked.isChecked()) {
                    varient.setProudctPacked("Packed");
                } else {
                    varient.setProudctPacked("Loose");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    class AddProductVarientViewHolde extends RecyclerView.ViewHolder {
        EditText edtVarientName, edtMrp, edtPrice;
        RadioButton rbPacked;

        public AddProductVarientViewHolde(@NonNull View itemView) {
            super(itemView);
            edtVarientName = itemView.findViewById(R.id.edt_variant_name_custom_add_product);
            edtMrp = itemView.findViewById(R.id.edt_variant_mrp_custom_add_product);
            edtPrice = itemView.findViewById(R.id.edt_variant_price_custom_add_product);
            rbPacked = itemView.findViewById(R.id.rb_variant_packed_custom_add_product);


        }
    }

}
