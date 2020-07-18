package com.example.androidjava.DriverFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DeliveryPartner;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DriverRegistration3 extends Fragment {
    private TextView txtUserName, txtUserAddress, txtUserMobile, txtUserEmail, txtUserAadharCardNo;
    private TextView txtVehicleType, txtVehicleNo, txtLicenceNo;
    private Button btnSubmit;
    private SharedPreferences sharedPreferences;
    private String userString;
    private ImageView imgLicence, imgVehicle;
    private CheckBox chkTerm;
    private mUser user1;
    private String strLicence, strVehicle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_registration3, container, false);
        findViewById(view);
        String user = sharedPreferences.getString("user", "");
        Log.i("response", user);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(user);
            userString = jsonArray.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        user1 = gson.fromJson(userString, mUser.class);
        txtUserName.setText(user1.getName());
        txtUserEmail.setText(user1.getEmail());
        txtUserAddress.setText(user1.getPincode());
        txtUserMobile.setText(user1.getMobile_no());
        txtUserAadharCardNo.setText(user1.getAdharcardNo());
        Bundle b = getArguments();
        if (b != null) {
            txtVehicleNo.setText(b.getString("vehicleNo", ""));
            txtVehicleType.setText(b.getString("vehicleType", ""));
            txtLicenceNo.setText(b.getString("licenceNo", ""));
            strLicence = b.getString("strImgLicenceBase64");
            byte[] decodedString = Base64.decode(strLicence, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgLicence.setImageBitmap(decodedByte);
            strVehicle = b.getString("strImgVehicleNoBase64");
            byte[] decodedString1 = Base64.decode(strVehicle, Base64.DEFAULT);
            Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString.length);
            imgVehicle.setImageBitmap(decodedByte1);
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkTerm.isChecked()) {
                    new DriverRegistration().execute();
                } else {
                    Toast.makeText(getActivity(), "Please Allowed Term and Condition", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void findViewById(View view) {
        txtUserName = view.findViewById(R.id.txt_driver_owner_name_driver_registration3);
        txtUserAadharCardNo = view.findViewById(R.id.txt_driver_owner_aadhar_driver_registration3);
        txtUserAddress = view.findViewById(R.id.txt_driver_owner_address_driver_registration3);
        txtUserMobile = view.findViewById(R.id.txt_driver_owner_mobile_driver_registration3);
        txtUserEmail = view.findViewById(R.id.txt_driver_owner_email_driver_registration3);
        txtVehicleType = view.findViewById(R.id.txt_driver_owner_deliver_driver_registration3);
        txtVehicleNo = view.findViewById(R.id.txt_driver_owner_vehicle_number_driver_registration3);
        txtLicenceNo = view.findViewById(R.id.txt_driver_owner_driving_lincence_driver_registration3);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        imgLicence = view.findViewById(R.id.img_licence_partner_registration3);
        imgVehicle = view.findViewById(R.id.img_vehicle_partner_registration3);
        chkTerm = view.findViewById(R.id.ck_terms_condition_partner_registration3);
    }

    private class DriverRegistration extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("!Partner Registration...\n!!Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("user_type", "Driver"));
            list.add(new BasicNameValuePair("user_id", user1.getId()));
            list.add(new BasicNameValuePair("driver_licence", txtLicenceNo.getText().toString().trim()));
            list.add(new BasicNameValuePair("driver_licence_img", strLicence));
            list.add(new BasicNameValuePair("driver_vehicle_type", txtVehicleType.getText().toString().trim()));
            list.add(new BasicNameValuePair("driver_vehicle_no", txtVehicleNo.getText().toString().trim()));
            list.add(new BasicNameValuePair("driver_vehicle_no_img", strVehicle));
            return JsonParse.getJsonStringFromUrl(ApiUrls.registration, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getActivity(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String message = jsonObject.getString("register");
                    if (message.equals("Register Successfully")) {
                        Toast.makeText(getActivity(), "Register Successfully" + s, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), DeliveryPartner.class));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Some thing went wrong" + s, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
