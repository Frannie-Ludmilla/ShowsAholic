package com.frannie.showsaholic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.frannie.showsaholic.SearchRespParser;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francesca on 14/06/2015.
 */
public class SearchScreen extends Activity {


    //protected String[] prova = new String[]{"Post 1", "Post 2", "Post 3", "Post 4", "Post 5", "Post 6"};
    private SearchItem[] listData;
    protected String URL_SEARCH= "http://services.tvrage.com/feeds/search.php?show=";
    protected URL myUrl;
    String searchedShow;
    ProgressBar progress;
    ListView listView;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                searchedShow= null;
            } else {
               searchedShow= extras.getString("selectedSeries");
            }
        } else {
            searchedShow= (String) savedInstanceState.getSerializable("selectedSeries");
        }
        progress=(ProgressBar)this.findViewById(R.id.progressbar_loading_search);
        listView = (ListView)this.findViewById(R.id.searchListView);
        ctx=this;

        try {
            myUrl=new URL(URL_SEARCH + searchedShow);
            new HttpGetTask().execute();
        }
        catch (Exception e){
            Log.e("Error:",e.toString());
        }
    }

    //May be deleted
    public boolean onCreateOptionsMenu(Menu menu) {
        //generateDummyData();
        return true;
    }

    private void generateDummyData() {
        SearchItem data = null;
        listData = new SearchItem[5];

        for (int i = 0; i < 5; i++) {
            data = new SearchItem();
            data.dateSearchItem = "" + (2000 + i);
            data.nameSearchItem = "Post " + (i + 1);
            data.seasonsSearchItem = "" + i;
            listData[i] = data;
        }

    }


    private class HttpGetTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(Void... params) {

            try {
                urlConnection = (HttpURLConnection)myUrl.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            }catch(Exception e){
                Log.e("DoInBackgroud", e.toString());
            }finally{
                urlConnection.disconnect();
            }
            return null;
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                Log.e("Error in readStream", "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            //Retrieve the SearchItem[] from the XMLParser and the result
            Log.v("Result_PE:",result);
            SearchRespParser myXMLparser= new SearchRespParser();
            InputStream in= new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));

            try {
                listData = (SearchItem[])myXMLparser.parse(in);
            }catch (Exception e){
                Log.e("onPostExceuteXMLParser", e.toString());
                if(listData==null){
                    Log.e("listdata", "null");
                    generateDummyData();
                }
            }
            //Inflate and set the elements of the views: ARRAYADAPTER
            if(listData==null) Log.e("listdata", "null");
            SearchAdapter itemAdapter = new SearchAdapter(ctx, R.layout.searchitem, listData);
            AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view,
                                        int position, long id) {
                    SearchItem searchedIt = (SearchItem) adapter.getItemAtPosition(position);
                    Bundle data = new Bundle();
                    data.putParcelable("selectedShow", searchedIt);
                    Intent in = new Intent(SearchScreen.this, SeasonsScreen.class);
                    in.putExtras(data);
                    startActivity(in);
                }
            };
            listView.setAdapter(itemAdapter);
            listView.setOnItemClickListener(clickListener);
            progress.setVisibility(View.GONE);
        }
    }

}