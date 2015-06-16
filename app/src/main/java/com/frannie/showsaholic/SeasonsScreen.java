package com.frannie.showsaholic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * Created by Francesca on 15/06/2015.
 */
public class SeasonsScreen extends Activity {
    // more efficient than HashMap for mapping integers to objects
    SparseArray<Group> groups = new SparseArray<Group>();
    ProgressBar progress;
    ExpandableListView listView;
    Context ctx;
    protected String URL_SEARCH= "http://services.tvrage.com/feeds/full_show_info.php?sid=";
    protected URL myUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelistseasons);

        //Retrieve the data related to the previously selected show
        SearchItem selectedShow;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                selectedShow= null;
            } else {
                selectedShow= extras.getParcelable("selectedShow");
            }
        } else {
            selectedShow= (SearchItem)savedInstanceState.getParcelable("selectedShow");
        }
        Log.v("Result_PE:", selectedShow.toString());


        try {
            myUrl=new URL(URL_SEARCH + selectedShow.showID);
            new HttpGetTask().execute();
        }
        catch (Exception e){
            Log.e("Error:",e.toString());
        }


        createData();
        progress=(ProgressBar)this.findViewById(R.id.progressbar_loading_seasons);
        ExpandableListView listView = (ExpandableListView)findViewById(R.id.listSeasonsExpandable);
        ctx=this;
        SeriesEpisodesAdapter adapter = new SeriesEpisodesAdapter(this, groups);
        listView.setAdapter(adapter);
    }

    public void createData() {
        for (int j = 0; j < 5; j++) {
            Group group = new Group("Test " + j);
            for (int i = 0; i < 5; i++) {
                group.children.add("Sub Item" + i);
            }
            groups.append(j, group);
        }
    }

    //Class for Asking the retrieval of the

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
    /*
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
        progress.setVisibility(View.GONE);*/
    }
}


}

