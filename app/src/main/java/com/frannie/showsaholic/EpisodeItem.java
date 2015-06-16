package com.frannie.showsaholic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Francesca on 15/06/2015.
 */
public class EpisodeItem implements Parcelable{
    public String season;
    public String epnum;
    public String seasonnum;
    public String airdate;
    public String link;
    public String title;
    public String screencap;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(season);
        out.writeString(epnum);
        out.writeString(seasonnum);
        out.writeString(airdate);
        out.writeString(link);
        out.writeString(title);
        out.writeString(screencap);
    }

    private static EpisodeItem readFromParcel(Parcel in) {
        EpisodeItem newItem= new EpisodeItem();
        newItem.season = in.readString();
        newItem.epnum = in.readString();
        newItem.seasonnum = in.readString();
        newItem.airdate = in.readString();
        newItem.link = in.readString();
        newItem.title= in.readString();
        newItem.screencap= in.readString();
        return newItem;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public EpisodeItem createFromParcel(Parcel in){
            return readFromParcel(in);
        }

        public EpisodeItem[] newArray(int size){
            return new EpisodeItem[size];
        }
    };
}
