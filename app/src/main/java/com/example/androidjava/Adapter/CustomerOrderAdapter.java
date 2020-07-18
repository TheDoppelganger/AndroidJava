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
import com.baoyachi.stepview.bean.StepBean;
import com.example.androidjava.Model.mOrderCustomerOnline;
import com.example.androidjava.Model.mOrderItem;
import com.example.androidjava.Model.mRating;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.CustomerOrderAdapterViewHolder> {
    Activity activity;
    List<mOrderCustomerOnline> list;
    Gson gson;
    public CustomerOrderAdapter(Activity activity, List<mOrderCustomerOnline> list) {
        this.activity = activity;
        this.list = list;
        gson=new Gson();
    }

    @NonNull
    @Override
    public CustomerOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_one_order_item_customer_order_history, parent, false);
        return new CustomerOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrderAdapterViewHolder holder, int position) {
        int pos = 0;
        final mOrderCustomerOnline orderCustomerOnline = list.get(position);
        holder.txtOrderAmount.setText(orderCustomerOnline.getTotal_payable_amount());
        holder.txtOrderStatus.setText(orderCustomerOnline.getOrder_status());
        holder.txtOrderRemark.setText(orderCustomerOnline.getPayment_mode());
        holder.txtOrderDate.setText(orderCustomerOnline.getOrder_date());
        holder.txtOrderId.setText(orderCustomerOnline.getOrder_no() + "/" + orderCustomerOnline.getId());
        switch (orderCustomerOnline.getOrder_status()){
            case "Pending":
                pos=1;
                holder.linearLayout.setBackgroundColor(Color.parseColor("#32a852"));
                break;
            case "On Way":
                pos=2;
                holder.linearLayout.setBackgroundColor(Color.parseColor("#32e3da"));
                break;
            case "Deliver Partner Hand":
                pos=3;
                holder.linearLayout.setBackgroundColor(Color.parseColor("#4ce6e0"));
                break;
            case "Deliver":
                pos=4;
                holder.linearLayout.setBackgroundColor(Color.parseColor("#9fed7e"));
                break;
        }
        final int finalPos = pos;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<mOrderItem> orderItemList=new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.fragment_seller_new_order_view_order, null);
                builder.setView(view1);
                RecyclerView recyclerView = view1.findViewById(R.id.recycle_seller_new_view_order);
                RecyclerView recyclerRating=view1.findViewById(R.id.rating_recycle_view_order);
                recyclerRating.setHasFixedSize(true);
                recyclerRating.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setHasFixedSize(true);
                Button btnPackingComplete = view1.findViewById(R.id.btn_packing_completed_view_order);
                btnPackingComplete.setVisibility(View.GONE);
                Button btnContact=view1.findViewById(R.id.btn_packing_completed_view_order);
                btnContact.setVisibility(View.GONE);
                VerticalStepView verticalStepView=view1.findViewById(R.id.vertical_status_order);
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
                if(finalPos==4){
                    if(orderCustomerOnline.getiSReviewGive().equals("0")){
                        List<mRating> list2=new ArrayList<>();
                        String item = gson.toJson(orderCustomerOnline.getOrder_item());
                        String sellerString=gson.toJson(orderCustomerOnline.getSellerList());
                        JSONArray jsonArray = null;
                        JSONArray jsonArray1=null;
                        try {
                            jsonArray = new JSONArray(item);
                            jsonArray1=new JSONArray(sellerString);
                            for (int j = 0; j < jsonArray.length(); j++) {
                                String dummyItem=jsonArray.getString(j);
                                mOrderItem mainItem=gson.fromJson(dummyItem,mOrderItem.class);
                                mRating rating=new mRating();
                                rating.setName(mainItem.getProduct_name());
                                rating.setShopOrDriverOrProductId(mainItem.getProduct_id());
                                rating.setShopOrDriverOrProduct("Product");
                                list2.add(rating);
                            }
                            for(int k=0;k<jsonArray1.length();k++){
                                String dummyItem=jsonArray1.getString(k);
                                mSeller mainItem=gson.fromJson(dummyItem,mSeller.class);
                                mRating rating=new mRating();
                                rating.setName(mainItem.getShop_name());
                                rating.setShopOrDriverOrProductId(mainItem.getId());
                                rating.setShopOrDriverOrProduct("Seller");
                                list2.add(rating);
                            }
                            mRating rating=new mRating();
                            rating.setName(orderCustomerOnline.getDriver_name());
                            rating.setShopOrDriverOrProductId(orderCustomerOnline.getDriver_id());
                            rating.setShopOrDriverOrProduct("Driver");
                            list2.add(rating);
                            ShowRateAdapter showRateAdapter=new ShowRateAdapter(activity,list2);
                            recyclerRating.setAdapter(showRateAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    String item = gson.toJson(orderCustomerOnline.getOrder_item());
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
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomerOrderAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtOrderStatus, txtOrderRemark, txtOrderAmount;
        LinearLayout linearLayout;
        public CustomerOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txt_order_id_custom_one_order_item);
            txtOrderDate = itemView.findViewById(R.id.txt_order_date_custom_one_order_item);
            txtOrderStatus = itemView.findViewById(R.id.txt_order_status_custom_one_order_item);
            txtOrderRemark = itemView.findViewById(R.id.txt_order_remark_custom_one_order_item);
            txtOrderAmount = itemView.findViewById(R.id.txt_order_amount_custom_one_order_item);
            linearLayout=itemView.findViewById(R.id.linear_custom_one_order_item);
        }
    }
}
