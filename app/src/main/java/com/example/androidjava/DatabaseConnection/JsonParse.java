package com.example.androidjava.DatabaseConnection;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonParse {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getJsonStringFromUrl(String url, List<NameValuePair> list) {
        String result = null;
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            InputStream inputStream = null;
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertHttpResponsetoString(inputStream);
            } else {
                result = "";
            }
        } catch (Exception e) {
            result = "Error1:" + e.getLocalizedMessage();
        }

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String convertHttpResponsetoString(InputStream inputStream) throws IOException {

        String result = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);

        }
        inputStream.close();
        bufferedReader.close();
        result = stringBuilder.toString();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getJsonStringFromUrl(String url, String jsonEntity) {
        String result = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            InputStream inputStream = null;
            StringEntity stringEntity = new StringEntity("json=" + jsonEntity);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertHttpResponsetoString(inputStream);
            } else {
                result = "";
            }
        } catch (Exception e) {

        }
        return result;
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static double getLatitudeToPincode(String pincode, Activity activity) {
        Geocoder geocoder = new Geocoder(activity);
        try {
            List<Address> addresses = geocoder.getFromLocationName(pincode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getLatitude();
            } else {
            }
        } catch (IOException e) {
        }
        return 0.0;
    }

    public static double getLongitudeToPincode(String pincode, Activity activity) {
        Geocoder geocoder = new Geocoder(activity);
        try {
            List<Address> addresses = geocoder.getFromLocationName(pincode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getLongitude();
            } else {
            }
        } catch (IOException e) {
        }
        return 0.0;
    }
    public static String getFileName(Uri uri, Context context) {
        String filename = "";

        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int colIndex;
            colIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            if (colIndex < 0) {
                //These are standard columns for openable URIs. Providers that serve openable URIs must support at least these columns when queried.
                filename = "";
            } else {
                filename = cursor.getString(colIndex);
            }
        } else {
            filename = uri.getLastPathSegment();//needed for Dropbox
        }
        return filename;
    }
}
