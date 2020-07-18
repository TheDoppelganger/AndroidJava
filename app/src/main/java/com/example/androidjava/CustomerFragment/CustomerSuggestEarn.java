package com.example.androidjava.CustomerFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mUser;
import com.example.androidjava.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.androidjava.DatabaseConnection.JsonParse.getFileName;
import static com.example.androidjava.DatabaseConnection.JsonParse.getStringImage;

public class CustomerSuggestEarn extends Fragment {
    private SharedPreferences sharedPreferences;
    private Button btnSuggestMyIdea;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private EditText edtTitle, edtDescription;
    private TextView txtFileName;
    private CheckBox chkHideName;
    private Button btnSubmit, btnCancel, btnSelectFile;
    private String fileBase64, userId;
    private Gson gson;
    private Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_customer_suggest_earn, container, false);
        findViewById(view);
        gson = new Gson();
        String user = sharedPreferences.getString("user", "");
        if (!user.equals("")) {
            mUser user1 = gson.fromJson(user, mUser.class);
            userId = user1.getId();
            Toast.makeText(getActivity(),userId,Toast.LENGTH_LONG).show();
        }
        ViewPagerAdapater viewPagerAdapater = new ViewPagerAdapater(getActivity().getSupportFragmentManager());
        Bundle bundle=new Bundle();
        bundle.putString("userId",userId);
        CustomerSuggestEarnApprove customerSuggestEarnApprove=new CustomerSuggestEarnApprove();
        customerSuggestEarnApprove.setArguments(bundle);
        CustomerSuggestEarnAwarded customerSuggestEarnAwarded=new CustomerSuggestEarnAwarded();
        customerSuggestEarnAwarded.setArguments(bundle);
        CustomerSuggestEarnRecentlySubmit customerSuggestEarnRecentlySubmit=new CustomerSuggestEarnRecentlySubmit();
        customerSuggestEarnRecentlySubmit.setArguments(bundle);
        viewPagerAdapater.addfragment(customerSuggestEarnApprove, "Approve");
        viewPagerAdapater.addfragment(customerSuggestEarnAwarded, "Awarded");
        viewPagerAdapater.addfragment(customerSuggestEarnRecentlySubmit, "Recently Submit");
        viewPager.setAdapter(viewPagerAdapater);
        tabLayout.setupWithViewPager(viewPager);
        btnSuggestMyIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.custom_customer_suggest_earn_suggested_idea, null);
                builder.setView(view1);
                edtTitle = view1.findViewById(R.id.edt_title_suggested_idea);
                edtDescription = view1.findViewById(R.id.edt_description_suggested_idea);
                txtFileName = view1.findViewById(R.id.txt_file_name_suggested_idea);
                btnSelectFile = view1.findViewById(R.id.btn_insert_file_suggested_idea);
                chkHideName = view1.findViewById(R.id.chk_hide_my_name_suggested_idea);
                btnSubmit = view1.findViewById(R.id.btn_submit_suggested_idea);
                btnCancel = view1.findViewById(R.id.btn_cancel_suggested_idea);
                btnSelectFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            startGallery1();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    2000);
                        }
                    }
                });


                final AlertDialog dialog = builder.create();
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edtTitle.getText().toString().trim().equals("")) {
                            Toast.makeText(getActivity(), "You must enter Title", Toast.LENGTH_LONG).show();
                        } else if (edtDescription.getText().toString().trim().equals("") && txtFileName.getText().toString().equals("Insert File(If you have image or video or any other file)")) {
                            Toast.makeText(getActivity(), "You must enter description otherwise description file", Toast.LENGTH_LONG).show();
                        } else {
                            class SubmitSuggestedIdea extends AsyncTask<Void, Void, String> {
                                ProgressDialog progressDialog = new ProgressDialog(getActivity());

                                @Override
                                protected void onPreExecute() {
                                    progressDialog.setMessage("!!!Please Wait");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                }

                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                protected String doInBackground(Void... voids) {
                                    List<NameValuePair> list = new ArrayList<>();
                                    if (chkHideName.isChecked()) {
                                        list.add(new BasicNameValuePair("hideName", "Yes"));
                                    }else{
                                        list.add(new BasicNameValuePair("hideName", "No"));
                                    }
                                    list.add(new BasicNameValuePair("userId", userId));
                                    list.add(new BasicNameValuePair("suggestedTitle", edtTitle.getText().toString().trim()));
                                    list.add(new BasicNameValuePair("suggestedDescription", edtDescription.getText().toString().trim()));
                                    list.add(new BasicNameValuePair("operationToSuggestEarn","Insert"));
                                    if (txtFileName.getText().toString().equals("Insert File(If you have image or video or any other file)")) {
                                        list.add(new BasicNameValuePair("suggestedFileName", ""));
                                    } else {
                                        list.add(new BasicNameValuePair("suggestedFileName", txtFileName.getText().toString().trim()));
                                        list.add(new BasicNameValuePair("suggestedFile", fileBase64));
                                    }
                                    return JsonParse.getJsonStringFromUrl(ApiUrls.suggestEarn, list);
                                }
                                @Override
                                protected void onPostExecute(String s) {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Thanks For Your Suggestion..\n We will resolve or develop shortly", Toast.LENGTH_LONG).show();
                                }
                            }
                            new SubmitSuggestedIdea().execute();
                        }
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    private void findViewById(View view) {
        btnSuggestMyIdea = view.findViewById(R.id.btn_suggest_my_idea_customer_suggest_earn);
        tabLayout = view.findViewById(R.id.tab_layout_customer_suggest_earn);
        viewPager = view.findViewById(R.id.page_viewer_customer_suggest_earn);
        sharedPreferences = getActivity().getSharedPreferences("Database", Context.MODE_PRIVATE);
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

    private void startGallery1() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 2000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                txtFileName.setText(getFileName(uri,getContext()));
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                } catch (IOException e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            fileBase64 = getStringImage(bitmap);
        }
    }

}
