package com.example.androidjava.SellerFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.SellerOfflineBillItem;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mProduct;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.R;
import com.example.androidjava.SupportInterFace.AddRemoveFunction;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SellerOfflineBilling extends Fragment implements AddRemoveFunction {
    private Button btnCheckOut;
    private SharedPreferences sharedPreferences;
    private List<mProduct> list;
    private TextView txtShopName, txtShopAddress, txtShopPhone, txtShopEmail, txtTotalAmount;
    private AutoCompleteTextView edtBarcode;
    private ImageView btnBarcodeScan;
    private mSeller seller;
    private JSONArray jsonArray;
    private List<String> productName, productId;
    private List<mProduct> mainList;
    private RecyclerView recyleItem;
    private EditText edtCustomerName, edtCustomerAddress, edtCustomerMobile;

    private CheckBox chkNotCustomerDetails;
    private String shopId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_seller_offline_billing2, container, false);
        findViewById(view);
        Gson gson = new Gson();
        String user = sharedPreferences.getString("seller", null);
        seller = gson.fromJson(user, mSeller.class);
//        txtShopName.setText(seller.getShop_name());
//        txtShopEmail.setText(seller.getShop_contact_email());
//        txtShopPhone.setText(seller.getShop_contact_number());
//        txtShopAddress.setText(seller.getShop_pincode());
        shopId = seller.getId();
        final String product = sharedPreferences.getString("publishedProduct", null);
        try {
            jsonArray = new JSONArray(product);
            for (int i = 0; i < jsonArray.length(); i++) {
                Gson gson1 = new Gson();
                mProduct product1 = gson1.fromJson(jsonArray.getString(i), mProduct.class);
                if (product1.getIsPublished().equals("0")) {
                    list.add(product1);
                    productId.add(product1.getId());
                    productName.add(product1.getProductName());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1, productName);
        edtBarcode.setAdapter(arrayAdapter);
        edtBarcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setItemInRecycle();
            }
        });
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_customer_details_order_offline, null);
                builder.setView(view1);

                edtCustomerName = view1.findViewById(R.id.edt_customer_name_offline_billing);
                edtCustomerAddress = view1.findViewById(R.id.edt_customer_address_offline_billing);
                edtCustomerMobile = view1.findViewById(R.id.edt_customer_mobile_offline_billing);
                chkNotCustomerDetails = view1.findViewById(R.id.chk_not_customer_details_offline_billing);
                builder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new PlaceOfflineOrder().execute();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
                IntentIntegrator.forSupportFragment(SellerOfflineBilling.this).initiateScan();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                Toast.makeText(getContext(), "BarCode Scan Cancelled", Toast.LENGTH_SHORT).show();
            }else {
                edtBarcode.setText(intentResult.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void findViewById(View view) {
//        txtShopName = view.findViewById(R.id.txt_shop_name_offline_billing);
//        txtShopAddress = view.findViewById(R.id.txt_shop_address_offline_billing);
//        txtShopPhone = view.findViewById(R.id.txt_shop_phone_offline_billing);
//        txtShopEmail = view.findViewById(R.id.txt_shop_email_offline_billing);
        txtTotalAmount = view.findViewById(R.id.txt_total_amount_bill_offline_billing);
        edtBarcode = view.findViewById(R.id.edt_item_barcode_offline_billing);
        btnBarcodeScan = view.findViewById(R.id.btnBarcodeScan);
        list = new ArrayList<>();
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        productName = new ArrayList<>();
        recyleItem = view.findViewById(R.id.recycle_seller_offline_billing);
        recyleItem.setHasFixedSize(true);
        recyleItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainList = new ArrayList<>();
        productId = new ArrayList<>();
        btnCheckOut = view.findViewById(R.id.btn_check_out_offline_billing);
    }

    @Override
    public void addItem() {
        float totalamount = 0;
        for (int i = 0; i < mainList.size(); i++) {
            mProduct product = mainList.get(i);
            totalamount = totalamount + Float.parseFloat(product.getProductAmount());
        }
        txtTotalAmount.setText(String.valueOf(totalamount));
    }

    @Override
    public void removeItem() {
        float totalamount = 0;
        for (int i = 0; i < mainList.size(); i++) {
            mProduct product = mainList.get(i);
            totalamount = totalamount + Float.parseFloat(product.getProductAmount());
        }
        txtTotalAmount.setText(String.valueOf(totalamount));
    }

    private void setItemInRecycle() {
        String productMainName = edtBarcode.getText().toString().trim();
        boolean productExist = false;
        for (int i = 0; i < mainList.size(); i++) {
            mProduct checkProductExist = mainList.get(i);
            String id = checkProductExist.getProductName();
            if (id.equals(productMainName)) {
                productExist = true;
                break;
            }
        }
        if (!productExist) {
            float amount;
            Toast.makeText(getActivity(), productMainName, Toast.LENGTH_LONG).show();
            for (int i = 0; i < list.size(); i++) {
                mProduct productToBill = list.get(i);
                if (productToBill.getProductName().equals(productMainName)) {
                    productToBill.setProductQut("1");
                    amount = Float.parseFloat(productToBill.getProductQut()) * Float.parseFloat(productToBill.getProductPrice());
                    productToBill.setProductAmount(String.valueOf(amount));
                    if (productToBill.getTypePackage().equals("Loose")) {
                        productToBill.setProductPrice(productToBill.getProductPrice() + "/" + productToBill.getProductUnit());
                    }
                    mainList.add(productToBill);
                }
            }
            addItem();
            SellerOfflineBilling sellerOfflineBilling = this;
            SellerOfflineBillItem sellerOfflineBillItem = new SellerOfflineBillItem(mainList, getActivity(), sellerOfflineBilling);
            recyleItem.setAdapter(sellerOfflineBillItem);

        } else {
            Toast.makeText(getActivity(), "Product is already selected", Toast.LENGTH_LONG).show();
        }
        edtBarcode.setText("");
    }

    class PlaceOfflineOrder extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("ShopId", shopId));
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String orderDate = df.format(c);
            list.add(new BasicNameValuePair("orderDate", orderDate));
            list.add(new BasicNameValuePair("customerName", edtCustomerName.getText().toString().trim()));
            list.add(new BasicNameValuePair("customerAddress", edtCustomerAddress.getText().toString().trim()));
            list.add(new BasicNameValuePair("customerMobile", edtCustomerMobile.getText().toString().trim()));
            list.add(new BasicNameValuePair("orderDetails", ""));
            list.add(new BasicNameValuePair("orderAmount", txtTotalAmount.getText().toString()));
            for (int i = 0; i < mainList.size(); i++) {
                mProduct product = mainList.get(i);
                list.add(new BasicNameValuePair("totalProduct", String.valueOf(i + 1)));
                list.add(new BasicNameValuePair("productId" + i, product.getId()));
                list.add(new BasicNameValuePair("productQut" + i, product.getProductQut()));
                list.add(new BasicNameValuePair("productAmount" + i, product.getProductAmount()));
            }
            return JsonParse.getJsonStringFromUrl(ApiUrls.orderOfflineBilling, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(s);
                String check = jsonObject.getString("order");
                String item = jsonObject.getString("item");
                if (check.equals("Something Went Wrong") ||
                        item.equals("Something Went Wrong")) {
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new SellerOfflineBilling()).commit();
                    Toast.makeText(getActivity(), "Order Done!!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
