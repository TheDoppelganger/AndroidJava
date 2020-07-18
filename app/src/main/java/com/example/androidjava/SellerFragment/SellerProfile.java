package com.example.androidjava.SellerFragment;

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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SellerProfile extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_profile);
        ViewPager viewPager = view.findViewById(R.id.page_viewer_profile);

        ViewPagerAdapater viewPagerAdapater = new ViewPagerAdapater(getActivity().getSupportFragmentManager());

        viewPagerAdapater.addfragment(new SellerProfileCustomer(), "Customer Profile");
        viewPagerAdapater.addfragment(new SellerProfileShop(), "Shop Profile");

        viewPager.setAdapter(viewPagerAdapater);

        tabLayout.setupWithViewPager(viewPager);
        return view;
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
