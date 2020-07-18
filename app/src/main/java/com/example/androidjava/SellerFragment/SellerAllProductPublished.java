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

import com.example.androidjava.Adapter.ProductAdapter;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mProduct;
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

public class SellerAllProductPublished extends Fragment {
    List<mProduct> productList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_all_product_published, container, false);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        findViewById(view);
        productList = new ArrayList<>();
        new PendingProductDisplay().execute();
        return view;
    }
    private void findViewById(View view) {
        recyclerView = view.findViewById(R.id.recycle_published_product);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    class PendingProductDisplay extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("!Getting Product..\n!!Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            mSeller seller;
            String user = sharedPreferences.getString("seller", null);
            Gson gson = new Gson();
            seller = gson.fromJson(user, mSeller.class);
            list.add(new BasicNameValuePair("productType", "PublishedProduct"));
            list.add(new BasicNameValuePair("shopId", seller.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.fetchProduct, list);
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
                    editor.putString("product", s);
                    editor.commit();
                    jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Gson gson = new Gson();
                        mProduct product = gson.fromJson(jsonArray.getString(i), mProduct.class);
                        if (product.getIsPublished().equals("1"))
                            productList.add(product);
                    }
                    ProductAdapter productAdapter = new ProductAdapter(productList, getActivity());
                    recyclerView.setAdapter(productAdapter);
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
