package com.example.androidjava.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.CustomerFragment.CustomerMyCart;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mCartProduct;
import com.example.androidjava.R;
import com.example.androidjava.SupportInterFace.AddRemoveFunction;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class CustomerCartItemAdapater extends RecyclerView.Adapter<CustomerCartItemAdapater.CustomerCartItemAdapaterViewHolder> {
    float amount = 0;
    private Activity activity;
    private List<mCartProduct> list;
    private CustomerMyCart customerMyCart;
    private Boolean isDriverAllocated;

    public CustomerCartItemAdapater(Activity activity, List<mCartProduct> list, CustomerMyCart customerMyCart, Boolean isDriverAllocated) {
        this.activity = activity;
        this.list = list;
        this.customerMyCart = customerMyCart;
        this.isDriverAllocated = isDriverAllocated;
    }

    @NonNull
    @Override
    public CustomerCartItemAdapaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_item_list_my_cart_customer_cart, parent, false);
        return new CustomerCartItemAdapaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomerCartItemAdapaterViewHolder holder, final int position) {
        final mCartProduct cartProduct = list.get(position);
        holder.txtItemShortDescription.setText(cartProduct.getProductShortDescription());
        holder.txtItemPrice.setText(cartProduct.getProductPrice());
        holder.txtItemName.setText(cartProduct.getProductName());
        holder.txtItemAmount.setText(cartProduct.getProuductAmount());
        holder.txtItemQut.setText(cartProduct.getProductQut());
        byte[] decodedString = Base64.decode(cartProduct.getProductImg(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imgProduct.setImageBitmap(decodedByte);
        if (isDriverAllocated) {
            holder.imgQutAdd.setVisibility(View.INVISIBLE);
            holder.imgQutRemove.setVisibility(View.INVISIBLE);
            holder.txtRemove.setVisibility(View.INVISIBLE);
            holder.txtSaveForLater.setVisibility(View.INVISIBLE);
        } else {
            holder.imgQutAdd.setVisibility(View.VISIBLE);
            holder.imgQutRemove.setVisibility(View.VISIBLE);
            holder.txtRemove.setVisibility(View.VISIBLE);
            holder.txtSaveForLater.setVisibility(View.VISIBLE);
        }
        holder.imgQutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productQut = Integer.parseInt(cartProduct.getProductQut()) + 1;
                cartProduct.setProductQut(String.valueOf(productQut));
                holder.txtItemQut.setText(cartProduct.getProductQut());
                amount = Float.parseFloat(cartProduct.getProductQut()) * Float.parseFloat(cartProduct.getProductPrice());
                cartProduct.setProuductAmount(String.valueOf(amount));
                holder.txtItemAmount.setText(cartProduct.getProuductAmount());
                ((AddRemoveFunction) customerMyCart).addItem();
                class UpdateCartProductQut extends AsyncTask<Void, Void, String> {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    protected String doInBackground(Void... voids) {
                        List<NameValuePair> list = new ArrayList<>();
                        list.add(new BasicNameValuePair("cartOperation", "UpdateQut"));
                        list.add(new BasicNameValuePair("productQut", cartProduct.getProductQut()));
                        list.add(new BasicNameValuePair("id", cartProduct.getId()));
                        return JsonParse.getJsonStringFromUrl(ApiUrls.tempCartOperation, list);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.startsWith("Error1:")) {
                            Toast.makeText(activity, "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                new UpdateCartProductQut().execute();
            }
        });
        holder.imgQutRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(cartProduct.getProductQut()) > 1) {
                    int productQut = Integer.parseInt(cartProduct.getProductQut()) - 1;
                    cartProduct.setProductQut(String.valueOf(productQut));
                    holder.txtItemQut.setText(cartProduct.getProductQut());
                    amount = Float.parseFloat(cartProduct.getProductQut()) * Float.parseFloat(cartProduct.getProductPrice());
                    cartProduct.setProuductAmount(String.valueOf(amount));
                    holder.txtItemAmount.setText(cartProduct.getProuductAmount());
                    ((AddRemoveFunction) customerMyCart).addItem();
                    class UpdateCartProductQut extends AsyncTask<Void, Void, String> {

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        protected String doInBackground(Void... voids) {
                            List<NameValuePair> list = new ArrayList<>();
                            list.add(new BasicNameValuePair("cartOperation", "UpdateQut"));
                            list.add(new BasicNameValuePair("productQut", cartProduct.getProductQut()));
                            list.add(new BasicNameValuePair("id", cartProduct.getId()));
                            return JsonParse.getJsonStringFromUrl(ApiUrls.tempCartOperation, list);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if (s.startsWith("Error1:")) {
                                Toast.makeText(activity, "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    new UpdateCartProductQut().execute();
                } else {
                    list.remove(position);
                    notifyDataSetChanged();
                    ((AddRemoveFunction) customerMyCart).addItem();
                    class DeleteCartProductQut extends AsyncTask<Void, Void, String> {

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        protected String doInBackground(Void... voids) {
                            List<NameValuePair> list = new ArrayList<>();
                            list.add(new BasicNameValuePair("cartOperation", "DeleteProduct"));
                            list.add(new BasicNameValuePair("id", cartProduct.getId()));
                            return JsonParse.getJsonStringFromUrl(ApiUrls.tempCartOperation, list);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if (s.startsWith("Error1:")) {
                                Toast.makeText(activity, "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    new DeleteCartProductQut().execute();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomerCartItemAdapaterViewHolder extends RecyclerView.ViewHolder {
        TextView  txtItemName, txtItemShortDescription, txtItemPrice, txtItemQut, txtItemAmount;
        ImageView imgQutAdd, imgQutRemove,imgProduct;
        TextView txtSaveForLater,txtRemove;
        public CustomerCartItemAdapaterViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemName = itemView.findViewById(R.id.txt_item_name_item_list_custom_cart);
            txtItemShortDescription = itemView.findViewById(R.id.txt_item_short_description_item_list_custom_cart);
            txtItemPrice = itemView.findViewById(R.id.txt_item_price_item_list_custom_cart);
            txtItemQut = itemView.findViewById(R.id.txt_qut_item_list_custom_cart);
            txtItemAmount = itemView.findViewById(R.id.txt_total_amount_item_list_custom_cart);
            imgQutAdd = itemView.findViewById(R.id.img_add_qut_item_list_custom_cart);
            imgQutRemove = itemView.findViewById(R.id.img_remove_qut_item_list_custom_cart);
            imgProduct=itemView.findViewById(R.id.img_product_item_list_custom_cart);
            txtSaveForLater=itemView.findViewById(R.id.btn_save_for_later_item_list_custom_cart);
            txtRemove=itemView.findViewById(R.id.btn_remove_item_list_custom_cart);
        }
    }

}
