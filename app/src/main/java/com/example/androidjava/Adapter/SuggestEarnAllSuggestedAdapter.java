package com.example.androidjava.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidjava.Model.mSuggestEarn;
import com.example.androidjava.R;

import java.util.ArrayList;
import java.util.List;

public class SuggestEarnAllSuggestedAdapter extends RecyclerView.Adapter<SuggestEarnAllSuggestedAdapter.SuggestEarnAllSuggestedAdapterViewHolder> {
    private Activity activity;
    private List<mSuggestEarn> list;

    public SuggestEarnAllSuggestedAdapter(Activity activity, List<mSuggestEarn> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public SuggestEarnAllSuggestedAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_sumit_idea_suggest_earn, parent, false);
        return new SuggestEarnAllSuggestedAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestEarnAllSuggestedAdapterViewHolder holder, int position) {
        mSuggestEarn suggestEarn = list.get(position);
        holder.txtIdeaTitle.setText(suggestEarn.getTitle());
        holder.txtIdeaDescription.setText(suggestEarn.getDescription());
        holder.txtAwardedNot.setText(suggestEarn.getWhichType());
        holder.txtName.setText(suggestEarn.getName());
        final String[] arrayList=suggestEarn.getOpinion().split(",");
        holder.txtOpenion.setText("Openion"+"("+String.valueOf(arrayList.length)+")");
        holder.txtOpenion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String openion="";
                for (int i=0;i<arrayList.length;i++){
                    openion=openion+"\n"+String.valueOf(i+1)+") "+arrayList[i]+".";
                }
                final AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setTitle("Openion on this Suggestion");
                builder.setMessage(openion);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
        if (suggestEarn.getWhichType().equals("Awarded")) {
            holder.txtEarnReward.setText(suggestEarn.getMyEarn());
        } else if (suggestEarn.getWhichType().contains("Approved")) {

        } else if (suggestEarn.getWhichType().contains("submit")) {

        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class SuggestEarnAllSuggestedAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView txtIdeaTitle, txtIdeaDescription, txtName, txtAwardedNot, txtEarnReward, txtOpenion;
        private EditText edtYourOpinon;
        private Button btnSendOpinion;
        private ImageView imgLike, imgDisLike;

        public SuggestEarnAllSuggestedAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdeaTitle = itemView.findViewById(R.id.txt_idea_title_custom_submit_idea);
            txtIdeaDescription = itemView.findViewById(R.id.txt_idea_description_custom_submit_idea);
            txtName = itemView.findViewById(R.id.txt_name_custom_submit_idea);
            txtAwardedNot = itemView.findViewById(R.id.txt_approve_awarded_custom_submit_idea);
            txtEarnReward = itemView.findViewById(R.id.txt_total_earn_custom_submit_idea);
            txtOpenion = itemView.findViewById(R.id.txt_opinions_custom_submit_idea);
            edtYourOpinon = itemView.findViewById(R.id.edt_my_opinion_custom_submit_idea);
            btnSendOpinion = itemView.findViewById(R.id.btn_my_opinion_custom_submit_idea);
            imgLike = itemView.findViewById(R.id.img_like_custom_submit_idea);
            imgDisLike = itemView.findViewById(R.id.img_dislike_custom_submit_idea);
        }
    }
}
