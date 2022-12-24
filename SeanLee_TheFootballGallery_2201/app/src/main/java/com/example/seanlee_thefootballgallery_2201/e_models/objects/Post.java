package com.example.seanlee_thefootballgallery_2201.e_models.objects;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Post implements Serializable {

    public static final int INT_LEAGUE_NONE = 1000;
    public static final int INT_LEAGUE_EPL = 0;
    public static final int INT_LEAGUE_LALIGA = 1;
    public static final int INT_LEAGUE_BUNDES = 2;
    public static final int INT_LEAGUE_SERIE = 3;
    public static final int INT_LEAGUE_LIGUE1 = 4;

    public static final int INT_CATEGORY_NEWS = 0;
    public static final int INT_CATEGORY_ANALYSIS = 1;
    public static final int INT_CATEGORY_DISCUSSION = 2;
    public static final int INT_CATEGORY_QUESTION = 3;

    public static final String STR_LEAGUE_NONE = "ALL";
    public static final String STR_LEAGUE_EPL = "EPL";
    public static final String STR_LEAGUE_LALIGA = "La Liga";
    public static final String STR_LEAGUE_BUNDES = "Bundesliga";
    public static final String STR_LEAGUE_SERIE = "Serie A";
    public static final String STR_LEAGUE_LIGUE1 = "Ligue 1";

    public static final String STR_CATEGORY_ANALYSIS = "Analysis";
    public static final String STR_CATEGORY_DISCUSSION = "Discussion";
    public static final String STR_CATEGORY_QUESTION = "Question";
    public static final String STR_CATEGORY_NEWS = "News";

    private String mId;
    private String mAuthorEmail;
    private String mAuthorUName;
    private User mAuthor;
    private int mAuthorEmblemLeagueInt;
    private int mAuthorEmblemTeamInt;
    private String mAuthorEmblemLeagueStr;
    private String mAuthorEmblemTeamStr;
    private String mTitle;
    private String mDesc;
    private int mCategory;
    private int mFilter;
    private int mLikes;
    private int mYCards;
    private ArrayList<String> mComments;

    public Post(String _title, String _desc, int _category, int _league) {

        this.mId = StringUtil.generateId(StringUtil.CASE_POST);
        this.mTitle = _title;
        this.mDesc = _desc;

        this.mCategory = _category;
        this.mFilter = _league;

        this.mLikes = 0;
        this.mYCards = 0;
        this.mComments = new ArrayList<>();
    }

    public Post(String _postId, String _author, String _title, String _desc, int _category, int _filter, int _like, int _ycard, ArrayList<String> _comments){
        this.mId = _postId;
        this.mAuthorEmail = _author;
        this.mTitle = _title;
        this.mDesc = _desc;
        this.mCategory = _category;
        this.mFilter = _filter;
        this.mLikes = _like;
        this.mYCards = _ycard;
        this.mComments = _comments;
    }

    // TODO: maybe DBUtil here?

    public void setId(String _id){ this.mId = _id; }
    public Date getDateDate(){ return StringUtil.extractDateFromDocumentId(mId); }
    public String getTimeGap(){

        return StringUtil.calculateTimeDiff(StringUtil.extractDateFromDocumentId(mId));
    }

    public String getId(){return mId;}
    public String getAuthor(){return mAuthorEmail;}
    public String getTitle(){return mTitle;}
    public String getDesc(){return mDesc;}
    public int getLikes(){return mLikes;}
    public int getYCards(){return mYCards;}
    public int getCommentSize(){
        if (mComments.size() == 0) return 0;
        else return mComments.size();
    }
    public ArrayList<String> getComments(){
        return mComments;
    }
    public Integer getPopularity(){

        int x, y;

        if(mLikes == 0) x = 0;
        else x = mLikes;

        if(mComments == null) y = 0;
        else y = mComments.size();

        return x + y * 2;
    }

    public int getCategoryInt(){ return mCategory;}
    public int getLeagueInt(){ return mFilter;}

    public Drawable[] getDrawableTags(Context _context){

        Drawable retCategory = null;
        Drawable retFilter = null;

        switch (mCategory){
            case INT_CATEGORY_ANALYSIS:
                retCategory = _context.getDrawable(R.drawable.tag_news);
                break;
            case INT_CATEGORY_DISCUSSION:
                retCategory = _context.getDrawable(R.drawable.tag_news);
                break;
            case INT_CATEGORY_QUESTION:
                retCategory = _context.getDrawable(R.drawable.tag_news);
                break;
            case INT_CATEGORY_NEWS:
                retCategory = _context.getDrawable(R.drawable.tag_news);
                break;
            default:
                retCategory = null;
                break;
        }

        switch (mFilter){
            case INT_LEAGUE_EPL:
                retFilter = _context.getDrawable(R.drawable.tag_epl);
                break;
            case INT_LEAGUE_LALIGA:
                retFilter = _context.getDrawable(R.drawable.tag_laliga);
                break;
            case INT_LEAGUE_BUNDES:
                retFilter = _context.getDrawable(R.drawable.tag_bundes);
                break;
            case INT_LEAGUE_SERIE:
                retFilter = _context.getDrawable(R.drawable.tag_serie);
                break;
            case INT_LEAGUE_LIGUE1:
                retFilter = _context.getDrawable(R.drawable.tag_ligue1);
                break;
            default:
                retFilter = null;
                break;
        }
        return new Drawable[]{retCategory, retFilter};
    }


    public String getCategoryStr(){
        String ret = "ERROR";
        switch (mCategory){
            case INT_CATEGORY_ANALYSIS:
                ret = STR_CATEGORY_ANALYSIS;
                break;
            case INT_CATEGORY_DISCUSSION:
                ret = STR_CATEGORY_DISCUSSION;
                break;
            case INT_CATEGORY_QUESTION:
                ret = STR_CATEGORY_QUESTION;
                break;
            case INT_CATEGORY_NEWS:
                ret = STR_CATEGORY_NEWS;
                break;
            default:
                break;
        }
        return " " + ret + " ";
    }
    public String getLeagueStr(){
        String ret = "ERROR";
        switch (mFilter){
            case INT_LEAGUE_EPL:
                ret = STR_LEAGUE_EPL;
                break;
            case INT_LEAGUE_LALIGA:
                ret = STR_LEAGUE_LALIGA;
                break;
            case INT_LEAGUE_BUNDES:
                ret = STR_LEAGUE_BUNDES;
                break;
            case INT_LEAGUE_SERIE:
                ret = STR_LEAGUE_SERIE;
                break;
            case INT_LEAGUE_LIGUE1:
                ret = STR_LEAGUE_LIGUE1;
                break;
            default:
                break;
        }
        return " " + ret + " ";
    }

    public String getDescSummary(){

        if(mDesc.length() <= 50){
            return mDesc;
        }else {
            return StringUtil.summarize(mDesc, 60);
        }
    }

    public void setAuthor(String _uid){ this.mAuthorEmail = _uid; }
    public void setLikes(int _like){ this.mLikes = _like; }
    public void setYCard(int _ycard){ this.mYCards = _ycard; }
    public void setComments(ArrayList<String> _comments){ this.mComments = _comments; }
    public void appendComments(ArrayList<String> _comments){ this.mComments.addAll(_comments); }
    public void setLeague(int _league){
        this.mFilter = _league;
    }
    public void setCategory(int _category){
        this.mCategory = _category;
    }

    public void setAuthorEmblemLeagueInt(int _emblemLeagueInt){
        this.mAuthorEmblemLeagueInt = _emblemLeagueInt;
    }
    public void setAuthorEmblemTeamInt(int _emblemTeamInt){
        this.mAuthorEmblemTeamInt = _emblemTeamInt;
    }

    public String getAuthorEmblemLeagueStr(){
        return this.mAuthorEmblemLeagueStr;
    }
    public String getAuthorEmblemTeamStr(){
        return this.mAuthorEmblemTeamStr;
    }

    public int getAuthorEmblemLeagueInt(){
        return this.mAuthorEmblemLeagueInt;
    }
    public int getAuthorEmblemTeamInt(){
        return this.mAuthorEmblemTeamInt;
    }

    public void setAuthorUname(String _uname){
        this.mAuthorUName = _uname;
    }

    public String getAuthorUName(){
        return this.mAuthorUName;
    }

    public void updateTitle(String _title){
        this.mTitle = _title;
    }

    public void updateDesc(String _desc){
        this.mDesc = _desc;
    }

    public void updateLikes(){
        this.mLikes++;
    }

    public void updateYCards(){
        this.mYCards++;
    }

    public void comment(String _commentId, String _newComment){

    }

    public void updateComments(String _newComment){

    }

    public int getEmblemForImageView(Context _context){ return emblemUtil(_context, mAuthorEmblemLeagueInt, mAuthorEmblemTeamInt); }

    private int emblemUtil(Context _context, int _league, int _team){

        int arrId = -1;

        switch (_league){
            case 0:
                arrId = R.array.arr_epl_logo_str;
                break;
            case 1:
                arrId = R.array.arr_laliga_logo_str;
                break;
            case 2:
                arrId = R.array.arr_bundes_logo_str;
                break;
            case 3:
                arrId = R.array.arr_serie_logo_str;
                break;
            case 4:
                arrId = R.array.arr_ligue1_logo_str;
                break;
            default:
                break;
        }
        ArrayList<String> arr = new ArrayList<>(Arrays.asList(_context.getResources().getStringArray(arrId)));
        Log.i("TAG", arr.get(_team) + " / pkgName: " + _context.getPackageName());
        return _context.getResources().getIdentifier(arr.get(_team), "drawable", _context.getPackageName());
    }


    private String generateDate(){
        return StringUtil.dateToString(new Date());
    }
}
