package com.example.androidjava.SellerFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.LogIn;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.example.androidjava.SellerActivity;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

public class SellerRegistration3 extends Fragment {
    HorizontalStepView horizontalStepView;
    private Bundle bundle;
    private TextView txtShopName, txtShopAddress, txtShopPhone, txtShopEmail, txtShopGST;
    private TextView txtOwnerName, txtOwnerAddress, txtOwnerEmailId, txtOwnerAdharId;
    private String strShopName, strShopPhone, strShopEmail, strShopGST, strShopPinCode, strShopLatitude, strShopLongitude, strShopFrontImage;
    private String strShopCategory, strShopInventoryImage;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView imgShopImage, imgShopInventory;
    private mUser muser;
    private CheckBox ckTermCondition;
    private Button btnSubmit;
    private String sellerId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_registration3, container, false);

        findViewById(view);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bundle = getArguments();
        if (bundle != null) {
            strShopName = bundle.getString("shop_name");
            txtShopName.setText(strShopName);
            strShopPinCode = bundle.getString("shop_pincode");
            txtShopAddress.setText(strShopPinCode);
            strShopLatitude = bundle.getString("shop_latitude");
            strShopLongitude = bundle.getString("shop_longitude");
            strShopFrontImage = bundle.getString("shop_front_image");
            byte[] decodedString = Base64.decode(strShopFrontImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgShopImage.setImageBitmap(decodedByte);
            strShopInventoryImage = bundle.getString("shop_inventory_image");
            byte[] decodedString1 = Base64.decode(strShopInventoryImage, Base64.DEFAULT);
            Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
            imgShopInventory.setImageBitmap(decodedByte1);
            strShopCategory = bundle.getString("shop_category");
            strShopPhone = bundle.getString("shop_contact_number");
            strShopEmail = bundle.getString("shop_email");
            strShopGST = bundle.getString("shop_GST_no");
            txtShopGST.setText(strShopGST);
            txtShopPhone.setText(strShopPhone);
            txtShopEmail.setText(strShopEmail);
            String user = sharedPreferences.getString("user", null);
            Gson gson = new Gson();
            muser = gson.fromJson(user, mUser.class);
            txtOwnerName.setText(muser.getName());
            txtOwnerEmailId.setText(muser.getEmail());
            txtOwnerAdharId.setText(muser.getAdharcardNo());
            txtOwnerAddress.setText(muser.getAddress());
        }
        List<StepBean> list = new ArrayList<>();
        list.add(0, new StepBean("Owner\nDetails", StepBean.STEP_COMPLETED));
        list.add(1, new StepBean("Shop\nDetails", StepBean.STEP_COMPLETED));
        list.add(2, new StepBean("Preview\nSubmit", StepBean.STEP_CURRENT));
        horizontalStepView.setStepViewTexts(list)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#000000"))
                .setStepViewComplectedTextColor(Color.parseColor("#000000"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_black_24dp))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_info_outline_black_24dp));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ckTermCondition.isChecked()) {
                    new SellerRegister().execute();
                } else {
                    Toast.makeText(getActivity(), "Approve terms and condition of your company", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    private void findViewById(View view) {
        horizontalStepView = view.findViewById(R.id.seller_partner_step_view3);
        txtShopPhone = view.findViewById(R.id.txt_shop_contact_number_selller_registration3);
        txtShopAddress = view.findViewById(R.id.txt_shop_address_selller_registration3);
        txtShopEmail = view.findViewById(R.id.txt_shop_email_selller_registration3);
        txtShopGST = view.findViewById(R.id.txt_shop_gst_selller_registration3);
        txtShopName = view.findViewById(R.id.txt_shop_name_selller_registration3);
        txtOwnerName = view.findViewById(R.id.txt_shop_owner_name_selller_registration3);
        txtOwnerAddress = view.findViewById(R.id.txt_shop_owner_address_selller_registration3);
        txtOwnerAdharId = view.findViewById(R.id.txt_shop_owner_aadhar_selller_registration3);
        txtOwnerEmailId = view.findViewById(R.id.txt_shop_owner_email_selller_registration3);
        imgShopImage = view.findViewById(R.id.img_front_selller_registration3);
        imgShopInventory = view.findViewById(R.id.img_inventory_selller_registration3);
        ckTermCondition = view.findViewById(R.id.ck_terms_condition_seller_registration3);
        btnSubmit = view.findViewById(R.id.btn_submit_seller_registration3);
    }

    class SellerRegister extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("user_type", "Seller"));
            list.add(new BasicNameValuePair("user_id", muser.getId()));
            list.add(new BasicNameValuePair("shop_name", strShopName));
            list.add(new BasicNameValuePair("shop_pincode", strShopPinCode));
            list.add(new BasicNameValuePair("shop_latitude", strShopLatitude));
            list.add(new BasicNameValuePair("shop_longitude", strShopLongitude));
            /*list.add(new BasicNameValuePair("shop_front_image", strShopFrontImage));
            list.add(new BasicNameValuePair("shop_inventory_image", strShopInventoryImage));*/
            list.add(new BasicNameValuePair("shop_category", strShopCategory));
            list.add(new BasicNameValuePair("shop_contact_number", strShopPhone));
            list.add(new BasicNameValuePair("shop_email", strShopEmail));
            list.add(new BasicNameValuePair("shop_gst", strShopGST));
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
                    String register = jsonObject.getString("register");
                    if (register.equals("Register Successfully")) {
                        sellerId=String.valueOf(jsonObject.getInt("id"));
                        Toast.makeText(getActivity(), "Register Success", Toast.LENGTH_LONG).show();
                        new UploadImage().execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class UploadImage extends AsyncTask<Void,Void,String>{
        ProgressDialog progressDialog=new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("!!Please Wait\n!!!Uploading Image");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("shop_front_image", strShopFrontImage));
            list.add(new BasicNameValuePair("shop_inventory_image", strShopInventoryImage));
            list.add(new BasicNameValuePair("UploadImage","Image"));
            list.add(new BasicNameValuePair("sellerId",sellerId));
            return JsonParse.getJsonStringFromUrl(ApiUrls.registration,list);
        }

        @Override
        protected void onPostExecute(String s) {
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(getActivity(), "Thank you.. For Registering Here...\n We are here for you 24/7....\nYour future has been bright here...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LogIn.class));
            Toast.makeText(getActivity(), "Please Log In here", Toast.LENGTH_SHORT).show();
        }
    }
}
