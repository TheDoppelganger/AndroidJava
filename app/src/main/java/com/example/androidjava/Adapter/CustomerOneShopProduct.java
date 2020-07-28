package com.example.androidjava.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.CustomerFragment.CustomerMyCart;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mProduct;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CustomerOneShopProduct extends RecyclerView.Adapter<CustomerOneShopProduct.CustomerOneShopProductViewHolder> {
    Activity activity;
    List<mProduct> list;
    SharedPreferences sharedPreferences;

    public CustomerOneShopProduct(Activity activity, List<mProduct> list) {
        this.activity = activity;
        this.list = list;
        sharedPreferences = activity.getSharedPreferences("Database", MODE_PRIVATE);
    }

    @NonNull
    @Override
    public CustomerOneShopProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_one_product_one_customer_shop, parent, false);
        return new CustomerOneShopProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOneShopProductViewHolder holder, int position) {
        final mProduct product = list.get(position);
        holder.txtProductName.setText(product.getProductName());
        holder.txtProductPrice.setText("Rs:-" + product.getProductPrice());
        holder.txtProductRating.setText("4");
        byte[] decodedString = Base64.decode(product.getImgProduct(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imgProductImage.setImageBitmap(decodedByte);
        holder.imgProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_view_image, null);
                builder.setView(view1);
                ImageView img = view1.findViewById(R.id.img_for_view_profile);
                byte[] decodedString = Base64.decode(product.getImgProduct(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img.setImageBitmap(decodedByte);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        if (product.getTypePackage().equals("Loose")) {
            holder.txtProductVariant.setText(product.getProductUnit());
        }
        holder.btnAddCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_select_size_varient, null);
                builder.setView(view1);
                {
//                            final RadioButton rbFast, rbSlow, rbMedium;
//                            rbFast = view1.findViewById(R.id.rb_mode_fast_custom_delivery_mode);
//                            rbMedium = view1.findViewById(R.id.rb_mode_medium_custom_delivery_mode);
//                            rbSlow = view1.findViewById(R.id.rb_mode_slow_custom_delivery_mode);
//                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    if (rbFast.isChecked()) {
//                                        deliveryMode = "Fast";
//                                        new CustomerMyCart.PlaceCustomerOrder().execute();
//                                    } else if (rbMedium.isChecked()) {
//                                        deliveryMode = "Medium";
//                                        new CustomerMyCart.PlaceCustomerOrder().execute();
//                                    } else if (rbSlow.isChecked()) {
//                                        deliveryMode = "Slow";
//                                        new CustomerMyCart.PlaceCustomerOrder().execute();
//                                    }
//                                }
//                            });
                }

                ImageView imgProductImage_dia;
                TextView txtProductName_dia, txtProductPrice_dia, txtProductVariant_dia,tv_select_size_varient_dia;
                Button btnAddCartProduct_dia;

                tv_select_size_varient_dia = view1.findViewById(R.id.tv_select_size_varient_dia);
                imgProductImage_dia = view1.findViewById(R.id.img_product_image_custom_one_product_one_customer_shop_dia);
                txtProductName_dia = view1.findViewById(R.id.txt_product_name_custom_one_product_customer_one_shop_dia);
                txtProductPrice_dia = view1.findViewById(R.id.txt_price_custom_one_product_customer_one_shop_dia);
                btnAddCartProduct_dia = view1.findViewById(R.id.btn_add_cart_custom_one_product_customer_one_shop_dia);
                txtProductVariant_dia = view1.findViewById(R.id.txt_variant_custom_one_product_customer_one_shop_dia);
                if (product.getTypePackage().equals("Loose")) {
                    txtProductVariant_dia.setText(product.getProductUnit());
                }





                txtProductName_dia.setText(product.getProductName());
                txtProductPrice_dia.setText("Rs:-" + product.getProductPrice());

                byte[] decodedString = Base64.decode(product.getImgProduct(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgProductImage_dia.setImageBitmap(decodedByte);




                btnAddCartProduct_dia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s = sharedPreferences.getString("user", null);
                        Gson gson = new Gson();
                        final mUser user = gson.fromJson(s, mUser.class);
                        class AddProductToCart extends AsyncTask<Void, Void, String> {
                            ProgressDialog progressDialog = new ProgressDialog(activity);

                            @Override
                            protected void onPreExecute() {
                                progressDialog.setMessage("Please Wait");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            }

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            protected String doInBackground(Void... voids) {
                                List<NameValuePair> list = new ArrayList<>();
                                list.add(new BasicNameValuePair("cartOperation", "AddProduct"));
                                list.add(new BasicNameValuePair("productId", product.getId()));
                                list.add(new BasicNameValuePair("userId", user.getId()));
                                return JsonParse.getJsonStringFromUrl(ApiUrls.tempCartOperation, list);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
                            }
                        }
                        new AddProductToCart().execute();
                    }
                });



                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                tv_select_size_varient_dia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                alertDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomerOneShopProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProductImage;
        TextView txtProductName, txtProductPrice, txtProductRating, txtProductVariant;
        Button btnAddCartProduct;

        public CustomerOneShopProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductImage = itemView.findViewById(R.id.img_product_image_custom_one_product_one_customer_shop);
            txtProductName = itemView.findViewById(R.id.txt_product_name_custom_one_product_customer_one_shop);
            txtProductPrice = itemView.findViewById(R.id.txt_price_custom_one_product_customer_one_shop);
            txtProductRating = itemView.findViewById(R.id.txt_shop_rate_custom_one_product_customer_one_shop);
            btnAddCartProduct = itemView.findViewById(R.id.btn_add_cart_custom_one_product_customer_one_shop);
            txtProductVariant = itemView.findViewById(R.id.txt_variant_custom_one_product_customer_one_shop);
        }
    }
}
