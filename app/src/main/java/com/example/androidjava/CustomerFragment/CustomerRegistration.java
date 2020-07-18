package com.example.androidjava.CustomerFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.CustomerActivity;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DriverFragment.DriverRegistration1;
import com.example.androidjava.DriverFragment.DriverRegistration2;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.example.androidjava.SellerFragment.SellerRegistration1;
import com.example.androidjava.SellerFragment.SellerRegistration2;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CustomerRegistration extends Fragment implements LocationListener {
    private Double latitude, longitude;
    private boolean gps_enabled, network_enabled;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Bundle b;
    private Button btnDeliveryPartner, btnSeller, btnNext;
    private EditText edtAddress, edtAadharCardNumber, edtName, edtPhoneNumber, edtPinCode, edtEmailId, edtPassword, edtCPassword, edtFullAddress;
    private LinearLayout linearAadharCard;
    private RadioGroup rgAadharCard;
    private RadioButton rbHaveAadhar, rbNotAadhar;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_registration, container, false);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        findViewById(view);
        b = getArguments();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        rgAadharCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbNotAadhar.isChecked()) {
                    linearAadharCard.setVisibility(View.GONE);
                } else {
                    linearAadharCard.setVisibility(View.VISIBLE);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty()) {
                    new RegisterCustomer().execute();
                }


            }
        });
        btnDeliveryPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new DriverRegistration1()).commit();
            }
        });
        btnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new SellerRegistration1()).commit();
            }
        });
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (checkLocationEnabled()) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            location_infromation(location);
        }
        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void location_infromation(Location location) {

        try {
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String country = addresses.get(0).getCountryName();
            String city = addresses.get(0).getLocality();
            edtAddress.setText(city + "," + addresses.get(0).getAdminArea() + "," + country);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkLocationEnabled() {

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(getContext().getResources().getString(R.string.gpsNetworkNotEnabled));
            dialog.setPositiveButton(getContext().getResources().getString(R.string.openLocationSettings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getContext().startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        }
        return gps_enabled || network_enabled;
    }

    private void findViewById(View view) {
        edtAddress = view.findViewById(R.id.edt_address_customer_register);
        edtAadharCardNumber = view.findViewById(R.id.edt_aadhar_card_number_customer_register);
        edtName = view.findViewById(R.id.edt_name_customer_register);
        edtEmailId = view.findViewById(R.id.edt_email_customer_register);
        edtPhoneNumber = view.findViewById(R.id.edt_phone_customer_register);
        edtPinCode = view.findViewById(R.id.edt_pincode_customer_register);
        edtPassword = view.findViewById(R.id.edt_password_customer_register);
        edtCPassword = view.findViewById(R.id.edt_confirm_password_customer_register);
        btnDeliveryPartner = view.findViewById(R.id.reg_main_btn_delivery_partner);
        btnSeller = view.findViewById(R.id.reg_main_btn_seller_partner);
        btnNext = view.findViewById(R.id.btn_register_customer_register);
        linearAadharCard = view.findViewById(R.id.linear_aadhar_customer_register);
        rgAadharCard = view.findViewById(R.id.rg_aadhar_card_customer_register);
        rbHaveAadhar = view.findViewById(R.id.rb_have_aadhar_customer_register);
        rbNotAadhar = view.findViewById(R.id.rb_not_aadhar_customer_register);
        edtFullAddress = view.findViewById(R.id.edt_full_address_customer_registration);
    }

    private boolean checkEmpty() {
        if (edtName.getText().toString().trim().isEmpty() || edtPassword.getText().toString().trim().isEmpty() ||
                edtCPassword.getText().toString().trim().isEmpty() ||
                edtPinCode.getText().toString().trim().isEmpty() ||
                edtPhoneNumber.getText().toString().trim().isEmpty() ||
                edtEmailId.getText().toString().trim().isEmpty() ||
                edtAddress.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Enter All data properly", Toast.LENGTH_LONG).show();
            return false;
        }
        if (rbHaveAadhar.isChecked()) {
            if (edtAadharCardNumber.getText().toString().trim().isEmpty() || edtAadharCardNumber.getText().toString().trim().length() != 12) {
                Toast.makeText(getActivity(), "Enter adhar data properly", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!edtPhoneNumber.getText().toString().trim().matches("^[0-9]*$") || edtPhoneNumber.getText().toString().trim().length() != 10) {
            Toast.makeText(getActivity(), "Enter Phone data properly", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmailId.getText().toString().trim()).matches()) {
            Toast.makeText(getActivity(), "Enter Email data properly", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtPassword.getText().toString().trim().length() < 6) {
            Toast.makeText(getActivity(), "Password Length must be 6 or More", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!edtPassword.getText().toString().trim().equals(edtCPassword.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Enter Password Confirm Password data properly", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    class RegisterCustomer extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            try {
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }


        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("user_type", "Customer"));
            list.add(new BasicNameValuePair("user_name", edtName.getText().toString().trim()));
            list.add(new BasicNameValuePair("user_aadhar_no", edtAadharCardNumber.getText().toString().trim()));
            list.add(new BasicNameValuePair("user_address", edtAddress.getText().toString().trim()));
            list.add(new BasicNameValuePair("user_pincode", edtPinCode.getText().toString().trim()));
            list.add(new BasicNameValuePair("user_phone_no", edtPhoneNumber.getText().toString().trim()));
            list.add(new BasicNameValuePair("user_email", edtEmailId.getText().toString().trim()));
            list.add(new BasicNameValuePair("user_password", edtPassword.getText().toString().trim()));
            list.add(new BasicNameValuePair("full_address", edtFullAddress.getText().toString().trim()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.registration, list);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                if (s.isEmpty()) {
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                    if (s.equals("Registration Successfully")) {
                        mUser user = new mUser("Customer", edtAadharCardNumber.getText().toString().trim(), edtName.getText().toString().trim(), edtAddress.getText().toString().trim(), edtPinCode.getText().toString().trim(), edtPhoneNumber.getText().toString().trim(), edtEmailId.getText().toString().trim(), edtPassword.getText().toString().trim());
                        Gson gson = new Gson();
                        String jsonUser = gson.toJson(user);

                        if (b != null) {
                            String s1 = b.getString("Seller");
                            if (s1.equals("Seller")) {
                                editor.putString("user", jsonUser);
                                editor.commit();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new SellerRegistration2()).commit();
                            } else if (s1.equals("Driver")) {
                                editor.putString("user", jsonUser);
                                editor.commit();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new DriverRegistration2()).commit();
                            }
                        } else {
                            editor.putString("user", jsonUser);
                            editor.commit();
                            startActivity(new Intent(getActivity(), CustomerActivity.class));
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
