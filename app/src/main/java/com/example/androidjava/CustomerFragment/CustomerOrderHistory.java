package com.example.androidjava.CustomerFragment;

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

import com.example.androidjava.Adapter.CustomerOrderAdapter;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mOrderCustomerOnline;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CustomerOrderHistory extends Fragment {
    private RecyclerView recycleOrderHistory;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<mOrderCustomerOnline> mainList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_order_history, container, false);
        findViewById(view);
        SetRecycleView();
        return view;
    }

    private void findViewById(View view) {
        recycleOrderHistory = view.findViewById(R.id.recycle_order_list_customer_order_history);
        recycleOrderHistory.setHasFixedSize(true);
        recycleOrderHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Database", MODE_PRIVATE);
        mainList = new ArrayList<>();
        editor=sharedPreferences.edit();
    }

    class GetOrderHistoryUser extends AsyncTask<Void, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            String s = sharedPreferences.getString("user", null);
            Gson gson = new Gson();
            mUser user = gson.fromJson(s, mUser.class);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("userId", user.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.orderHistoryCustomerOnline, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getActivity(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                editor.putString("orderHistory",s);
                editor.commit();
                SetRecycleView();
            }
        }
    }
    private void SetRecycleView(){
        String orderHistory=sharedPreferences.getString("orderHistory","");
        if(orderHistory.equals("")){
            new GetOrderHistoryUser().execute();
        }else{
            try {
                JSONArray jsonArray = new JSONArray(orderHistory);
                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    mOrderCustomerOnline orderCustomerOnline = gson.fromJson(jsonArray.getString(i), mOrderCustomerOnline.class);
                    mainList.add(orderCustomerOnline);
                }
                CustomerOrderAdapter customerOrderAdapter = new CustomerOrderAdapter(getActivity(), mainList);
                recycleOrderHistory.setAdapter(customerOrderAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
