package com.example.androidjava.SellerFragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Adapter.AddProductVarient;
import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.DriverFragment.DriverRegistration2;
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.Model.mVarient;
import com.example.androidjava.R;
import com.example.androidjava.SellerActivity;
import com.example.androidjava.SupportInterFace.AdapterCallItemNumber;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.androidjava.DatabaseConnection.JsonParse.getStringImage;

public class AddProduct extends Fragment implements AdapterCallItemNumber {
    private Button btnRequest, btnKgVarient, btnLiterVariant, btnOtherVariant;
    private ImageView btnAddImage, imgAddImageToCamera, btnBarcodeScan;
    private EditText edtProductName, edtProductBarCode, edtMrp, edtPrice, edtShortDescription, edtDescription, edtOtherUnit,edtVariantSize,edtProductStock;
    private Spinner spnProductCategory;
    private RadioButton rbPacked, rbLoose, rbReturnable;
    private TextView txtChooseImage;
    private Bitmap bitmap;
    private String strProductImage = "";
    private SharedPreferences sharedPreferences;
    private RecyclerView recycleVariant;
    private LinearLayout linearCategory,parentLayout;
    private Button imgVarient;
    private List<mVarient> listVarient;
    private String proudctPacked, strRurnable = "Yes", strProductunit = "K.G.";
    private int CAMERA_REQUEST = 200;
//    private Switch switchFoodItemOrNot;
//    private RadioGroup rgFoodItemYes;
    private AddProduct addProduct;
    private String variantImage;
    private int setVariantListNumber=0;
    private AddProductVarient addProductVarient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        sharedPreferences = getContext().getSharedPreferences("Database", MODE_PRIVATE);
        findViewById(view);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });
        btnAddImage.setOnClickListener(new View.OnClickListener() {
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
        imgAddImageToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            2000);
                }
            }
        });

        btnBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
                IntentIntegrator.forSupportFragment(AddProduct.this).initiateScan();
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty()) {
                    new AddProductDatabase().execute();
                }
            }
        });
        rbReturnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                strRurnable = b ? "Yes" : "No";
            }
        });
        rbLoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (rbLoose.isChecked()) {
                    linearCategory.setVisibility(View.VISIBLE);
                    proudctPacked = "Loose";
                } else {
                    linearCategory.setVisibility(View.GONE);
                    proudctPacked = "Packed";
                }
            }
        });

        imgVarient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listVarient.size() < 5) {
                    mVarient varient = new mVarient("", "", "Packed","","","","Choose Image:");
                    listVarient.add(varient);
                    addProductVarient = new AddProductVarient(listVarient, getActivity(),addProduct);
                    recycleVariant.setAdapter(addProductVarient);
                    parentLayout.invalidate();

                } else {
                    Toast.makeText(getActivity(), "You Can Add 5 Varient Under One Product", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnKgVarient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnKgVarient.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button));
                btnLiterVariant.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button1));
                btnOtherVariant.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button1));
                edtOtherUnit.setVisibility(View.GONE);
                strProductunit="K.G.";
            }
        });
        btnLiterVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnKgVarient.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button1));
                btnLiterVariant.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button));
                btnOtherVariant.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button1));
                edtOtherUnit.setVisibility(View.GONE);
                strProductunit="Liter";
            }
        });
        btnOtherVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnKgVarient.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button1));
                btnLiterVariant.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button1));
                btnOtherVariant.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button));
                edtOtherUnit.setVisibility(View.VISIBLE);
                strProductunit="Other";
            }
        });
