package com.example.androidjava.SellerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.androidjava.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SellerAllProduct extends Fragment {
    private Button btnAddProduct;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_all_product, container, false);
        findViewById(view);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_seller, new AddProduct()).commit();
            }
        });
        ViewPagerAdapater viewPagerAdapater = new ViewPagerAdapater(getActivity().getSupportFragmentManager());


        viewPagerAdapater.addfragment(new SellerAllProductPublished(), "Published Product");
        viewPagerAdapater.addfragment(new SellerAllProductPending(), "Pending Product");


        viewPager.setAdapter(viewPagerAdapater);

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void findViewById(View view) {
        btnAddProduct = view.findViewById(R.id.btn_add_product_product_list);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.page_viewer);
    }

    class ViewPagerAdapater extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapater(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        public void addfragment(Fragment fragment, String title) {

            fragments.add(fragment);
            titles.add(title);
        }
    }


}
