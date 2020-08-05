package com.example.androidjava.DriverFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
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
import androidx.loader.content.CursorLoader;

import com.example.androidjava.R;

import java.io.File;
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
                String path = null;
                if (Build.VERSION.SDK_INT < 11)
                    path = RealPathUtils.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    path = RealPathUtils.getRealPathFromURI_API11to18(getActivity(), data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    path = RealPathUtils.getRealPathFromURI_API19(getActivity(), data.getData());
                // Get the file instance
                File file = new File(path);
                long size = file.length() / 1024;
                Toast.makeText(getActivity(), String.valueOf(size), Toast.LENGTH_SHORT).show();
                if (size < 400) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    strImgLicenceBase64 = getStringImage(bitmap);
                }else{
                    Toast.makeText(getActivity(), "File size must be less than 400 Kb...", Toast.LENGTH_SHORT).show();
                }

            }
        }
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String path = null;
                if (Build.VERSION.SDK_INT < 11)
                    path = RealPathUtils.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    path = RealPathUtils.getRealPathFromURI_API11to18(getActivity(), data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    path = RealPathUtils.getRealPathFromURI_API19(getActivity(), data.getData());
                // Get the file instance
                File file = new File(path);
                long size = file.length() / 1024;
                Toast.makeText(getActivity(), String.valueOf(size), Toast.LENGTH_SHORT).show();
                if (size < 400) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    strImgVehicleNoBase64 = getStringImage(bitmap);
                }else{
                    Toast.makeText(getActivity(), "File size must be less than 400 Kb...", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    public static class RealPathUtils {

        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API19(Context context, Uri uri){
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Images.Media.DATA };

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{ id }, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }


        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj = { MediaStore.Images.Media.DATA };
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if(cursor != null){
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }

        public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }
}
