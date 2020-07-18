package com.example.androidjava.CustomerFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.CustomerOneShopProduct;
import com.example.androidjava.Adapter.IssuesAdapter;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mProduct;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.example.androidjava.SupportInterFace.AddRemoveFunction;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CustomerOneShop extends Fragment implements AddRemoveFunction {
    private CustomerOneShop customerOneShop;
    private TextView txtShopName, txtShopRate, txtReportIssues, txtShopDistance;
    private ImageView imgFrontImage, imgShopContact;
    private String shopId;
    private List<mProduct> mainList, pureList;
    private RecyclerView recyclerView;
    private List<String> listIssues;
    private RecyclerView recyleIssues;
    private Button btnReportIssues;
    private List<String> dummyList;
    private SharedPreferences sharedPreferences;
    private double userLat, userLong, shopLat, shopLong, distance;
    private mUser user1;
    private EditText edtSearch;
    private CustomerOneShopProduct productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_one_shop, container, false);
        findViewById(view);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    getActivity().getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
        final Bundle b = getArguments();
        if (b != null) {
            shopId = b.getString("id");
            txtShopName.setText(b.getString("shopName"));
            byte[] decodedString = Base64.decode(b.getString("shopFrontImage"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgFrontImage.setImageBitmap(decodedByte);
            txtShopRate.setText(b.getString("shopRate"));
            String user = sharedPreferences.getString("user", "");
            if (!user.equals("")) {
                Gson gson = new Gson();
                user1 = gson.fromJson(user, mUser.class);
                userLat = JsonParse.getLatitudeToPincode(user1.getPincode(), getActivity());
                userLong = JsonParse.getLongitudeToPincode(user1.getPincode(), getActivity());
                shopLat = Double.parseDouble(b.getString("shopLat"));
                shopLong = Double.parseDouble(b.getString("shopLong"));
                distance = Math.ceil(JsonParse.distance(userLat, userLong, shopLat, shopLong));
                txtShopDistance.setText(String.valueOf(distance));
            }
        }
        new GetShopDetailsWithProducts().execute();
        txtShopDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + String.valueOf(shopLat) + "," + String.valueOf(shopLong) + " (" + b.getString("shopName") + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                getContext().startActivity(intent);
            }
        });
        txtReportIssues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listIssues.clear();
                new FetchIssuess().execute();
                final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_shop_report_an_issues, null);
                dialog.setContentView(view1);
                recyleIssues = view1.findViewById(R.id.recycle_report_an_issues);
                recyleIssues.setHasFixedSize(true);
                recyleIssues.setLayoutManager(new LinearLayoutManager(getActivity()));
                btnReportIssues = view1.findViewById(R.id.btn_report_issues_shop_report);
                btnReportIssues.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ReportIssues().execute();
                        Toast.makeText(getActivity(), "Thanks For Your Review..\n We are taking very seriously your review...", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        imgShopContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0" + b.getString("shopContact")));
                startActivity(intent);
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                List<mProduct> searchList = new ArrayList<>(mainList);
                int a = pureList.size();
                if (s.trim().isEmpty()) {
                    mainList.clear();
                    mainList.addAll(pureList);
                    productAdapter.notifyDataSetChanged();
                } else {
                    mainList.clear();
                    for (int i = 0; i < searchList.size(); i++) {
                        if (searchList.get(i).getProductName().contains(s)) {
                            mainList.add(searchList.get(i));
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                }

            }
        });
        return view;
    }

    private void findViewById(View view) {
        txtShopName = view.findViewById(R.id.txt_shop_name_customer_one_shop);
        txtShopRate = view.findViewById(R.id.txt_shop_rate_customer_one_shop);
        imgFrontImage = view.findViewById(R.id.img_shop_front_img_customer_one_shop);
        imgShopContact = view.findViewById(R.id.img_shop_contact_customer_one_shop);
        mainList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyle_all_products_customer_one_shop);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        txtReportIssues = view.findViewById(R.id.txt_report_issues_one_shop);
        listIssues = new ArrayList<>();
        customerOneShop = this;
        dummyList = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        txtShopDistance = view.findViewById(R.id.txt_get_direction_customer_one_shop);
        edtSearch = view.findViewById(R.id.edt_search_product_customer_one_shop);
        pureList = new ArrayList<>();
    }

    @Override
    public void addItem() {

    }

    @Override
    public void removeItem() {

    }

    class GetShopDetailsWithProducts extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("!!Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("shopId", shopId));
            list.add(new BasicNameValuePair("productType", "PendingProduct"));
            return JsonParse.getJsonStringFromUrl(ApiUrls.fetchProduct, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getContext(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                JSONArray jsonArray = null;
                try {

                    jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        mProduct product = gson.fromJson(jsonArray.getString(i), mProduct.class);
                        if (product.getIsPublished().equals("0"))
                            if (product.getTypePackage().equals("Loose")) {
                                product.setProductPrice(product.getProductPrice() + "/" + product.getProductUnit());
                            }
                        mainList.add(product);
                        pureList.add(product);
                    }
                    productAdapter = new CustomerOneShopProduct(getActivity(), mainList);
                    recyclerView.setAdapter(productAdapter);

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class FetchIssuess extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("issuesOperation", "Fetch"));
            return JsonParse.getJsonStringFromUrl(ApiUrls.issues, list);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String issues = jsonArray.getString(i);
                    listIssues.add(issues);
                }
                IssuesAdapter issuesAdapter = new IssuesAdapter(getActivity(), listIssues, customerOneShop, dummyList);
                recyleIssues.setAdapter(issuesAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ReportIssues extends AsyncTask<Void, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("issuesOperation", "Report Issues"));
            list.add(new BasicNameValuePair("shopId", shopId));
            list.add(new BasicNameValuePair("totalIssues", String.valueOf(dummyList.size())));
            list.add(new BasicNameValuePair("userId", user1.getId()));
            for (int i = 0; i < dummyList.size(); i++) {
                list.add(new BasicNameValuePair("issueName" + i, dummyList.get(i)));
            }
            return JsonParse.getJsonStringFromUrl(ApiUrls.issues, list);
        }
    }
}
