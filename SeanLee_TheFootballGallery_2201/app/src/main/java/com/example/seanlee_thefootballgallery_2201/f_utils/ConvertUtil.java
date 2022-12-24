package com.example.seanlee_thefootballgallery_2201.f_utils;

import android.net.Uri;

import java.util.Date;

public class ConvertUtil {

    private static final String TAG = "ConvertUtil.TAG";

    //
    // TODO: Date Utils
    //

    public static String GetTimePassedFromDate(Date _date){

        // TODO: calculate time passed from the date

        String unit;
        int count = 0;

        Date curr = new Date();

        // TODO: if over a year
        if("" == ""){
            unit = "y";
        }
        // TODO: if over a month
        else if("" == ""){
            unit = "M";
        }
        // TODO: if over a week
        else if("" == ""){
            unit = "w";
        }
        // TODO: if over a day
        else if("" == ""){
            unit = "d";
        }
        // TODO: if less than a day
        else if("" == ""){
            unit = "h";
        }
        // TODO: less than an hour
        else if("" == ""){
            unit = "m";
        }
        return count + unit;
    }

    public static String GetStringFromDate(Date _date){
        return "";
    }

    public static Date GetDateFromString(String _string){
        return null;
    }

    //
    // TODO: Photo Utils
    //

    public static Uri GetUriFromString(String _string){
        return null;
    }

    public static String GetStringFromUri(Uri _uri){
        return null;
    }

}
