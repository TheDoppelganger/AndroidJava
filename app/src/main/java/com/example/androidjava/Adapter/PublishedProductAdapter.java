package com.example.androidjava.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Model.mProduct;
import com.example.androidjava.R;

import java.util.List;

public class PublishedProductAdapter extends RecyclerView.Adapter<PublishedProductAdapter.ProductAdapterViewHolder>{
    private Activity activity;
    private List<mProduct> list;

    public PublishedProductAdapter(Activity activity, List<mProduct> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_pending_product_list, parent, false);
        return new ProductAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterViewHolder productAdapterViewHolder, int i) {
        final mProduct product = list.get(i);
        productAdapterViewHolder.txtProductName.setText(product.getProductName());
        productAdapterViewHolder.txtProductPrice.setText(product.getProductPrice());
        productAdapterViewHolder.txtProductMrp.setText(product.getProductMrp());
        productAdapterViewHolder.txtProductDiscription.setText(product.getProductCategory());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName,  txtProductDiscription, txtProductMrp;
        EditText txtProductPrice,edtStock;
        ImageView imgOpenMenu;

        public ProductAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txt_product_name_custom_published_product_list);

            txtProductDiscription = itemView.findViewById(R.id.txt_product_dis_custom_published_product_list);
            txtProductMrp = itemView.findViewById(R.id.txt_mrp_custom_published_product_list);
            txtProductPrice = itemView.findViewById(R.id.txt_price_custom_published_product_list);
            imgOpenMenu=itemView.findViewById(R.id.img_drop_down_custom_published_product_list);
            edtStock=itemView.findViewById(R.id.txt_stock_custom_published_product_list);
        }
    }
}
