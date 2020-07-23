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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.example.androidjava.Model.mSeller;
import com.example.androidjava.Model.mVarient;
import com.example.androidjava.R;
import com.example.androidjava.SellerActivity;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.androidjava.DatabaseConnection.JsonParse.getStringImage;

public class AddProduct extends Fragment {
    private Button btnRequest,btnKgVarient,btnLiterVariant,btnOtherVariant;
    private ImageView btnAddImage, imgAddImageToCamera;
    private EditText edtProductName, edtProductBarCode, edtVariant, edtMrp, edtPrice, edtShortDescription, edtDescription;
    private Spinner spnProductCategory;
    private RadioButton rbPacked, rbLoose, rbVariantYes, rbVariantNo, rbReturnable, rbReturnableNot;
    private TextView txtChooseImage;
    private Bitmap bitmap;
    private String strProductImage = "";
    private SharedPreferences sharedPreferences;
    private RecyclerView recycleVariant;
    private LinearLayout linearCategory, linearVarient;
    private ImageView imgVarient;
    private List<mVarient> listVarient;
    private String proudctPacked, strRurnable = "Yes",strProductunit="";
    private int CAMERA_REQUEST = 200;

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

        rbVariantYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rbVariantYes.isChecked())
                    linearVarient.setVisibility(View.VISIBLE);
                else
                    linearVarient.setVisibility(View.GONE);
            }
        });
        imgVarient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listVarient.size() < 5) {
                    mVarient varient = new mVarient("", "", "", "Packed");
                    listVarient.add(varient);
                    AddProductVarient addProductVarient = new AddProductVarient(listVarient, getActivity());
                    recycleVariant.setAdapter(addProductVarient);
                } else {
                    Toast.makeText(getActivity(), "You Can Add 5 Varient Under One Product", Toast.LENGTH_LONG).show();
                }
            }
        });
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
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            strProductImage = getStringImage(bitmap);
            Uri uri = data.getData();
            File file = new File(uri.getPath());
            txtChooseImage.setText(file.getName());
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {

            bitmap = (Bitmap) data.getExtras().get("data");
            strProductImage = getStringImage(bitmap);
            txtChooseImage.setText("Camera Image:1");
        }
    }

    private boolean checkEmpty() {
        if (edtProductName.getText().toString().trim().isEmpty()
                || spnProductCategory.getSelectedItem().toString().equals("Choose shop category?*")
                || edtPrice.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Enter Data Properly", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!rbLoose.isChecked() && !rbPacked.isChecked()) {
            Toast.makeText(getActivity(), "Select Packed or loose any one properly", Toast.LENGTH_LONG).show();
            return false;
        }
        if (rbLoose.isChecked() && edtVariant.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter variant of packed item(Like 1kg,2kg,500gm)", Toast.LENGTH_LONG).show();
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
                        || varient.getProductVarient().equals("")
                        || varient.getProudctPacked().equals("")) {
                    Toast.makeText(getActivity(), "Please Enter Different Varient Properly Of Product", Toast.LENGTH_LONG)
                            .show();
                    return false;
                }


            }
        }
        if(rbPacked.isChecked()){
            if(edtMrp.getText().toString().trim().isEmpty()){
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
        edtVariant = view.findViewById(R.id.edt_variant_add_product);
        edtMrp = view.findViewById(R.id.edt_mrp_add_product);
        edtPrice = view.findViewById(R.id.edt_price_add_product);
        edtShortDescription = view.findViewById(R.id.edt_short_description_add_product);
        edtDescription = view.findViewById(R.id.edt_description_add_product);
        btnAddImage = view.findViewById(R.id.btn_select_image_add_product);

        rbVariantYes = view.findViewById(R.id.rb_variant_yes_add_product);
        rbVariantNo = view.findViewById(R.id.rb_variant_not_add_product);
        linearCategory = view.findViewById(R.id.linear_unit_add_product);
        linearCategory.setVisibility(View.GONE);
        recycleVariant = view.findViewById(R.id.recycle_variant_add_product);
        recycleVariant.setHasFixedSize(true);
        recycleVariant.setLayoutManager(new LinearLayoutManager(getActivity()));
        imgVarient = view.findViewById(R.id.img_add_variant_add_product);
        listVarient = new ArrayList<>();
        linearVarient = view.findViewById(R.id.linear_variant_add_product);
        linearVarient.setVisibility(View.GONE);
        rbReturnable = view.findViewById(R.id.rb_returnable_yes_add_product);
        rbReturnableNot = view.findViewById(R.id.rb_returnable_no_add_product);
        imgAddImageToCamera = view.findViewById(R.id.btn_select_image_camera_add_product);
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
            list.add(new BasicNameValuePair("ProductUnit", strProductunit));
            list.add(new BasicNameValuePair("ProductMrp", edtMrp.getText().toString().trim()));
            list.add(new BasicNameValuePair("ProductPrice", edtPrice.getText().toString().trim()));
            if (listVarient.size() >= 1) {
                for (int i = 0; i < listVarient.size(); i++) {
                    mVarient varient = listVarient.get(i);
                    list.add(new BasicNameValuePair("IsVarientAvailable", String.valueOf(i + 1)));
                    list.add(new BasicNameValuePair("ProductVariant" + i, varient.getProductVarient()));
                    list.add(new BasicNameValuePair("ProductVariantMrp" + i, varient.getProductMrp()));
                    list.add(new BasicNameValuePair("ProductVariantPrice" + i, varient.getProductPrice()));
                    list.add(new BasicNameValuePair("ProductPacked" + i, varient.getProudctPacked()));

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
