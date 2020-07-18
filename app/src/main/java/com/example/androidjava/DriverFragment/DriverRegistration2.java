package com.example.androidjava.DriverFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.androidjava.R;

import java.io.IOException;

import static com.example.androidjava.DatabaseConnection.JsonParse.getStringImage;

public class DriverRegistration2 extends Fragment {
    private EditText edtLicenceNo, edtVehicleNo;
    private Button btnChooseLicence, btnChooseVehicle;
    private Button btnNext;
    private Bitmap bitmap;
    private String strImgLicenceBase64, strImgVehicleNoBase64;
    private Spinner spnVehicleType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_registration2, container, false);
        findViewById(view);
        btnChooseVehicle.setOnClickListener(new View.OnClickListener() {
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

        btnChooseLicence.setOnClickListener(new View.OnClickListener() {
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
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverRegistration3 driverRegistration3 = new DriverRegistration3();
                Bundle b = new Bundle();
                b.putString("strImgLicenceBase64", strImgLicenceBase64);
                b.putString("strImgVehicleNoBase64", strImgVehicleNoBase64);
                b.putString("licenceNo", edtLicenceNo.getText().toString().trim());
                b.putString("vehicleNo", edtVehicleNo.getText().toString().trim());
                b.putString("vehicleType", spnVehicleType.getSelectedItem().toString());
                driverRegistration3.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, driverRegistration3).commit();
            }
        });
        return view;
    }

    private void findViewById(View view) {
        edtLicenceNo = view.findViewById(R.id.edt_driving_lincence_number_partner_registration2);
        edtVehicleNo = view.findViewById(R.id.edt_vehicle_lincence_number_partner_registration2);
        btnChooseLicence = view.findViewById(R.id.btn_driving_lincence_number_partner_registration2);
        btnChooseVehicle = view.findViewById(R.id.btn_vehicle_lincence_number_partner_registration2);
        btnNext = view.findViewById(R.id.btn_next_partner_registration2);
        spnVehicleType = view.findViewById(R.id.spn_vehicle_type_partner_registration2);
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
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                } catch (IOException e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            strImgLicenceBase64 = getStringImage(bitmap);
        }
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            strImgVehicleNoBase64 = getStringImage(bitmap);
        }
    }
}
