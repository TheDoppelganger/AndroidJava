package com.example.androidjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidjava.ApiCalling.ApiUrls;
import com.example.androidjava.DatabaseConnection.JsonParse;
import com.example.androidjava.Model.mUser;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LogIn extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView txtSignUp;
    private EditText edtId, edtPassword;
    private Button btnSubmit;

    @Override
    protected void onStart() {
        super.onStart();
        String s = sharedPreferences.getString("user", "");
        String s1 = sharedPreferences.getString("seller", "");
        String s2 = sharedPreferences.getString("driver", "");
        /*if (!s1.equals("")) {
            Gson gson = new Gson();
            mUser user = gson.fromJson(s, mUser.class);
            if (user.getUser_type().equals("Seller")) {
                startActivity(new Intent(LogIn.this, SellerActivity.class));
            }
        }

        if (!s2.equals("")) {
            Gson gson = new Gson();
            mUser user = gson.fromJson(s, mUser.class);
            if (user.getUser_type().equals("Driver")) {
                startActivity(new Intent(LogIn.this, DeliveryPartner.class));
            }
        }
        if (!s.equals("")) {
            Gson gson = new Gson();
            mUser user = gson.fromJson(s, mUser.class);
            Log.i("response", s);
            if (user.getUser_type().equals("Customer")) {
                startActivity(new Intent(LogIn.this, CustomerActivity.class));
            }
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        sharedPreferences = getApplicationContext().getSharedPreferences("Database", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edtId = findViewById(R.id.edt_id_log_in);
        edtPassword = findViewById(R.id.edt_password_log_in);
        btnSubmit = findViewById(R.id.btn_submit_log_in);
        txtSignUp = findViewById(R.id.log_in_sign_up);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, MainActivity.class));
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtId.getText().toString().trim().isEmpty() || edtPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter All data properly", Toast.LENGTH_LONG).show();
                } else {
                    new LogInData().execute();
                }
            }
        });

    }

    class LogInData extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("user_id", edtId.getText().toString().trim()));
            list.add(new BasicNameValuePair("user_password", edtPassword.getText().toString().trim()));
            return JsonParse.getJsonStringFromUrl(ApiUrls.login, list);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("") || s.startsWith("Error1:")) {
                Toast.makeText(getApplicationContext(), "Do not get any Response From database\nTry Again After Some Time" + s, Toast.LENGTH_LONG).show();
            } else {
                JSONArray jsonObject = null;
                try {
                    jsonObject = new JSONArray(s);
                    String s1=jsonObject.getString(0);
                    if (s1.startsWith("{\"error\"")){
                        Toast.makeText(getApplicationContext(), "User Name and password is not match", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String userString = null;
                String sellerString = null;
                String driverString = null;
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    userString = jsonArray.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                Gson gson = new Gson();
                mUser user = gson.fromJson(userString, mUser.class);
                user.setIsSwitch("0");
                editor.putString("user", userString);
                editor.commit();
                if (user.getUser_type().equals("Customer")) {
                    startActivity(new Intent(LogIn.this, CustomerActivity.class));
                } else if (user.getUser_type().equals("Seller")) {
                    try {
                        sellerString = jsonArray.getString(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                    editor.putString("seller", sellerString);
                    editor.commit();
                    startActivity(new Intent(LogIn.this, SellerActivity.class));
                } else if (user.getUser_type().equals("Driver")) {
                    try {
                        driverString = jsonArray.getString(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.putString("driver", driverString);
                    editor.commit();
                    startActivity(new Intent(LogIn.this, DeliveryPartner.class));
                }
            }
        }
    }
}
