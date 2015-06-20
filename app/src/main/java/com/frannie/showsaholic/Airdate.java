package com.frannie.showsaholic;

import android.util.Log;

/**
 * Created by Francesca on 19/06/2015.
 */
public class Airdate {
    int day;
    int month;
    int year;

    public Airdate(String airdate){
        //The retrieved airdate is YYYY-MM-DD 2015-04-19
        String[] stringTrimmed= airdate.split("-");
        year= Integer.parseInt(stringTrimmed[0]);
        month= Integer.parseInt(stringTrimmed[1]);
        day= Integer.parseInt(stringTrimmed[2]);
    }

    /*
    * Return TRUE if this airdate is today or in the future
    * */
    public boolean toBeAired(int today_day, int today_month ,int today_year){

        if(this.year>=today_year) {
            //Log.v("YEAR", this.year+">="+today_year);
            if (this.month >= today_month) {
                //Log.v("MONTH", this.month+">="+today_month);
                if (this.day >= today_day){
                    //Log.v("DAY", this.day+">="+today_day);
                    return true;
                }
            }
        }

            return false;
        }

    @Override
    public String toString(){
        return day+"-"+month+"-"+year;
    }

}
