package com.example.androidjava.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.R;

public class CustomerMyBillsAdapter extends RecyclerView.Adapter<CustomerMyBillsAdapter.CustomerMyBillsViewHolder>{
    Activity activity;

    public CustomerMyBillsAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CustomerMyBillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_customer_my_bills, parent, false);
        return new CustomerMyBillsAdapter.CustomerMyBillsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerMyBillsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CustomerMyBillsViewHolder extends RecyclerView.ViewHolder{

        public CustomerMyBillsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
