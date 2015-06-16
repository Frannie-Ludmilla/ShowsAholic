package com.frannie.showsaholic;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;


import com.frannie.showsaholic.SearchRespParser;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francesca on 14/06/2015.
 */
public class SearchScreen extends Activity {


    //protected String[] prova = new String[]{"Post 1", "Post 2", "Post 3", "Post 4", "Post 5", "Post 6"};
    private SearchItem[] listData;
    protected String URL_SEARCH= "http://services.tvrage.com/feeds/search.php?show=";
    String searchedShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // String search = savedInstanceState.getIntent();
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

        try {
            URL url=new URL(URL_SEARCH + searchedShow);

            HTTPRequest myhttp = new HTTPRequest(url);
            myhttp.execute();
        }
        catch (Exception e){
            Log.e("Error:",e.toString());
        }


       /* new HTTPHandler() {
            @Override
            public HttpUriRequest getHttpRequestMethod() {
                Log.v("Search URL",URL_SEARCH+searchedShow);
                return new HttpGet(URL_SEARCH+searchedShow);

                // return new HttpPost(url)
            }

            @Override
            public void onResponse(String result) {
                Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
                InputStream stream = new ByteArrayInputStream(result.getBytes());
                try{
                    listData = new SearchRespParser().parse(stream);
                }
                catch(XmlPullParserException e){
                    //Data cannot be parsed
                    Log.e("Search Parser: ","Stream cannot be parsed");
                    Log.e("Stream is", result);
                }
                catch(IOException e){

                }
            }

        }.execute();
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //generateDummyData();
        setContentView(R.layout.searchlist);
        ListView listView = (ListView) this.findViewById(R.id.searchListView);
        if(listData==null) Log.e("listdata", "null");
        SearchAdapter itemAdapter = new SearchAdapter(this, R.layout.searchitem, listData);
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


    /**
     * @TODO: Trigger other activity
     * Intent in = new Intent(MainActivity.this, SearchScreen.class);
     * in.putExtras(data);
     * startActivity(in);
     */
}