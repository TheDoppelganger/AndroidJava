package com.example.androidjava.CustomerFragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.SuggestEarnAllSuggestedAdapter;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mSuggestEarn;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CustomerSuggestEarnApprove extends Fragment {
    String userId;
    List<mSuggestEarn> list;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_customer_suggest_earn_approve, container, false);
        findViewById(view);
        Bundle bundle=getArguments();
        if(bundle != null)
            userId=bundle.getString("userId","");
        if(!userId.equals(""))
            new FetchApprove().execute();
        return view;
    }
    private void findViewById(View view){
        list=new ArrayList<>();
        recyclerView=view.findViewById(R.id.recycle_approve_customer_suggest_earn);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    class FetchApprove extends AsyncTask<Void,Void,String>{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("userId",userId));
            list.add(new BasicNameValuePair("operationToSuggestEarn","FetchApprove"));
            return JsonParse.getJsonStringFromUrl(ApiUrls.suggestEarn,list);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Gson gson=new Gson();
                JSONArray jsonArray=new JSONArray(s);
                for (int i=0;i<jsonArray.length();i++){
                    String suggestEarnString=jsonArray.getString(i);
                    mSuggestEarn suggestEarn=gson.fromJson(suggestEarnString,mSuggestEarn.class);
                    list.add(suggestEarn);
                 }
                SuggestEarnAllSuggestedAdapter suggestEarnAllSuggestedAdapter=new SuggestEarnAllSuggestedAdapter(getActivity(),list);
                recyclerView.setAdapter(suggestEarnAllSuggestedAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
