package com.frannie.showsaholic;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class HTTPRequest extends AsyncTask<String, Void, String> {

    URL mySearch;

    public HTTPRequest(URL url){
        mySearch=url;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream in = null;
        String result = "";

        try {
            String charset = "UTF-8";

            URLConnection urlConnection = mySearch.openConnection();
            urlConnection.setRequestProperty("Accept-Charset", charset);
            //int status = urlConnection.getResponseCode();
            // Log.v("Parsing: ", urlConnection.getInputStream().toString());
            in = new BufferedInputStream(urlConnection.getInputStream());

        }catch(Exception e){
            Log.e("Error:",e.toString());
        }
        return in.toString();
    }

    protected void onPostExecute(String result) {
        Log.v("HTTPRequest","Downloaded " + result.length() + " bytes");
        Log.v("String ",result);
    }
}
