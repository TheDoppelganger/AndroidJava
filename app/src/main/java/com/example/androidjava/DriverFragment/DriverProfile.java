package com.example.androidjava.DriverFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.androidjava.R;
import com.example.androidjava.SellerFragment.SellerProfile;
import com.example.androidjava.SellerFragment.SellerProfileCustomer;
import com.example.androidjava.SellerFragment.SellerProfileShop;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DriverProfile extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_driver_profile, container, false);
        findViewById(view);

        ViewPagerAdapater viewPagerAdapater = new ViewPagerAdapater(getActivity().getSupportFragmentManager());

        viewPagerAdapater.addfragment(new SellerProfileCustomer(), "Customer Profile");
        viewPagerAdapater.addfragment(new DriverProfileDetails(), "Driver Profile");

        viewPager.setAdapter(viewPagerAdapater);

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
    private void findViewById(View view){
        tabLayout = view.findViewById(R.id.tab_layout_profile_deliver);
        viewPager = view.findViewById(R.id.page_viewer_profile_deliver);
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
    }}
