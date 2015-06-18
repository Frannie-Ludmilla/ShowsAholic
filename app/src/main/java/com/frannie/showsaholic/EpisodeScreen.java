package com.frannie.showsaholic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by Francesca on 15/06/2015.
 */
public class EpisodeScreen extends Activity {

    protected URL episodeURL;
    protected String episodeSUrl;
    protected ProgressBar progress;
    protected TextView episodeTitle_tv;
    protected TextView title_tv;
    protected TextView episodeNum_tv;
    protected TextView airdate_tv;
    protected TextView synopsis_tv;
    protected TextView info_tv;
    protected EpisodeItem selectedEpisode;
    protected NetworkImageView imgFromUrl;
    protected RequestQueue mRequestQueue;
    protected ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_screen);

        //Retrieve the data related to the previously selected show

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                selectedEpisode= null;
            } else {
                selectedEpisode= extras.getParcelable("current_ep");
            }
        } else {
            selectedEpisode= (EpisodeItem)savedInstanceState.getParcelable("current_ep");
        }

        progress=(ProgressBar)this.findViewById(R.id.progressbar_loading_episode);
        episodeTitle_tv= (TextView)this.findViewById(R.id.EpisodeTVInfo);
        title_tv= (TextView)this.findViewById(R.id.TitleEpisodeTV);
        episodeNum_tv=(TextView)this.findViewById(R.id.NumEpisodeTV);
        airdate_tv=(TextView)this.findViewById(R.id.AirDateEpisodeTV);
        synopsis_tv=(TextView)this.findViewById(R.id.SynopsisEpisodeTV);
        info_tv=(TextView)this.findViewById(R.id.URLepisodeTV);
        imgFromUrl = (NetworkImageView)this.findViewById(R.id.imageEpisode);
        mRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        try {
            episodeSUrl= selectedEpisode.link;
            episodeURL = new URL(selectedEpisode.link);
            new FetchWebsiteData().execute();
        }catch(Exception e){
            Log.e("EPISODESCREEN","onCreate");
        }


        Calendar rightNow = Calendar.getInstance();
        int today= rightNow.DAY_OF_MONTH;


        Button button = (Button) findViewById(R.id.button2GoogleCal);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //@TODO: Intent to Calendar
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, "Watch this episode!");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private class FetchWebsiteData extends AsyncTask<Void, Void, Void> {
        String websiteTitle, websiteDescription;
        String ep_synopsis;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to website
                Document document = Jsoup.connect(episodeSUrl).get();
                String temp = document.html().replace("<br />", "$$$"); //$$$ instead <br>
                document = Jsoup.parse(temp); //Parse again
                String text = document.body().text().replace("$$$", "\n").toString();
                Log.v("SITE source:",episodeSUrl);
                Log.v("SITE:",text);
                Element masthead = document.select("div.show_synopsis").first();
                if (masthead.hasText())
                    ep_synopsis = masthead.text();
                else
                    ep_synopsis = null;
                Log.v("END:",masthead.text());
            }catch (Exception e){
                Log.e("JSOUP", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            int numSeason=Integer.parseInt(selectedEpisode.season);
            int numEpisode= Integer.parseInt(selectedEpisode.seasonnum);
            imgFromUrl.setImageUrl(selectedEpisode.screencap, mImageLoader);
            //The episode must appear as SxxExx, for episodes' and series' number less than 10 set prefix "0"
            episodeTitle_tv.setText("Episode Info: S"+
                    (numSeason<10?"0"+numSeason:""+numSeason)+"E"+
                            (numEpisode<10?"0"+numEpisode:""+numEpisode));
            //title_tv.setText("Title: "+selectedEpisode.title);
            title_tv.setText(Html.fromHtml("<b>Title:</b> " + selectedEpisode.title));
            episodeNum_tv.setText(Html.fromHtml("<b>Episode Number:</b> "+selectedEpisode.epnum));
            airdate_tv.setText(Html.fromHtml("<b>Airdate:</b> "+selectedEpisode.airdate));
            synopsis_tv.setText(Html.fromHtml("<b>Synopsis: </b>"+
                    (ep_synopsis!=null?ep_synopsis:"Not Available") ));
            info_tv.setText(Html.fromHtml("For more info: "+"<a href=\""+selectedEpisode.link+"\">"+selectedEpisode.link+"</a>"));
            progress.setVisibility(View.GONE);
        }
    }
}