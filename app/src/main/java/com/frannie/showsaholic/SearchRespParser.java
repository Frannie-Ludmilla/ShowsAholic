package com.frannie.showsaholic;

/**
 * Created by Francesca on 14/06/2015.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;
public class SearchRespParser {
    // We don't use namespaces
    private final String ns = null;

    public SearchItem[] parse(InputStream inputStream) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);
                parser.nextTag();
                return readFeed(parser);
            } finally {
                inputStream.close();
            }
        }

        private SearchItem[] readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, null, "Results");
            String title = null;
            String link = null;
            String showid = null;
            String season = null;
            String date= null;

            List<SearchItem> items = new ArrayList<SearchItem>();
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if(name.equals("showid")){
                    showid= readShowID(parser);
                } else if (name.equals("name")) {
                    title = readTitle(parser);
                } else if (name.equals("link")) {
                    link = readLink(parser);
                }else if(name.equals("started")){
                    date= readYear(parser);
                }else if(name.equals("seasons")){
                    season= readSeason(parser);
                }

                if (showid!=null && title != null && link != null && date!=null  && season!=null) {
                    SearchItem item= new SearchItem();
                    item.nameSearchItem=title;
                    item.dateSearchItem=date;
                    item.seriesURL=link;
                    item.showID=showid;
                    item.seasonsSearchItem=season;
                    items.add(item);
                }
            }
            return (SearchItem[])items.toArray();
        }

        private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "link");
            String link = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "link");
            return link;
        }

        private String readSeason(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "seasons");
            String seasons= readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "seasons");
            return seasons;
        }

        private String readYear(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "started");
            String year= readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "started");
            return year;
        }

        private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "name");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "name");
            return title;
        }

        private String readShowID(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "showid");
            String showId = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "showid");
            return showId;
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
