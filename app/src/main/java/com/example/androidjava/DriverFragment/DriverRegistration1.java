package com.example.androidjava.DriverFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;

import com.baoyachi.stepview.HorizontalStepView;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.CustomerFragment.CustomerRegistration;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DriverRegistration1 extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_driver_registration1, container, false);
        findViewById(view);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                b.putString("Seller", "Driver");
                customer_registration1.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, customer_registration1).commit();
                Toast.makeText(getActivity(), "First Register Here!!!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void findViewById(View view) {
        edtId = view.findViewById(R.id.edt_id_partner_registration1);
        edtPassword = view.findViewById(R.id.edt_password_partner_registration1);
        btnVerify = view.findViewById(R.id.btn_submit_partner_registration1);
        txtSignUp = view.findViewById(R.id.txt_sign_up_partner_registration1);
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
            if (s.equals("{\"error\":\"User name & Password is not exists\"}")) {
                String[] a = s.split(":");
                Toast.makeText(getContext(), a[1], Toast.LENGTH_LONG).show();
                CustomerRegistration customer_registration1 = new CustomerRegistration();
                Bundle b = new Bundle();
                b.putString("Seller", "Driver");
                customer_registration1.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, customer_registration1).commit();
                Toast.makeText(getActivity(), "First Register Here!!!", Toast.LENGTH_LONG).show();
            } else {
                editor.putString("user", s);
                editor.commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new DriverRegistration2()).commit();
                Toast.makeText(getActivity(), "Please Fill this all value very precisely", Toast.LENGTH_LONG).show();
            }
        }
    }
}
