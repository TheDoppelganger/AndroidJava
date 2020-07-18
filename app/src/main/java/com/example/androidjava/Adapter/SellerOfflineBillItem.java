package com.example.androidjava.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Model.mProduct;
import com.example.androidjava.R;
import com.example.androidjava.SellerFragment.SellerOfflineBilling;
import com.example.androidjava.SupportInterFace.AddRemoveFunction;

import java.util.List;

public class SellerOfflineBillItem extends RecyclerView.Adapter<SellerOfflineBillItem.SellerOfflineBillItemViewHolder> {
    List<mProduct> list;
    Activity activity;
    SellerOfflineBilling sellerOfflineBilling;
    float amount;

    public SellerOfflineBillItem(List<mProduct> list, Activity activity, SellerOfflineBilling sellerOfflineBilling) {
        this.list = list;
        this.activity = activity;
        this.sellerOfflineBilling = sellerOfflineBilling;
    }

    @NonNull
    @Override
    public SellerOfflineBillItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_seller_offline_order_one_product, parent, false);
        return new SellerOfflineBillItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SellerOfflineBillItemViewHolder holder, final int position) {
        final mProduct product = list.get(position);
        holder.txtProductPrice.setText(product.getProductPrice());
        holder.txtProductName.setText(product.getProductName());
        holder.txtSerialNo.setText(String.valueOf((position + 1)));
        holder.txProductMrp.setText(product.getProductMrp());
        holder.txtProductQut.setText(product.getProductQut());
        holder.txtAmount.setText(product.getProductAmount());
        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productQut = Integer.parseInt(product.getProductQut()) + 1;
                product.setProductQut(String.valueOf(productQut));
                holder.txtProductQut.setText(product.getProductQut());
                amount = Float.parseFloat(product.getProductQut()) * Float.parseFloat(product.getProductPrice());
                product.setProductAmount(String.valueOf(amount));
                holder.txtAmount.setText(product.getProductAmount());
                ((AddRemoveFunction) sellerOfflineBilling).addItem();
            }
        });
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(product.getProductQut()) > 1) {
                    int productQut = Integer.parseInt(product.getProductQut()) - 1;
                    product.setProductQut(String.valueOf(productQut));
                    holder.txtProductQut.setText(product.getProductQut());
                    amount = Float.parseFloat(product.getProductQut()) * Float.parseFloat(product.getProductPrice());
                    product.setProductAmount(String.valueOf(amount));
                    holder.txtAmount.setText(product.getProductAmount());
                    ((AddRemoveFunction) sellerOfflineBilling).addItem();
                } else {
                    list.remove(position);
                    notifyDataSetChanged();
                    ((AddRemoveFunction) sellerOfflineBilling).addItem();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SellerOfflineBillItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSerialNo, txtProductName, txProductMrp, txtProductPrice, txtProductQut, txtAmount;
        private ImageView imgAdd, imgRemove;

        public SellerOfflineBillItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSerialNo = itemView.findViewById(R.id.txt_sl_no_custom_seller_offline_order);
            txtProductName = itemView.findViewById(R.id.txt_item_name_custom_seller_offline_order);
            txProductMrp = itemView.findViewById(R.id.txt_item_mrp_custom_seller_offline_order);
            txtProductPrice = itemView.findViewById(R.id.txt_item_price_custom_seller_offline_order);
            txtProductQut = itemView.findViewById(R.id.txt_item_qui_custom_seller_offline_order);
            txtAmount = itemView.findViewById(R.id.txt_item_amount_custom_seller_offline_order);
            imgAdd = itemView.findViewById(R.id.img_item_add_custom_seller_offline_order);
            imgRemove = itemView.findViewById(R.id.img_item_remove_custom_seller_offline_order);

        }
    }
}
