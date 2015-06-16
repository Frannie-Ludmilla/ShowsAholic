package com.frannie.showsaholic;

import android.os.Parcel;
import android.os.Parcelable;
import java.net.URL;

/**
 * Created by Francesca on 14/06/2015.
 */
public class SearchItem implements Parcelable{
    public String nameSearchItem;
    public String dateSearchItem;
    public String seasonsSearchItem;
    public String showID;
    public String seriesURL;


    public SearchItem(){}

    public SearchItem(String name, String date, String season, String show, String url){
        nameSearchItem=name;
        dateSearchItem=date;
        seasonsSearchItem=season;
        showID=show;
        seriesURL= url;
    }

    public void copyFields(Object o){
        if(o.getClass().equals(SearchItem.class)){
            SearchItem toModify=(SearchItem)o;
            toModify.nameSearchItem= this.nameSearchItem;
            toModify.dateSearchItem= this.dateSearchItem;
            toModify.seasonsSearchItem= this.seasonsSearchItem;
            toModify.showID=this.showID;
            toModify.seriesURL=this.seriesURL;
        }
    }

    @Override
    public String toString(){
        return "Name :"+nameSearchItem+
                " date : "+ dateSearchItem+
                " seasons: "+seasonsSearchItem+
                " ShowId : "+ showID+
                " link: "+ seriesURL +"\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(nameSearchItem);
        out.writeString(dateSearchItem);
        out.writeString(seasonsSearchItem);
        out.writeString(showID);
        out.writeString(seriesURL);
    }

    private static SearchItem readFromParcel(Parcel in) { // (4)
        String name = in.readString();
        String date = in.readString();
        String season = in.readString();
        String show = in.readString();
        String url = in.readString();
        return new SearchItem(name,date,season,show,url);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public SearchItem createFromParcel(Parcel in){
            return readFromParcel(in);
        }

        public SearchItem[] newArray(int size){
            return new SearchItem[size];
        }
    };
}
