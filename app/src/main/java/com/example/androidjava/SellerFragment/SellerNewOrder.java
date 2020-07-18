package com.example.androidjava.SellerFragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.SellerAllOrderAdpater;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mOrderCustomerOnline;
import com.example.androidjava.Model.mOrderItem;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SellerNewOrder extends Fragment {
    private SharedPreferences sharedPreferences;
    private List<String> orderId;
    private List<mOrderCustomerOnline> mainList;
    private RecyclerView recyleOrder;
    private SellerNewOrder sellerNewOrder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_new_order, container, false);
        findViewById(view);
        new SellerOrderFetchs().execute();
        return view;
    }


    private void findViewById(View view) {
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        orderId = new ArrayList<>();
        mainList = new ArrayList<>();
        recyleOrder = view.findViewById(R.id.recycle_list_new_order);
        recyleOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyleOrder.setHasFixedSize(true);
        sellerNewOrder=this;
    }

    class SellerOrderFetchs extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        float totalAmount = 0;

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
            list.add(new BasicNameValuePair("orderStatus", "Pending"));
            return JsonParse.getJsonStringFromUrl(ApiUrls.orderHistorySellerOnline, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getActivity(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    Gson gson = new Gson();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String orderItem = jsonArray.getString(i);
                        mOrderItem orderItem1 = gson.fromJson(orderItem, mOrderItem.class);
                        if (orderItem1.getOrder_status().equals("Pending")) {
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
                            } else {
                                mOrderCustomerOnline orderCustomerOnline = new mOrderCustomerOnline();
                                orderCustomerOnline.setTotal_payable_amount(orderItem1.getTotal_item_amount());
                                orderCustomerOnline.setOrder_status(orderItem1.getOrder_status());
                                orderCustomerOnline.setOrder_no(orderItem1.getOrder_id());
                                List<mOrderItem> itemList = new ArrayList<>();
                                itemList.add(orderItem1);
                                orderCustomerOnline.setOrder_item(itemList);
                                orderId.add(orderItem1.getOrder_id());
                                mainList.add(orderCustomerOnline);
                            }
                        }
                        SellerAllOrderAdpater sellerAllOrderAdpater = new SellerAllOrderAdpater(getActivity(), mainList,sellerNewOrder);
                        recyleOrder.setAdapter(sellerAllOrderAdpater);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