//        switchFoodItemOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                rgFoodItemYes.setVisibility(View.VISIBLE);
//                }else{
//                    rgFoodItemYes.setVisibility(View.GONE);
//                }
//            }
//        });
        return view;
    }

    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "onActivityResult: through rv");
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                Toast.makeText(getContext(), "BarCode Scan Cancelled", Toast.LENGTH_SHORT).show();
            }else {
                edtProductBarCode.setText(intentResult.getContents());
            }
        }
        if(requestCode ==1210 && resultCode == Activity.RESULT_OK){
            IntentResult intentResultFromRV = IntentIntegrator.parseActivityResult(1210, resultCode, data);
            if (intentResultFromRV != null){
                if (intentResultFromRV.getContents() == null){
                    Toast.makeText(getContext(), "BarCode Scan Cancelled", Toast.LENGTH_SHORT).show();
                }else {
//                    edtProductBarCode.setText(intentResultFromRV.getContents());
                            Log.d("TAG", "");
                    mVarient varient1=listVarient.get(setVariantListNumber);
                    varient1.setProductBarCode(intentResultFromRV.getContents());
                    addProductVarient.notifyDataSetChanged();
                }
            }
        }

        if(requestCode ==5000 && resultCode == Activity.RESULT_OK){
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
                    variantImage = getStringImage(bitmap);
                    String fileName= JsonParse.getFileName(data.getData(),getActivity());
                    mVarient varient=listVarient.get(setVariantListNumber);
                    varient.setProductVariantImage(variantImage);
                    varient.setProductvariantImageName(fileName);
                    addProductVarient.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(), "File size must be less than 400 Kb...", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
                    strProductImage = getStringImage(bitmap);
                    String fileName= JsonParse.getFileName(data.getData(),getActivity());
                    txtChooseImage.setText(fileName);
                }else{
                    Toast.makeText(getActivity(), "File size must be less than 400 Kb...", Toast.LENGTH_SHORT).show();
                }

            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            strProductImage = getStringImage(bitmap);
            txtChooseImage.setText("Camera Image:1");
        }

    }

    private boolean checkEmpty() {
        if (edtProductName.getText().toString().trim().isEmpty()
                || spnProductCategory.getSelectedItem().toString().equals("Choose shop category?*")
                || edtPrice.getText().toString().trim().isEmpty()
                || edtVariantSize.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Enter Data Properly", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!rbLoose.isChecked() && !rbPacked.isChecked()) {
            Toast.makeText(getActivity(), "Select Packed or loose any one properly", Toast.LENGTH_LONG).show();
            return false;
        }

        if (strProductImage.equals("")) {
            Toast.makeText(getActivity(), "Please select image of product", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (listVarient.size() >= 1) {
            for (int i = 0; i < listVarient.size(); i++) {
                mVarient varient = listVarient.get(i);
                if (varient.getProductMrp().equals("")
                        || varient.getProductPrice().equals("")
                        || varient.getProudctPacked().equals("")
                        || varient.getProductBarCode().equals("")
                        || varient.getProductStock().equals("")) {
                    Toast.makeText(getActivity(), "Please Enter Different Varient Properly Of Product", Toast.LENGTH_LONG)
                            .show();
                    return false;
                }


            }
        }
        if (rbPacked.isChecked()) {
            if (edtMrp.getText().toString().trim().isEmpty()) {
                Toast.makeText(getActivity(), "Enter  mrp Data Properly", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void findViewById(View view) {
        btnRequest = view.findViewById(R.id.add_product_btn_request_product);
        edtProductName = view.findViewById(R.id.edt_name_add_product);
        edtProductBarCode = view.findViewById(R.id.edt_barcode_add_product);
        txtChooseImage = view.findViewById(R.id.edt_choose_Image_add_product);
        spnProductCategory = view.findViewById(R.id.spn_product_type_add_product);
        rbPacked = view.findViewById(R.id.rb_packed_add_product);
        rbLoose = view.findViewById(R.id.rb_loose_add_product);
        edtMrp = view.findViewById(R.id.edt_mrp_add_product);
        edtPrice = view.findViewById(R.id.edt_price_add_product);
//        edtShortDescription = view.findViewById(R.id.edt_short_description_add_product);
//        edtDescription = view.findViewById(R.id.edt_description_add_product);
        btnAddImage = view.findViewById(R.id.btn_select_image_add_product);
        btnKgVarient = view.findViewById(R.id.btn_unit_kg_add_product);
        btnLiterVariant = view.findViewById(R.id.btn_unit_liter_add_product);
        btnOtherVariant = view.findViewById(R.id.btn_unit_other_add_product);
        btnBarcodeScan= view.findViewById(R.id.btnBarcodeScan);
        linearCategory = view.findViewById(R.id.linear_unit_add_product);

        parentLayout = view.findViewById(R.id.parentLayout);

        linearCategory.setVisibility(View.GONE);
        recycleVariant = view.findViewById(R.id.recycle_variant_add_product);
        recycleVariant.setHasFixedSize(true);
        recycleVariant.setLayoutManager(new LinearLayoutManager(getActivity()));
        imgVarient = view.findViewById(R.id.img_add_variant_add_product);
        listVarient = new ArrayList<>();
        rbReturnable = view.findViewById(R.id.rb_returnable_yes_add_product);
        imgAddImageToCamera = view.findViewById(R.id.btn_select_image_camera_add_product);
        edtOtherUnit=view.findViewById(R.id.edt_unit_other_add_product);
//        switchFoodItemOrNot=view.findViewById(R.id.switch_food_item_or_not_add_product);
//        rgFoodItemYes=view.findViewById(R.id.rg_food_item_yes_add_product);
        edtVariantSize=view.findViewById(R.id.edt_variant_size_add_product);
        edtProductStock=view.findViewById(R.id.edt_stock_add_product);
        addProduct=this;
    }

    @Override
    public void inListItemNumber(int number) {
        setVariantListNumber=number;
    }

    private class AddProductDatabase extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Product Adding\nPlease Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("Only_Image", strProductImage));
            list.add(new BasicNameValuePair("ProductName", edtProductName.getText().toString().trim()));
            list.add(new BasicNameValuePair("ProductBarcode", edtProductBarCode.getText().toString().trim()));
            list.add(new BasicNameValuePair("ProductCategory", spnProductCategory.getSelectedItem().toString().trim()));
            list.add(new BasicNameValuePair("ProductPackage", proudctPacked));
            list.add(new BasicNameValuePair("ProductShortDescription", edtShortDescription.getText().toString().trim()));
            list.add(new BasicNameValuePair("ProductDescription", edtDescription.getText().toString().trim()));
            list.add(new BasicNameValuePair("ProductVariantStock",edtProductStock.getText().toString()));
            if(strProductunit.equals("Other")){
                strProductunit=edtOtherUnit.getText().toString().trim();
            }
            list.add(new BasicNameValuePair("ProductUnit", strProductunit));
            list.add(new BasicNameValuePair("ProductMrp", edtMrp.getText().toString().trim()));
            list.add(new BasicNameValuePair("ProductPrice", edtPrice.getText().toString().trim()));
            if (listVarient.size() >= 1) {
                for (int i = 0; i < listVarient.size(); i++) {
                    mVarient varient = listVarient.get(i);
                    list.add(new BasicNameValuePair("IsVarientAvailable", String.valueOf(i + 1)));
                    list.add(new BasicNameValuePair("ProductVariantMrp" + i, varient.getProductMrp()));
                    list.add(new BasicNameValuePair("ProductVariantPrice" + i, varient.getProductPrice()));
                    list.add(new BasicNameValuePair("ProductPacked" + i, varient.getProudctPacked()));
                    list.add(new BasicNameValuePair("ProductVariantBarcode"+i,varient.getProductBarCode()));
                    list.add(new BasicNameValuePair("ProductVariantStock"+i,varient.getProductStock()));
                    list.add(new BasicNameValuePair("ProductVariantImage"+i,varient.getProductVariantImage()));
                    if(!varient.getProudctPacked().equals("Packed")){
                        list.add(new BasicNameValuePair("ProductUnit"+i,varient.getProductUnit()));
                    }
                }
                list.add(new BasicNameValuePair("ProductVariant", "Yes"));
            } else {
                list.add(new BasicNameValuePair("ProductVariant", "No"));
            }
            list.add(new BasicNameValuePair("ProductReturnable", strRurnable));
            String user = sharedPreferences.getString("seller", null);
            Gson gson = new Gson();
            mSeller seller;
            seller = gson.fromJson(user, mSeller.class);
            if (!user.equals(null))
                list.add(new BasicNameValuePair("shop_id", seller.getId()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.addProduct, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getActivity(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), SellerActivity.class));
            }
        }
    }
}
