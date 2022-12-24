package com.example.seanlee_thefootballgallery_2201.e_models.objects;

import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class News implements Serializable {

    private String mAuthor;
    private String mDate;
    private String mTitle;
    private String mDescription;
    private ArrayList<String> mLeague;
    private String mUrl; // url for web text extraction

    public News(String _author, String _date, String _title, String _description) {
        this.mAuthor = _author;
        this.mDate = _date;
        this.mTitle = _title;
        this.mDescription = _description;
    }

    public String getAuthor(){ return mAuthor; }
    public String getDate(){ return mDate; }
    public String getFullTitle(){ return mTitle; }
    public String getFullDesc(){ return mDescription; }
    public String getTitleSummary(){ return ""; }
    public String getDescSummary(){

        if(mDescription.length() <= 50){
            return mDescription;
        }else {
            return StringUtil.summarize(mDescription, 55);
        }
    }
}
