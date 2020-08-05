package com.example.androidjava.SellerFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DriverFragment.DriverRegistration2;
import com.example.androidjava.GoogleMap;
import com.example.androidjava.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.androidjava.DatabaseConnection.JsonParse.getStringImage;

public class SellerRegistration2 extends Fragment {
    private HorizontalStepView horizontalStepView;
    private Button btnSubmit;
    private TextView txtChooseLocation;
    private EditText edtShopName, edtShopPinCode, edtShopFrontImage, edtShopInventoryImage;
    private Button btnShopFrontImage, btnShopInventoryImage;
    private EditText edtShopContactNumber, edtShopEmail, edtShopGST;
    private Spinner spnShopCategory;
    private Bitmap bitmap;
    private String strShopFrontImagebase64, strShopInventoryImagebase64, shop_latitude, shop_longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_registration2, container, false);
        findViewById(view);
        final Bundle b = this.getArguments();
        if (b != null) {
            shop_latitude = getArguments().getString("lat");
            shop_longitude = getArguments().getString("lang");
            location_infromation(Double.parseDouble(getArguments().getString("lat")), Double.parseDouble(getArguments().getString("lang")));
        }
        txtChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io=new Intent(getActivity(), GoogleMap.class);
                io.putExtra("Activity","S2");
                startActivity(io);
            }
        });
        List<StepBean> list = new ArrayList<>();
        list.add(0, new StepBean("Owner\nDetails", StepBean.STEP_COMPLETED));
        list.add(1, new StepBean("Shop\nDetails", StepBean.STEP_CURRENT));
        list.add(2, new StepBean("Preview\nSubmit", StepBean.STEP_UNDO));
        horizontalStepView.setStepViewTexts(list)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#000000"))
                .setStepViewComplectedTextColor(Color.parseColor("#000000"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_black_24dp))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_info_outline_black_24dp));

        btnShopFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startGallery();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
            }
        });
        btnShopInventoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startGallery1();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty1()) {
                    SellerRegistration3 sellerRegistration3 = new SellerRegistration3();
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_name", edtShopName.getText().toString().trim());
                    bundle.putString("shop_pincode", edtShopPinCode.getText().toString().trim());
                    bundle.putString("shop_latitude", shop_latitude);
                    bundle.putString("shop_longitude", shop_longitude);
                    bundle.putString("shop_front_image", strShopFrontImagebase64);
                    bundle.putString("shop_inventory_image", strShopInventoryImagebase64);
                    bundle.putString("shop_category", spnShopCategory.getSelectedItem().toString());
                    bundle.putString("shop_contact_number", edtShopContactNumber.getText().toString().trim());
                    bundle.putString("shop_email", edtShopEmail.getText().toString().trim());
                    bundle.putString("shop_GST_no", edtShopGST.getText().toString().trim());
                    sellerRegistration3.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, sellerRegistration3).commit();
                }

            }
        });
        return view;
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    private void startGallery1() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 2000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String path = null;
                if (Build.VERSION.SDK_INT < 11)
                    path = DriverRegistration2.RealPathUtils.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());


                else if (Build.VERSION.SDK_INT < 19)
                    path = DriverRegistration2.RealPathUtils.getRealPathFromURI_API11to18(getActivity(), data.getData());


                else
                    path = DriverRegistration2.RealPathUtils.getRealPathFromURI_API19(getActivity(), data.getData());

                File file = new File(path);
                long size = file.length() / 1024;

                if (size < 400) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    strShopFrontImagebase64 = getStringImage(bitmap);
                    String fileName= JsonParse.getFileName(data.getData(),getActivity());
                    edtShopFrontImage.setText(fileName);
                }else{
                    Toast.makeText(getActivity(), "File size must be less than 400 Kb...", Toast.LENGTH_SHORT).show();
                }

            }
        }
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String path = null;
                if (Build.VERSION.SDK_INT < 11)
                    path = DriverRegistration2.RealPathUtils.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    path = DriverRegistration2.RealPathUtils.getRealPathFromURI_API11to18(getActivity(), data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    path = DriverRegistration2.RealPathUtils.getRealPathFromURI_API19(getActivity(), data.getData());
                // Get the file instance
                File file = new File(path);
                long size = file.length() / 1024;

                if (size < 400) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    strShopInventoryImagebase64 = getStringImage(bitmap);
                    String fileName= JsonParse.getFileName(data.getData(),getActivity());
                    edtShopInventoryImage.setText(fileName);
                }else{
                    Toast.makeText(getActivity(), "File size must be less than 400 Kb...", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void findViewById(View view) {
        btnSubmit = view.findViewById(R.id.btn_next_seller_registration2);
        horizontalStepView = view.findViewById(R.id.seller_partner_step_view2);
        txtChooseLocation = view.findViewById(R.id.seller_2_txt_choose_location);
        edtShopContactNumber = view.findViewById(R.id.edt_shop_contact_number_seller_registration2);
        edtShopEmail = view.findViewById(R.id.edt_shop_email_id_seller_registration2);
        btnShopFrontImage = view.findViewById(R.id.btn_shop_image_seller_registration2);
        edtShopGST = view.findViewById(R.id.edt_shop_gst_seller_registration2);
        btnShopInventoryImage = view.findViewById(R.id.btn_shop_inventory_image_seller_registration2);
        edtShopName = view.findViewById(R.id.edt_shop_name_seller_registration2);
        edtShopPinCode = view.findViewById(R.id.edt_shop_pincode_seller_registration2);
        edtShopFrontImage = view.findViewById(R.id.edt_shop_front_image_seller_registration2);
        edtShopInventoryImage = view.findViewById(R.id.edt_shop_inventory_image_seller_registration2);
        spnShopCategory = view.findViewById(R.id.spn_shop_type_seller_registration2);
    }

    private void location_infromation(Double latitude, Double longitude) {
        try {
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            txtChooseLocation.setText(addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Please choose perfect Location", Toast.LENGTH_LONG).show();

        }
    }

    private boolean checkEmpty1() {
        if (edtShopPinCode.getText().toString().trim().isEmpty()
                || spnShopCategory.getSelectedItem().toString().equals("Choose shop category?*")) {
            Toast.makeText(getActivity(), "Enter All Data Properly", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!edtShopContactNumber.getText().toString().trim().isEmpty()) {
            if (!edtShopContactNumber.getText().toString().trim().matches("^[0-9]*$") || edtShopContactNumber.getText().toString().trim().length() != 10) {
                Toast.makeText(getActivity(), "Enter Phone Data Properly", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!edtShopEmail.getText().toString().trim().isEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(edtShopEmail.getText().toString().trim()).matches()) {
                Toast.makeText(getActivity(), "Enter Email data properly", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (edtShopFrontImage.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Insert Front Image Of Your Shop", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
