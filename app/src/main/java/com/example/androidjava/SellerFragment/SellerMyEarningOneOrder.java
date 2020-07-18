package com.example.androidjava.SellerFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.baoyachi.stepview.VerticalStepView;
import com.example.androidjava.R;

import java.util.ArrayList;
import java.util.List;

public class SellerMyEarningOneOrder extends Fragment {
    VerticalStepView verticalStepView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_my_earning_one_order, container, false);
        verticalStepView = view.findViewById(R.id.order_track_frag_step_view);
        List<String> list = new ArrayList<>();
        list.add("Order Accepter\n[10:20AM]");
        list.add("Packed\n[10:30AM]");
        list.add("HandOver\n[10:35AM]");
        list.add("Delivered\n[10:50AM]");
        verticalStepView.setStepsViewIndicatorComplectingPosition(list.size() - 3)
                .reverseDraw(false)
                .setStepViewTexts(list)
                .setLinePaddingProportion(0.85f)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#000000"))
                .setStepViewComplectedTextColor(Color.parseColor("#000000"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#000000"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_black_24dp))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_info_outline_black_24dp));
        return view;
    }
}
