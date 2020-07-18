package com.example.androidjava.DriverFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.SellerAllOrderOneViewOrderAdapter;
import com.example.androidjava.Adapter.TripInDetailDriverAdapter;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mDeliveryInstrusction;
import com.example.androidjava.Model.mDriver;
import com.example.androidjava.Model.mOrderItem;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DriverMyTask extends Fragment {
    private boolean isBulkYes = false;
    private TextView txtTripDistance, txtTripPickUpPoints, txtTotalEarning;
    private Button btnTripSkip, btnReadyToTrip;
    private SharedPreferences sharedPreferences;
    private boolean acceptedOrNot;
    private LinearLayout linearFoundamentalTripDetails, linearNotAllocatedOrder, linearTripInDetails;
    private RecyclerView recyclerDriverTripDetails;
    private TextView txtOrderId;
    private Button btnStartDriving;
    private List<mDeliveryInstrusction> list, currentRunningItem;
    private String tripId, isAccepted;
    private List<mOrderItem> orderItemList;
    private Gson gson;
    private boolean isAutoAccepted;
    private mDriver driver;
    public static boolean isClickDrication=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_my_task, container, false);
        findViewById(view);
        String bulkYes=driver.getIsBulkDelivery();
        isBulkYes=bulkYes.equals("0") ? false : true;
        if (isBulkYes) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverBulkTask()).commit();
        }
        linearTripInDetails.setVisibility(View.GONE);
        linearFoundamentalTripDetails.setVisibility(View.GONE);
        linearNotAllocatedOrder.setVisibility(View.GONE);
        isAutoAccepted=true;
        if(isAutoAccepted)
            isAccepted = "Accepted";
        else
            isAccepted="Pending";
        new TripFindToDatabase().execute();
        btnReadyToTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptedOrNot = true;
                isAccepted = "Accepted";
                new OrderAcceptedOrNot().execute();
            }
        });
        btnTripSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptedOrNot = false;
                new OrderAcceptedOrNot().execute();
            }
        });
        btnStartDriving.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (btnStartDriving.getText().equals("Start Driving")) {
                    for (int i = 0; i < list.size(); i++) {
                        mDeliveryInstrusction deliveryInstrusction = list.get(i);
                        if (deliveryInstrusction.getDirectionShow().equals("0")) {
                            deliveryInstrusction.setDirectionShow("1");
                            list.remove(i);
                            list.add(i, deliveryInstrusction);
                            TripInDetailDriverAdapter tripInDetailDriverAdapter = new TripInDetailDriverAdapter(getActivity(), list);
                            recyclerDriverTripDetails.setAdapter(tripInDetailDriverAdapter);
                            currentRunningItem.clear();
                            currentRunningItem.add(deliveryInstrusction);

                            btnStartDriving.setText("Reached At Destination");
                            break;
                        }
                    }
                } else if (btnStartDriving.getText().equals("Reached At Destination")) {
                    if(isClickDrication) {
                        for (int i = 0; i < currentRunningItem.size(); i++) {
                            mDeliveryInstrusction deliveryInstrusction = currentRunningItem.get(i);
                            if (deliveryInstrusction.getShopOrCustomer().equals("Customer")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                                View view1 = layoutInflater.inflate(R.layout.fragment_seller_new_order_view_order, null);
                                builder.setView(view1);
                                RecyclerView recyclerView = view1.findViewById(R.id.recycle_seller_new_view_order);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setHasFixedSize(true);
                                Button btnPackingComplete = view1.findViewById(R.id.btn_packing_completed_view_order);
                                final AlertDialog dialog = builder.create();
                                for (int i2 = 0; i2 < list.size(); i2++) {
                                    mDeliveryInstrusction mDeliveryInstrusction2 = list.get(i2);
                                    if (mDeliveryInstrusction2.getShopOrCustomer().equals("Shop")) {
                                        try {
                                            String item = gson.toJson(mDeliveryInstrusction2.getShopItem());
                                            JSONArray jsonArray = new JSONArray(item);
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                String orderItem = jsonArray.getString(j);
                                                mOrderItem orderItem1 = gson.fromJson(orderItem, mOrderItem.class);
                                                orderItemList.add(orderItem1);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        SellerAllOrderOneViewOrderAdapter sellerAllOrderOneViewOrderAdapter = new SellerAllOrderOneViewOrderAdapter(orderItemList, getActivity());
                                        recyclerView.setAdapter(sellerAllOrderOneViewOrderAdapter);
                                        btnPackingComplete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new CompleteOrderDelivery().execute();
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                orderItemList.clear();
                                            }
                                        });
                                    }
                                }
                                dialog.show();
                            } else if (deliveryInstrusction.getShopOrCustomer().equals("Shop")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                                View view1 = layoutInflater.inflate(R.layout.fragment_seller_new_order_view_order, null);
                                builder.setView(view1);
                                RecyclerView recyclerView = view1.findViewById(R.id.recycle_seller_new_view_order);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setHasFixedSize(true);
                                Button btnPackingComplete = view1.findViewById(R.id.btn_packing_completed_view_order);
                                try {
                                    String item = gson.toJson(deliveryInstrusction.getShopItem());
                                    JSONArray jsonArray = new JSONArray(item);
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        String orderItem = jsonArray.getString(j);
                                        mOrderItem orderItem1 = gson.fromJson(orderItem, mOrderItem.class);
                                        orderItemList.add(orderItem1);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                SellerAllOrderOneViewOrderAdapter sellerAllOrderOneViewOrderAdapter = new SellerAllOrderOneViewOrderAdapter(orderItemList, getActivity());
                                recyclerView.setAdapter(sellerAllOrderOneViewOrderAdapter);
                                final AlertDialog dialog = builder.create();
                                btnPackingComplete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        btnStartDriving.setText("Start Driving");
                                        isClickDrication=false;
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        orderItemList.clear();
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }else{
                        Toast.makeText(getActivity(),"You must go direction and Reach at destination",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }

    private void findViewById(View view) {
        txtTripDistance = view.findViewById(R.id.txt_total_kilometer_driver_my_task);
        txtTripPickUpPoints = view.findViewById(R.id.txt_total_pick_up_point_driver_my_task);
        txtTotalEarning = view.findViewById(R.id.txt_total_earning_driver_my_task);
        btnTripSkip = view.findViewById(R.id.btn_skip_driver_my_task);
        btnReadyToTrip = view.findViewById(R.id.btn_Ready_to_pick_driver_my_task);
        sharedPreferences = getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        linearFoundamentalTripDetails = view.findViewById(R.id.linear_order_allocated);
        linearNotAllocatedOrder = view.findViewById(R.id.linear_order_allocated_not);
        linearTripInDetails = view.findViewById(R.id.linear_trip_in_details);
        recyclerDriverTripDetails = view.findViewById(R.id.recycle_order_driver_my_task);
        recyclerDriverTripDetails.setHasFixedSize(true);
        recyclerDriverTripDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        txtOrderId = view.findViewById(R.id.txt_order_id_driver_my_task);
        btnStartDriving = view.findViewById(R.id.btn_start_driving_driver_my_task);
        list = new ArrayList<>();
        currentRunningItem = new ArrayList<>();
        orderItemList = new ArrayList<>();
        gson = new Gson();
        String s = sharedPreferences.getString("driver", null);
        driver = gson.fromJson(s, mDriver.class);
    }


    class TripFindToDatabase extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Gson gson = new Gson();

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {

            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("driverId", driver.getId()));
            list.add(new BasicNameValuePair("isAccepted", isAccepted));
            return JsonParse.getJsonStringFromUrl(ApiUrls.getDriverOrder, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getActivity(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.getString("message").equals("Order available")) {
                        String checkConfirmedOrNot = jsonObject.getString("isConfirmed");
                        tripId = String.valueOf(jsonObject.getInt("tripId"));
                        if (checkConfirmedOrNot.equals("1")) {
                            linearFoundamentalTripDetails.setVisibility(View.GONE);
                            linearNotAllocatedOrder.setVisibility(View.GONE);
                            linearTripInDetails.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = jsonObject.getJSONArray("shop");

                            for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                                mDeliveryInstrusction deliveryInstrusction = gson.fromJson(jsonArray.getString(i1), mDeliveryInstrusction.class);
                                list.add(deliveryInstrusction);

                            }
                            JSONArray jsonArray1 = jsonObject.getJSONArray("customerDetails");
                            for (int i2 = 0; i2 < jsonArray1.length(); i2++) {
                                mDeliveryInstrusction deliveryInstrusction = gson.fromJson(jsonArray1.getString(i2), mDeliveryInstrusction.class);
                                list.add(deliveryInstrusction);
                            }
                            TripInDetailDriverAdapter tripInDetailDriverAdapter = new TripInDetailDriverAdapter(getActivity(), list);
                            recyclerDriverTripDetails.setAdapter(tripInDetailDriverAdapter);
                        } else {
                            linearFoundamentalTripDetails.setVisibility(View.VISIBLE);
                            tripId = String.valueOf(jsonObject.getInt("tripId"));
                            Toast.makeText(getActivity(), tripId, Toast.LENGTH_LONG).show();
                            String totalDistance = jsonObject.getString("totalKilometerToshop");
                            txtTripDistance.setText(totalDistance);
                            txtTotalEarning.setText(String.valueOf(Double.parseDouble(totalDistance) * 2));
                            txtTripPickUpPoints.setText(jsonObject.getString("totalPickUpPoints"));
                        }
                    }else{
                        linearFoundamentalTripDetails.setVisibility(View.GONE);
                        linearNotAllocatedOrder.setVisibility(View.VISIBLE);
                        linearTripInDetails.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class OrderAcceptedOrNot extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please Wait!!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("driverOperation", "UpdateOrderStatus"));
            list.add(new BasicNameValuePair("driverAllocatedId", tripId));
            if (acceptedOrNot) {
                list.add(new BasicNameValuePair("driverAllocatedStatus", "Accepted"));
            } else {
                list.add(new BasicNameValuePair("driverAllocatedStatus", "CancelByDriver"));
            }
            return JsonParse.getJsonStringFromUrl(ApiUrls.updateDriverOrderStatus, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (acceptedOrNot) {
                Toast.makeText(getActivity(), "Waiting For customer order conformation!!!!", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(10000);
                    new TripFindToDatabase().execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Wait untill next order not allocated to you!!!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    class CompleteOrderDelivery extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(), tripId, Toast.LENGTH_SHORT).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {

            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("driverAllocatedId", tripId));
            list.add(new BasicNameValuePair("driverOperation","FinishOrder"));
            return JsonParse.getJsonStringFromUrl(ApiUrls.updateDriverOrderStatus,list);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"Thanks For your Servicing",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_delivery_partner, new DriverDashboard()).commit();
        }
    }
    private void isIn10KiloMeter(){
        final mDeliveryInstrusction deliveryInstrusction=currentRunningItem.get(0);
        CountDownTimer countDownTimer=new CountDownTimer(10000,5000) {
            @Override
            public void onTick(long l) {
                String s = sharedPreferences.getString("driver", null);
                driver = gson.fromJson(s, mDriver.class);
                double distance=JsonParse.distance(Double.parseDouble(deliveryInstrusction.getLatitude()),Double.parseDouble(deliveryInstrusction.getLongitude()),Double.parseDouble(driver.getLatitude()),Double.parseDouble(driver.getLatitude()));
                if(distance<=5)
                    Toast.makeText(getContext(),String.valueOf(distance),Toast.LENGTH_LONG).show();
                    return;
            }

            @Override
            public void onFinish() {
                String s = sharedPreferences.getString("driver", null);
                driver = gson.fromJson(s, mDriver.class);
                double distance=JsonParse.distance(Double.parseDouble(deliveryInstrusction.getLatitude()),Double.parseDouble(deliveryInstrusction.getLongitude()),Double.parseDouble(driver.getLatitude()),Double.parseDouble(driver.getLatitude()));
                if(distance<=5)
                    return;
                else
                    isIn10KiloMeter();
            }
        }.start();
    }
}