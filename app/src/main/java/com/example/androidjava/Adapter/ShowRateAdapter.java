package com.example.androidjava.Adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mRating;
import com.example.androidjava.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ShowRateAdapter extends RecyclerView.Adapter<ShowRateAdapter.ShowRateAdapterViewHolder>{
    private Activity activity;
    private List<mRating> list;

    public ShowRateAdapter(Activity activity, List<mRating> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ShowRateAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_rating_view, parent, false);
        return new ShowRateAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowRateAdapterViewHolder holder, final int position) {
        final mRating rating=list.get(position);
        holder.txtName.setText(rating.getName());
        if(rating.getRating() != null){
            holder.ratingBar.setNumStars(Integer.parseInt(rating.getRating()));
            holder.edtComments.setText(rating.getRatingComments());
            holder.edtComments.setEnabled(false);
        }
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating1, boolean fromUser) {
                rating.setRating(String.valueOf(rating1));
                list.remove(position);
                list.add(position,rating);
                holder.btnSubmitReview.setVisibility(View.VISIBLE);
            }
        });
        holder.edtComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                rating.setRatingComments(holder.edtComments.getText().toString());
                list.remove(position);
                list.add(position,rating);
                holder.btnSubmitReview.setVisibility(View.VISIBLE);
            }
        });
        holder.btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    class SubmitReview extends AsyncTask<Void,Void,String>{
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        protected String doInBackground(Void... voids) {
                            List<NameValuePair> list=new ArrayList<>();
                            if(rating.getShopOrDriverOrProduct().equals("Product")){
                                list.add(new BasicNameValuePair("productId",rating.getShopOrDriverOrProductId()));
                            }else if(rating.getShopOrDriverOrProduct().equals("Driver")){
                                list.add(new BasicNameValuePair("driverId",rating.getShopOrDriverOrProductId()));
                            }else if(rating.getShopOrDriverOrProduct().equals("Seller")){
                                list.add(new BasicNameValuePair("sellerId",rating.getShopOrDriverOrProductId()));
                            }
                            list.add(new BasicNameValuePair("rate",String.valueOf(holder.ratingBar.getRating())));
                            list.add(new BasicNameValuePair("ratingComments",holder.edtComments.getText().toString()));
                            return JsonParse.getJsonStringFromUrl(ApiUrls.submitReview,list);
                        }
                    }
                    new SubmitReview().execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ShowRateAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private RatingBar ratingBar;
        private EditText edtComments;
        private Button btnSubmitReview;
        public ShowRateAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txt_name_custom_rate_view);
            ratingBar=itemView.findViewById(R.id.rate_bar_custom_rate_view);
            edtComments=itemView.findViewById(R.id.edt_rate_comment_custom_rate_view);
            btnSubmitReview=itemView.findViewById(R.id.btn_rate_submit_custom_rate_view);
        }
    }
}
