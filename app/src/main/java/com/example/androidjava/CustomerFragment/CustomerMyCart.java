package com.example.androidjava.CustomerFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.CustomerCartItemAdapater;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mCartProduct;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.example.androidjava.SupportInterFace.AddRemoveFunction;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerMyCart extends Fragment implements AddRemoveFunction {
    CustomerMyCart customerMyCart;
    private TextView txtTotalAmount, txtDiscountToAmount, txtDeliveryCharge, txtDiscountToDelivery, txtTotalAmountToPay, txtDeliveryMode, txtWalletWriteAmount, txtWalletWriteDelivery;
    private RecyclerView recycleCartItem;
    private SharedPreferences sharedPreferences;
    private List<mCartProduct> mainList;
    private Button btnPlaceOrder, btnContunieShopping;
    private String message = "Delivery Charge:";
    private String deliveryMode = "Medium";
    private double kilometer = 0;
    private mUser user;
    private boolean isDriverAllocated = false;
    private String orderId, driverOrderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_my_cart, container, false);
        findViewById(view);
        checkDriverAllocated();
        String s = sharedPreferences.getString("user", null);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (isDriverAllocated) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Are You Sure to Not give order at that time?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFragmentManager().popBackStack();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }else
                        getFragmentManager().popBackStack();

                    return true;
                }
                return false;
            }
        });

        Gson gson = new Gson();
        user = gson.fromJson(s, mUser.class);
        new SelectCartItemOfUser().execute();
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDriverAllocated) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                    View view1 = layoutInflater.inflate(R.layout.custom_select_delivery_mode_customer_my_cart, null);
                    builder.setView(view1);
                    final RadioButton rbFast, rbSlow, rbMedium;
                    rbFast = view1.findViewById(R.id.rb_mode_fast_custom_delivery_mode);
                    rbMedium = view1.findViewById(R.id.rb_mode_medium_custom_delivery_mode);
                    rbSlow = view1.findViewById(R.id.rb_mode_slow_custom_delivery_mode);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (rbFast.isChecked()) {
                                deliveryMode = "Fast";
                                new PlaceCustomerOrder().execute();
                            } else if (rbMedium.isChecked()) {
                                deliveryMode = "Medium";
                                new PlaceCustomerOrder().execute();
                            } else if (rbSlow.isChecked()) {
                                deliveryMode = "Slow";
                                new PlaceCustomerOrder().execute();
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    new PlaceCustomerOrder().execute();
                }
            }
        });
        return view;
    }


    private void findViewById(View view) {
        recycleCartItem = view.findViewById(R.id.recyle_customer_my_cart);
        recycleCartItem.setHasFixedSize(true);
        recycleCartItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        sharedPreferences = getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        txtTotalAmount = view.findViewById(R.id.txt_total_amount_customer_my_cart);
        txtDiscountToAmount = view.findViewById(R.id.txt_discount_total_amount_customer_my_cart);
        txtDeliveryCharge = view.findViewById(R.id.txt_total_delivery_charge_customer_my_cart);
        txtDiscountToDelivery = view.findViewById(R.id.txt_total_discount_delivery_charge_customer_my_cart);
        txtTotalAmountToPay = view.findViewById(R.id.txt_total_amount_pay_customer_my_cart);
        mainList = new ArrayList<>();
        customerMyCart = this;
        btnPlaceOrder = view.findViewById(R.id.btn_place_order_customer_my_cart);
        btnContunieShopping = view.findViewById(R.id.btn_countinue_shop_customer_my_cart);
        txtDeliveryMode = view.findViewById(R.id.txt_delivery_mode_customer_my_cart);
        txtWalletWriteAmount = view.findViewById(R.id.txt_wallet_discount_amount_customer_cart);
        txtWalletWriteDelivery = view.findViewById(R.id.txt_wallet_discount_delivery_customer_cart);
    }

    private void checkDriverAllocated() {
        if (isDriverAllocated) {
            txtDeliveryCharge.setVisibility(View.VISIBLE);
            txtDiscountToDelivery.setVisibility(View.VISIBLE);
            txtDeliveryMode.setVisibility(View.VISIBLE);
            txtDiscountToAmount.setVisibility(View.VISIBLE);
            txtWalletWriteAmount.setVisibility(View.VISIBLE);
            txtWalletWriteDelivery.setVisibility(View.VISIBLE);
            txtDeliveryMode.setText(message + "(" + deliveryMode + ")");
        } else {
            txtDeliveryCharge.setVisibility(View.GONE);
            txtDiscountToDelivery.setVisibility(View.GONE);
            txtDeliveryMode.setVisibility(View.GONE);
            txtDiscountToAmount.setVisibility(View.GONE);
            txtWalletWriteAmount.setVisibility(View.GONE);
            txtWalletWriteDelivery.setVisibility(View.GONE);
        }

    }

    @Override
    public void addItem() {
        float totalamount = 0;
        double currentKilometer = 0;
        for (int i = 0; i < mainList.size(); i++) {
            mCartProduct product = mainList.get(i);
            totalamount = totalamount + Float.parseFloat(product.getProuductAmount());
            if (isDriverAllocated) {
                Toast.makeText(getActivity(), String.valueOf(kilometer), Toast.LENGTH_SHORT).show();
                if (deliveryMode.equals("Fast")) {
                    txtDeliveryCharge.setText(String.valueOf(Math.ceil(kilometer * 7)));
                } else if (deliveryMode.equals("Medium")) {
                    txtDeliveryCharge.setText(String.valueOf(Math.ceil(kilometer * 5)));
                } else if (deliveryMode.equals("Slow")) {
                    txtDeliveryCharge.setText(String.valueOf(Math.ceil(kilometer * 3)));
                }
            }
        }
        txtTotalAmount.setText(String.valueOf(totalamount));
        float totalPayable = (Float.parseFloat(txtTotalAmount.getText().toString()) - Float.parseFloat(txtDiscountToAmount.getText().toString()))
                + (Float.parseFloat(txtDeliveryCharge.getText().toString()) - Float.parseFloat(txtDiscountToDelivery.getText().toString()));
        txtTotalAmountToPay.setText(String.valueOf(totalPayable));
    }

    @Override
    public void removeItem() {
        float totalamount = 0;
        for (int i = 0; i < mainList.size(); i++) {
            mCartProduct product = mainList.get(i);
            totalamount = totalamount + Float.parseFloat(product.getProuductAmount());
        }
        txtTotalAmount.setText(String.valueOf(totalamount));
        float totalPayable = (Float.parseFloat(txtTotalAmount.getText().toString()) - Float.parseFloat(txtDiscountToAmount.getText().toString()))
                + (Float.parseFloat(txtDeliveryCharge.getText().toString()) - Float.parseFloat(txtDiscountToDelivery.getText().toString()));
        txtTotalAmountToPay.setText(String.valueOf(totalPayable));
    }

    class SelectCartItemOfUser extends AsyncTask<Void, Void, String> {
        float amount;
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("!!Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            String s = sharedPreferences.getString("user", null);
            Gson gson = new Gson();
            mUser user = gson.fromJson(s, mUser.class);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("userId", user.getId()));
            list.add(new BasicNameValuePair("cartOperation", "FetchCartOperation"));
            return JsonParse.getJsonStringFromUrl(ApiUrls.tempCartOperation, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getActivity(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String product = jsonArray.getString(i);
                        mCartProduct product1 = gson.fromJson(product, mCartProduct.class);
                        amount = Float.parseFloat(product1.getProductQut()) * Float.parseFloat(product1.getProductPrice());
                        product1.setProuductAmount(String.valueOf(amount));
                        mainList.add(product1);
                    }
                    addItem();
                    CustomerCartItemAdapater customerCartItemAdapater = new CustomerCartItemAdapater(getActivity(), mainList, customerMyCart, isDriverAllocated);
                    recycleCartItem.setAdapter(customerCartItemAdapater);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class PlaceCustomerOrder extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            if (isDriverAllocated) {
                progressDialog.setMessage("!!!Order Placing");
                progressDialog.setCancelable(false);
                progressDialog.show();
            } else {
                progressDialog.setMessage("Please Waiting From driver availibility Check");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            if (isDriverAllocated) {
                list.add(new BasicNameValuePair("userId", user.getId()));
                list.add(new BasicNameValuePair("walletDiscount", txtDiscountToAmount.getText().toString()));
                list.add(new BasicNameValuePair("deliveryCharge", txtDeliveryCharge.getText().toString()));
                list.add(new BasicNameValuePair("deliveryDiscount", txtDiscountToDelivery.getText().toString()));
                list.add(new BasicNameValuePair("driverOrderId", driverOrderId));
                list.add(new BasicNameValuePair("orderId", orderId));
                list.add(new BasicNameValuePair("orderConfirmation", "Confirm"));
            } else {
                list.add(new BasicNameValuePair("userId", user.getId()));
                list.add(new BasicNameValuePair("deliveryType", deliveryMode));
                list.add(new BasicNameValuePair("orderDummy", "Order Dummy"));
            }
            return JsonParse.getJsonStringFromUrl(ApiUrls.orderCustomerOnline, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getActivity(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String driverAllocatedStatus = jsonObject.getString("driverAllocated");
                    if(driverAllocatedStatus.equals("Order Placed Successfully")){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_customer, new CustomerMain()).commit();
                    }else {
                        if (driverAllocatedStatus.equals("Driver Allocated")) {
                            isDriverAllocated = true;
                            kilometer = Double.parseDouble(jsonObject.getString("totalKilometer"));
                            checkDriverAllocated();
                            orderId = jsonObject.getString("orderId");
                            driverOrderId = jsonObject.getString("driverOrderId");
                            mainList.clear();
                            new SelectCartItemOfUser().execute();
                        } else if (driverAllocatedStatus.equals("Driver Not Allocated")) {
                            Toast.makeText(getActivity(), "Driver Not Available Right Now!!\nPlease Try Again After 30 Minutes!!", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
