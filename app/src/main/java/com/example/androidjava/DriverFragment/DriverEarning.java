package com.example.androidjava.DriverFragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mDriver;
import com.example.androidjava.Model.mDriverOrderCompleted;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DriverEarning extends Fragment {
    private ImageView imgSkipFiveDaysBefore, imgSkipOneDayBefore, imgSkipFiveDaysAfter, imgSkipOneDayAfter;
    private TextView txtCurrentDate, txtTotalEarningRs, txtTotalEarningTrip, txtTotalEarningKm;
    private RecyclerView recycleTotalOrder;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private mDriver driver;
    private String date;
    private Calendar calendar;
    private DateFormat dateFormat;
    private String jsonAllOrder;
    private List<mDriverOrderCompleted> list;
    private Date today;
    private int totalKilometer=0;
    private float totalEarning=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_earning, container, false);
        findViewById(view);
        Calendar calendar=Calendar.getInstance();
        Date currentDate=calendar.getTime();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date=dateFormat.format(currentDate);
        txtCurrentDate.setText(date);
        Log.i("response",date);
        new FetchCompleteOrderDriver().execute();
        imgSkipOneDayAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=returnDateToGive(1);
                Log.i("response",date);
                resetRecycleView();
            }
        });
        imgSkipFiveDaysAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=returnDateToGive(5);
                Log.i("response",date);
                resetRecycleView();
            }
        });
        imgSkipOneDayBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=returnDateToGive(-1);
                Log.i("response",date);
                resetRecycleView();
            }
        });
        imgSkipFiveDaysBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date=returnDateToGive(-5);
                Log.i("response",date);
                resetRecycleView();
            }
        });
        return view;
    }

    private void findViewById(View view) {
        imgSkipFiveDaysBefore = view.findViewById(R.id.img_skip_five_days_before_driver_earing);
        imgSkipOneDayBefore = view.findViewById(R.id.img_skip_one_days_before_driver_earing);
        imgSkipFiveDaysAfter = view.findViewById(R.id.img_skip_five_days_after_driver_earing);
        imgSkipOneDayAfter = view.findViewById(R.id.img_skip_one_days_after_driver_earing);
        txtCurrentDate = view.findViewById(R.id.txt_current_date_driver_earing);
        txtTotalEarningKm = view.findViewById(R.id.txt_total_earning_km_driver_earning);
        txtTotalEarningRs = view.findViewById(R.id.txt_total_earning_rs_driver_earning);
        txtTotalEarningTrip = view.findViewById(R.id.txt_total_earning_trip_driver_earning);
        recycleTotalOrder = view.findViewById(R.id.recycle_total_earning_driver_earning);
        recycleTotalOrder.setHasFixedSize(true);
        recycleTotalOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        sharedPreferences = getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
        gson=new Gson();
        String driver1=sharedPreferences.getString("driver","");
        if(!driver1.equals("")){
            driver=gson.fromJson(driver1,mDriver.class);
        }else{
            Toast.makeText(getActivity(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
        }
        list=new ArrayList<>();
    }

    class FetchCompleteOrderDriver extends AsyncTask<Void, Void, String> {
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
            list.add(new BasicNameValuePair("driverId", driver.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.driverCompletedOrder, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


            try {
                jsonAllOrder=s;
                JSONArray jsonArray = new JSONArray(s);
                totalEarning=0;
                totalKilometer=0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String orderDate=jsonObject.getString("finishedTime");
                    String date1=date;
                    if(orderDate.startsWith(date)){
                        String completedOrder=jsonArray.getString(i);
                        mDriverOrderCompleted driverOrderCompleted=gson.fromJson(completedOrder,mDriverOrderCompleted.class);
                        totalKilometer=totalKilometer+Integer.parseInt(driverOrderCompleted.getTotalKilometer());
                        totalEarning=totalEarning+Float.parseFloat(driverOrderCompleted.getTotalAmount());
                        list.add(driverOrderCompleted);
                    }
                }
                txtTotalEarningTrip.setText(String.valueOf(list.size()));
                txtTotalEarningKm.setText(String.valueOf(totalKilometer)+"Km");
                txtTotalEarningRs.setText("Rs."+String.valueOf(totalEarning));
                DriverCompletedOrderAdapter driverCompletedOrderAdapter=new DriverCompletedOrderAdapter(list, getActivity());
                recycleTotalOrder.setAdapter(driverCompletedOrderAdapter);
            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        list.clear();
        totalEarning=0;
        totalKilometer=0;
        try {
            JSONArray jsonArray = new JSONArray(jsonAllOrder);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String orderDate=jsonObject.getString("finishedTime");
                if(orderDate.startsWith(date)){
                    String completedOrder=jsonArray.getString(i);
                    mDriverOrderCompleted driverOrderCompleted=gson.fromJson(completedOrder,mDriverOrderCompleted.class);
                    totalKilometer=totalKilometer+Integer.parseInt(driverOrderCompleted.getTotalKilometer());
                    totalEarning=totalEarning+Float.parseFloat(driverOrderCompleted.getTotalAmount());
                    list.add(driverOrderCompleted);
                }
            }
            txtTotalEarningTrip.setText(String.valueOf(list.size()));
            txtTotalEarningKm.setText(String.valueOf(totalKilometer)+"Km");
            txtTotalEarningRs.setText("Rs."+String.valueOf(totalEarning));
            DriverCompletedOrderAdapter driverCompletedOrderAdapter=new DriverCompletedOrderAdapter(list, getActivity());
            recycleTotalOrder.setAdapter(driverCompletedOrderAdapter);
        }catch (Exception e){

        }

    }
}
