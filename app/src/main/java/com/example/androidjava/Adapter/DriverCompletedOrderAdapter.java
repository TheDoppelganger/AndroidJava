package com.example.androidjava.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyachi.stepview.VerticalStepView;
import com.example.androidjava.Model.mDriverOrderCompleted;
import com.example.androidjava.Model.mOrderItem;
import com.example.androidjava.Model.mVarient;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DriverCompletedOrderAdapter extends RecyclerView.Adapter<DriverCompletedOrderAdapter.DriverCompletedOrderAdapterViewHolder> {
    private List<mDriverOrderCompleted> listOrder;
    private Activity activity;
    private Gson gson;

    public DriverCompletedOrderAdapter(List<mDriverOrderCompleted> listOrder, Activity activity) {
        this.listOrder = listOrder;
        this.activity = activity;
        gson = new Gson();
    }

    @NonNull
    @Override
    public DriverCompletedOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_seller_one_order_details, parent, false);
        return new DriverCompletedOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverCompletedOrderAdapterViewHolder holder, int position) {
        int pos = 0;
        float myEarn = 0;
        float giveToShop = 0;
        final mDriverOrderCompleted driverOrderCompleted = listOrder.get(position);
        holder.btnViewOrder.setVisibility(View.INVISIBLE);
        holder.txtOrderId.setText(driverOrderCompleted.getOrder_id());
        holder.txtOrderTotalKilometer.setText(driverOrderCompleted.getTotalKilometer()+"K.M.");
        String orderDate=driverOrderCompleted.getFinishedTime().substring(11);
        int hour=Integer.parseInt(orderDate.substring(0,2));
        int nhour=hour;
        if(hour>12){
             nhour=hour-12;
            String newHour=String.valueOf(nhour);
            if(nhour<10){
                newHour="0"+String.valueOf(nhour);
                orderDate=orderDate.replace(String.valueOf(hour),newHour);
            }else {
                orderDate=orderDate.replace(String.valueOf(hour),String.valueOf(nhour));
            }
        }
        holder.txtOrderDate.setText(orderDate);
        myEarn = Float.parseFloat(driverOrderCompleted.getTotalKilometer()) * 5;
        giveToShop = Float.parseFloat(driverOrderCompleted.getTotalAmount()) - myEarn;
        holder.txtTotalAmount.setText("Rs."+String.valueOf(myEarn));
        switch (driverOrderCompleted.getOrderStatus()) {
            case "Pending":
                pos = 1;
                break;
            case "On Way":
                pos = 2;
                break;
            case "Deliver Partner Hand":
                pos = 3;
                break;
            case "Deliver":
                pos = 4;
                break;
        }
        final int finalPos = pos;
        final float finalGiveToShop = giveToShop;
        final float finalMyEarn = myEarn;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<mOrderItem> orderItemList = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.fragment_seller_new_order_view_order, null);
                builder.setView(view1);
                TextView txtRsToGiveShop, txtMyEarn;
                LinearLayout linearLayout;
                RecyclerView recyclerView = view1.findViewById(R.id.recycle_seller_new_view_order);
                RecyclerView recyclerRating = view1.findViewById(R.id.rating_recycle_view_order);
                recyclerRating.setHasFixedSize(true);
                recyclerRating.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setHasFixedSize(true);
                Button btnPackingComplete = view1.findViewById(R.id.btn_packing_completed_view_order);
                linearLayout = view1.findViewById(R.id.linear_show_driver);
                linearLayout.setVisibility(View.VISIBLE);
                txtRsToGiveShop = view1.findViewById(R.id.txt_give_shop_rs_driver);
                txtMyEarn = view1.findViewById(R.id.txt_my_earning_rs_driver);
                txtRsToGiveShop.setText("Give To Shop"+String.valueOf(finalGiveToShop));
                txtMyEarn.setText("My Earning"+String.valueOf(finalMyEarn));
                btnPackingComplete.setVisibility(View.GONE);
                Button btnContact = view1.findViewById(R.id.btn_packing_completed_view_order);
                btnContact.setVisibility(View.GONE);
                VerticalStepView verticalStepView = view1.findViewById(R.id.vertical_status_order);
                verticalStepView.setVisibility(View.VISIBLE);
                List<String> list1 = new ArrayList<>();
                list1.add("Order Accepter");
                list1.add("Packed");
                list1.add("HandOver");
                list1.add("Delivered");
                verticalStepView.setStepsViewIndicatorComplectingPosition(finalPos)
                        .reverseDraw(false)
                        .setStepViewTexts(list1)
                        .setLinePaddingProportion(0.85f)
                        .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#000000"))
                        .setStepViewComplectedTextColor(Color.parseColor("#000000"))
                        .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))
                        .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#000000"))
                        .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(activity, R.drawable.ic_check_black_24dp))
                        .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(activity, R.drawable.attention))
                        .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(activity, R.drawable.ic_info_outline_black_24dp));
                try {
                    String item = gson.toJson(driverOrderCompleted.getOrderItem());
                    JSONArray jsonArray = new JSONArray(item);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        String orderItem = jsonArray.getString(j);
                        mOrderItem orderItem1 = gson.fromJson(orderItem, mOrderItem.class);
                        orderItemList.add(orderItem1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SellerAllOrderOneViewOrderAdapter sellerAllOrderOneViewOrderAdapter = new SellerAllOrderOneViewOrderAdapter(orderItemList, activity);
                recyclerView.setAdapter(sellerAllOrderOneViewOrderAdapter);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    class DriverCompletedOrderAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderDate, txtOrderId, txtOrderTotalKilometer, txtTotalAmount;
        Button btnViewOrder;

        public DriverCompletedOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderDate = itemView.findViewById(R.id.txt_order_date_custom_seller_one_order);
            txtOrderId = itemView.findViewById(R.id.txt_order_id_custom_seller_one_order);
            txtOrderTotalKilometer = itemView.findViewById(R.id.txt_order_payment_custom_seller_one_order);
            txtTotalAmount = itemView.findViewById(R.id.txt_order_amount_custom_seller_one_order);
            btnViewOrder = itemView.findViewById(R.id.btn_view_order_custom_seller_one_order);

        }
    }
}
