package com.frannie.showsaholic;

import com.frannie.showsaholic.Group;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

/**
 * Created by Francesca on 16/06/2015.
 */
public class SeasonParser {

        // We don't use namespaces
        private final String ns = null;


        //TAGS TO LOOK FOR
        String SEASONNUMTAG="seasonnum";
        String EPNUMTAG="epnum";
        String AIRDATETAG= "airdate";
        String TITLETAG= "title";
        String LINKTAG= "link";
        String SCREENCAPTAG= "screencap";
        String EPISODETAG= "episode";

        public SparseArray<Group> parse(InputStream inputStream) throws XmlPullParserException, IOException {
            try {
                Log.v("SEASONPARSER:", "calling");
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);
                parser.nextTag();

                return readFeed(parser);
            } finally {
                inputStream.close();
            }
        }

        private SparseArray<Group> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

            int numSeason=0;
            SparseArray<Group> groups= new SparseArray<Group>();
            parser.require(XmlPullParser.START_TAG, null, "Show");
            String epnum=null;
            String title = null;
            String link = null;
            String seasonnum = null;
            String airdate= null;
            String screencap=null;
            String imgUrl=null;
            boolean hasScreencapTag=true;
            EpisodeItem item;

            //List<SearchItem> items = new ArrayList<SearchItem>();

            List<EpisodeItem> currentList=new ArrayList<EpisodeItem>();
            Group current_group=null;
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                //Retrieve the TVSeries image from the url in the xml
                if(name.equals("image")){
                    imgUrl=readLinkImage(parser);
                } else if(name.equals("Season")){
                    //All the Other Seasons except the first: I have to append the previous group
                    if(currentList!=null && current_group!=null) {
                        //current_group.children=currentList;
                        groups.append(numSeason-1,current_group);
                        }
                    ++numSeason;
                    String namegroup = "Season: " + numSeason;
                    Group gr = new Group(namegroup);
                    gr.children = new ArrayList<EpisodeItem>();
                    currentList = gr.children;
                    current_group= gr;
                }else if(name.equals(EPNUMTAG)){
                    epnum=readTag(parser, EPNUMTAG);
                }else if(name.equals(SEASONNUMTAG)){
                    seasonnum= readTag(parser, SEASONNUMTAG);
                }else if(name.equals(AIRDATETAG)){
                    airdate= readTag(parser,AIRDATETAG);
                }else if (name.equals(LINKTAG)) {
                    link = readLink(parser);
                }else if (name.equals(TITLETAG)) {
                    title = readTag(parser,TITLETAG);
                }else if (name.equals(SCREENCAPTAG)) {
                    screencap = readTag(parser,SCREENCAPTAG);
                }else if(name.equals(EPISODETAG)){
                    if(current_group.children.isEmpty()&& epnum!=null && seasonnum != null && airdate!=null && link != null && title!=null)
                        hasScreencapTag=false;
                }

                if(hasScreencapTag==true){
                    if (epnum!=null && seasonnum != null && airdate!=null && link != null && title!=null && screencap!=null) {
                         item= new EpisodeItem();
                        item.season = "" + numSeason;
                        item.epnum = epnum;
                        item.seasonnum = seasonnum;
                        item.airdate = airdate;
                        item.link = link;
                        item.title = title;
                        item.screencap = screencap;
                        Log.v("EpisodeItem", item.toString());
                        Log.v("Season", ""+numSeason);
                        if(currentList==null)
                            Log.v("currentList: ","null");
                        if(current_group==null)
                            Log.v("current_group: ", "null");
                        currentList.add(item);

                        epnum=seasonnum=airdate=link=title=screencap=null;
                    }
                }
                else if(hasScreencapTag==false){
                    if(epnum!=null && seasonnum != null && airdate!=null && link != null && title!=null){
                        item= new EpisodeItem();
                        item.season = "" + numSeason;
                        item.epnum = epnum;
                        item.seasonnum = seasonnum;
                        item.airdate = airdate;
                        item.link = link;
                        item.title = title;
                        item.screencap= imgUrl;

                        Log.v("EpisodeItem", item.toString());
                        Log.v("Season", ""+numSeason);
                        if(currentList==null)
                            Log.v("currentList: ","null");
                        if(current_group==null)
                            Log.v("current_group: ", "null");
                        currentList.add(item);

                        epnum=seasonnum=airdate=link=title=screencap=null;
                    }
                }

            }
            groups.append(numSeason-1,current_group);
            Log.v("Season retrieved:", ""+numSeason);
            return groups;
        }

        private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

        private String readLinkImage(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "image");
            String img = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "image");
            return img;
        }


        private String readTag(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, tag);
            String text= readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, tag);
            return text;
        }



        // For the tags title and link, extract their text values.
        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }

}


