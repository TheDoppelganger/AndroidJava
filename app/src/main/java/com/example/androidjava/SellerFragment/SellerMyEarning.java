package com.example.androidjava.SellerFragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.DriverCompletedOrderAdapter;
import com.example.androidjava.Adapter.SellerAllOrderAdpater;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mDriverOrderCompleted;
import com.example.androidjava.Model.mOrderCustomerOnline;
import com.example.androidjava.Model.mOrderItem;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SellerMyEarning extends Fragment {
    private TextView txtTotalEarning,txtTotalOrder,txtTotalReturnOrder,txtCurrentDate;
    private RecyclerView recycleTotalOder;
    private SharedPreferences sharedPreferences;
    private List<String> orderId;
    private List<mOrderCustomerOnline> mainList;
    private int returnOrder=0;
    private float myEarning=0f;
    private String date;
    private Calendar calendar;
    private DateFormat dateFormat;
    private ImageView imgSkipFiveDaysBefore, imgSkipOneDayBefore, imgSkipFiveDaysAfter, imgSkipOneDayAfter;
    private Date today;
    private String jsonAllOrder;
    private Gson gson ;
    private SellerNewOrder sellerNewOrder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_my_earning, container, false);
        findViewById(view);
        calendar=Calendar.getInstance();
        Date currentDate=calendar.getTime();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date=dateFormat.format(currentDate);
        txtCurrentDate.setText(date);
        new SellerOrderFetchs().execute();
        imgSkipOneDayAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=returnDateToGive(1);
                resetRecycleView();
            }
        });
        imgSkipFiveDaysAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=returnDateToGive(5);
                resetRecycleView();
            }
        });
        imgSkipOneDayBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=returnDateToGive(-1);
                resetRecycleView();
            }
        });
        imgSkipFiveDaysBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=returnDateToGive(-5);
                resetRecycleView();
            }
        });
        return view;
    }
    private void findViewById(View view){
        gson = new Gson();
        imgSkipFiveDaysBefore = view.findViewById(R.id.img_skip_five_days_before_seller_earing);
        imgSkipOneDayBefore = view.findViewById(R.id.img_skip_one_days_before_seller_earing);
        imgSkipFiveDaysAfter = view.findViewById(R.id.img_skip_five_days_after_seller_earing);
        imgSkipOneDayAfter = view.findViewById(R.id.img_skip_one_days_after_seller_earing);
        txtTotalEarning=view.findViewById(R.id.txt_total_earning_seller_my_earning);
        txtTotalOrder=view.findViewById(R.id.txt_total_order_seller_my_earning);
        txtTotalReturnOrder=view.findViewById(R.id.txt_total_return_order_seller_my_earning);
        recycleTotalOder=view.findViewById(R.id.txt_total_view_order_seller_my_earning);
        recycleTotalOder.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleTotalOder.setHasFixedSize(true);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        orderId = new ArrayList<>();
        mainList = new ArrayList<>();
        txtCurrentDate=view.findViewById(R.id.txt_current_date_seller_earing);
    }
    class SellerOrderFetchs extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("!!Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            String user = sharedPreferences.getString("seller", null);
            Gson gson = new Gson();
            mSeller seller;
            seller = gson.fromJson(user, mSeller.class);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("shopId", seller.getId()));
            list.add(new BasicNameValuePair("orderStatus", "All"));
            return JsonParse.getJsonStringFromUrl(ApiUrls.orderHistorySellerOnline, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getActivity(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                jsonAllOrder=s;
                resetRecycleView();
            }
        }
    }
    private String returnDateToGive(int howMuch) {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int year=Integer.parseInt(txtCurrentDate.getText().toString().substring(0,4));
        int month=Integer.parseInt(txtCurrentDate.getText().toString().substring(5,7))-1;
        int day=Integer.parseInt(txtCurrentDate.getText().toString().substring(8,10));
        calendar.set(year,month,day);
        String localDate = "";
        Date date;
        long duration;
        switch (howMuch) {
            case 0:
                date=calendar.getTime();
                localDate = dateFormat.format(date);
                today=date;
                txtCurrentDate.setText(localDate);
                break;
            case 1:
                calendar.add(Calendar.DAY_OF_YEAR,1);
                date=calendar.getTime();
                localDate = dateFormat.format(date);
                txtCurrentDate.setText(localDate);
                /*duration=date.getTime()-today.getTime();
                if(TimeUnit.MICROSECONDS.toDays(duration) == 1){
                    txtCurrentDate.setText("Tomorrow");
                }else if(TimeUnit.MICROSECONDS.toDays(duration) == -1){
                    txtCurrentDate.setText("Yesterday");
                }else {
                    txtCurrentDate.setText(localDate);
                }*/
                break;
            case -1:
                calendar.add(Calendar.DAY_OF_YEAR,-1);
                date=calendar.getTime();
                localDate = dateFormat.format(date);
                txtCurrentDate.setText(localDate);
                /*duration=date.getTime()-today.getTime();
                if(TimeUnit.MICROSECONDS.toDays(duration) == 1){
                    txtCurrentDate.setText("Tomorrow");
                }else if(TimeUnit.MICROSECONDS.toDays(duration) == -1){
                    txtCurrentDate.setText("Yesterday");
                }else {

                }*/
                break;
            case 5:
                calendar.add(Calendar.DAY_OF_YEAR,5);
                date=calendar.getTime();
                localDate = dateFormat.format(date);
                txtCurrentDate.setText(localDate);
                /*duration=date.getTime()-today.getTime();*/
                /*if(TimeUnit.MICROSECONDS.toDays(duration) == 1){
                    txtCurrentDate.setText("Tomorrow");
                }else if(TimeUnit.MICROSECONDS.toDays(duration) == -1){
                    txtCurrentDate.setText("Yesterday");
                }else {
                    txtCurrentDate.setText(localDate);
                }*/
                break;
            case -5:
                calendar.add(Calendar.DAY_OF_YEAR,-5);
                date=calendar.getTime();
                localDate = dateFormat.format(date);
                txtCurrentDate.setText(localDate);
                /*duration=date.getTime()-today.getTime();*/
                /*if(TimeUnit.MICROSECONDS.toDays(duration) == 1){
                    txtCurrentDate.setText("Tomorrow");
                }else if(TimeUnit.MICROSECONDS.toDays(duration) == -1){
                    txtCurrentDate.setText("Yesterday");
                }else {
                    txtCurrentDate.setText(localDate);
                }*/
                break;
        }
        return localDate;
    }
    private void resetRecycleView(){
        mainList.clear();
        myEarning=0;
        returnOrder=0;
        float totalAmount = 0;
        try {
            JSONArray jsonArray = new JSONArray(jsonAllOrder);
            for (int i = 0; i < jsonArray.length(); i++) {
                String orderItem = jsonArray.getString(i);
                mOrderItem orderItem1 = gson.fromJson(orderItem, mOrderItem.class);
                String orderDate=orderItem1.getOrder_date();
                String dummy=date;
                if (orderDate.startsWith(date)) {
                    if (!orderItem1.getOrder_status().equals("Pending")) {
                        if (orderId.contains(orderItem1.getOrder_id())) {
                            for (int j = 0; j < orderId.size(); j++) {
                                String currentId = orderId.get(j);
                                if (currentId.equals(orderItem1.getOrder_id())) {
                                    for (int i1 = 0; i1 < mainList.size(); i1++) {
                                        mOrderCustomerOnline orderCustomerOnline = mainList.get(i1);
                                        if (orderCustomerOnline.getOrder_no().equals(orderItem1.getOrder_id())) {
                                            List<mOrderItem> dummyList = orderCustomerOnline.getOrder_item();
                                            dummyList.add(orderItem1);
                                            orderCustomerOnline.setOrder_item(dummyList);
                                            totalAmount = Float.parseFloat(orderCustomerOnline.getTotal_payable_amount())
                                                    + Float.parseFloat(orderItem1.getTotal_item_amount());
                                            orderCustomerOnline.setTotal_payable_amount(String.valueOf(totalAmount));
                                        }
                                    }
                                }
                            }
                            myEarning = myEarning + totalAmount;
                        } else {
                            mOrderCustomerOnline orderCustomerOnline = new mOrderCustomerOnline();
                            orderCustomerOnline.setTotal_payable_amount(orderItem1.getTotal_item_amount());
                            orderCustomerOnline.setOrder_status(orderItem1.getOrder_status());
                            orderCustomerOnline.setOrder_no(orderItem1.getOrder_id());
                            orderCustomerOnline.setOrder_date(orderItem1.getOrder_date());
                            List<mOrderItem> itemList = new ArrayList<>();
                            itemList.add(orderItem1);
                            orderCustomerOnline.setOrder_item(itemList);
                            orderId.add(orderItem1.getOrder_id());
                            mainList.add(orderCustomerOnline);
                        }
                    } else if (orderItem1.getOrder_status().equals("return")) {
                        returnOrder++;
                    }
                }
            }
            SellerAllOrderAdpater sellerAllOrderAdpater = new SellerAllOrderAdpater(getActivity(), mainList,sellerNewOrder);
            recycleTotalOder.setAdapter(sellerAllOrderAdpater);
            txtTotalOrder.setText(String.valueOf(mainList.size()));
            txtTotalReturnOrder.setText(String.valueOf(returnOrder));
            txtTotalEarning.setText(String.valueOf(myEarning));
        } catch (JSONException e) {
            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
