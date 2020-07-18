package com.example.androidjava.SellerFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.CustomerFragment.CustomerRegistration;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SellerRegistration1 extends Fragment {
    HorizontalStepView horizontalStepView;
    Double latitude, longitude;
    private LocationManager locationManager;
    private EditText edtId, edtPassword;
    private Button btnVerify;
    private TextView txtSignUp;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_registration1, container, false);
        findViewById(view);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        horizontalStepView = view.findViewById(R.id.seller_partner_step_view1);
        List<StepBean> list = new ArrayList<>();
        list.add(0, new StepBean("Owner\nDetails", StepBean.STEP_CURRENT));
        list.add(1, new StepBean("Shop\nDetails", StepBean.STEP_UNDO));
        list.add(2, new StepBean("Preview\nSubmit", StepBean.STEP_UNDO));
        horizontalStepView.setStepViewTexts(list)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#000000"))
                .setStepViewComplectedTextColor(Color.parseColor("#000000"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_black_24dp))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_info_outline_black_24dp));
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtId.getText().toString().trim().isEmpty() || edtPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter All data properly", Toast.LENGTH_LONG).show();
                } else {
                    new checkAccountRegisterOrNot().execute();
                }
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerRegistration customer_registration1 = new CustomerRegistration();
                Bundle b = new Bundle();
                b.putString("Seller", "Seller");
                customer_registration1.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, customer_registration1).commit();
                Toast.makeText(getActivity(), "First Register Here!!!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void findViewById(View view) {
        edtId = view.findViewById(R.id.edt_id_seller_registration1);
        edtPassword = view.findViewById(R.id.edt_password_seller_registration1);
        btnVerify = view.findViewById(R.id.btn_submit_seller_registration1);
        txtSignUp = view.findViewById(R.id.txt_sign_up_seller_registration1);
    }

    private class checkAccountRegisterOrNot extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("user_id", edtId.getText().toString().trim()));
            list.add(new BasicNameValuePair("user_password", edtPassword.getText().toString().trim()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.login, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getContext(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                JSONArray jsonArray;
                String userString = null;
                try {
                    jsonArray = new JSONArray(s);
                    String s1=jsonArray.getString(0);
                    if (s1.startsWith("{\"error\"")){
                        String[] a = s.split(":");
                        Toast.makeText(getContext(), a[1], Toast.LENGTH_LONG).show();
                        CustomerRegistration customer_registration1 = new CustomerRegistration();
                        Bundle b = new Bundle();
                        b.putString("Seller", "Seller");
                        customer_registration1.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, customer_registration1).commit();
                        Toast.makeText(getActivity(), "First Register Here!!!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    userString = jsonArray.getString(0);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                    editor.putString("user", userString);
                    editor.commit();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new SellerRegistration2()).commit();
                    Toast.makeText(getActivity(), "Please Fill this all value very precisely", Toast.LENGTH_LONG).show();
            }
        }
    }
}
