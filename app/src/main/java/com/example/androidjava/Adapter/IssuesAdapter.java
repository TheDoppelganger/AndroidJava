package com.example.androidjava.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.CustomerFragment.CustomerOneShop;
import com.example.androidjava.R;
import com.example.androidjava.SupportInterFace.AddRemoveFunction;

import java.util.List;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.IssuesAdapterViewHolder> {
    Activity activity;
    List<String> list,dummyList;
    CustomerOneShop customerOneShop;
    public IssuesAdapter(Activity activity, List<String> list,CustomerOneShop customerOneShop,List<String> dummyList) {
        this.activity = activity;
        this.list = list;
        this.customerOneShop=customerOneShop;
        this.dummyList=dummyList;
    }

    @NonNull
    @Override
    public IssuesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_shop_report_an_issues_issues, parent, false);
        return new IssuesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IssuesAdapterViewHolder holder, final int position) {
        String issues=list.get(position);
        holder.chkIssueName.setText(issues);
        holder.chkIssueName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(holder.chkIssueName.isChecked())
                    dummyList.add(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class IssuesAdapterViewHolder extends RecyclerView.ViewHolder {
        private CheckBox chkIssueName;

        public IssuesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chkIssueName = itemView.findViewById(R.id.chk_issues_name);
        }
    }
}
