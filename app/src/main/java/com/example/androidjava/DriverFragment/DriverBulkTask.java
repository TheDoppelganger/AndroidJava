package com.example.androidjava.DriverFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mDriver;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DriverBulkTask extends Fragment {
    SharedPreferences sharedPreferences;
    mDriver driver;
    Gson gson;
    private TextView txtNoOrder;
    private RecyclerView recycleBulkTask;
    private LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_bulk_task, container, false);
        findViewById(view);
        return view;
    }
    private void findViewById(View view){
        txtNoOrder=view.findViewById(R.id.txt_no_order_driver_buk_delivery);
        recycleBulkTask=view.findViewById(R.id.recycle_driver_bulk_delivery);
        recycleBulkTask.setHasFixedSize(true);
        recycleBulkTask.setLayoutManager(new LinearLayoutManager(getActivity()));
        sharedPreferences=getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        String s=sharedPreferences.getString("driver","");
        gson=new Gson();
        if(s.equals("")){
            driver=gson.fromJson(s,mDriver.class);
        }
        linearLayout=view.findViewById(R.id.linear_order_allocated_not);
    }



    class FetchBulkTask extends AsyncTask<Void,Void,String>{
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("!!Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("driverId",driver.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.driverCompletedOrder,list);
        }

        @Override
        protected void onPostExecute(String s) {
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
