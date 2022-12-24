package com.example.seanlee_thefootballgallery_2201.e_models.objects;

import android.content.Context;

import com.example.seanlee_thefootballgallery_2201.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class User implements Serializable {

    private String mId;
    private String mUName;
    private ArrayList<Post> mPosts;

    // TODO:
    private ArrayList<String> mPostLikes;
    private ArrayList<String> mPostYCards;
    private ArrayList<String> mCommentLikes;
    private ArrayList<String> mCommentYCards;


    private String mPhoto;
    private String mEmblem;
    private int mEmblem_league;
    private int mEmblem_team;

    public User(String _id, String _uname, ArrayList<Post> _posts,
                ArrayList<String> _postLikes, ArrayList<String> _postYCards, ArrayList<String> _commentLikes, ArrayList<String> _commentYCards,
                String _photo, int _emblemLeague, int _emblemTeam) {

        this.mId = _id;
        this.mUName = _uname;
        this.mPosts = _posts;
        this.mPhoto = _photo;

        // TODO
        this.mPostLikes = _postLikes;
        this.mPostYCards = _postYCards;
        this.mCommentLikes = _commentLikes;
        this.mCommentYCards = _commentYCards;

        this.mEmblem_league = _emblemLeague;
        this.mEmblem_team = _emblemTeam;
    }

    public User(String _id, String _uname, int _emblemLeagueInt, int _emblemTeamInt){
        this.mId = _id;
        this.mUName = _uname;
        this.mEmblem_league = _emblemLeagueInt;
        this.mEmblem_team = _emblemTeamInt;
    }

    // TODO:
    public ArrayList<String> getPostLikes(){ return mPostLikes; }
    public ArrayList<String> getPostYCards(){ return mPostYCards; }
    public ArrayList<String> getCommentLikes(){ return mCommentLikes; }
    public ArrayList<String> getCommentYCards(){ return mCommentYCards; }

    public String getId(){ return mId; }
    public String getUName(){ return mUName; }
    public ArrayList<Post> getPosts(){ return mPosts; }
    public ArrayList<String> getPostIds(){

        ArrayList<String> postIds = new ArrayList<>();

        for (Post post: mPosts) {
            postIds.add(post.getId());
        }

        return postIds;
    }
    public String getPhotoString(){ return mPhoto; }

    public int getEmblemLeagueInt(){ return mEmblem_league; }
    public int getEmblemTeamInt(){ return mEmblem_team; }

    public int getEmblemForImageView(Context _context){ return emblemUtil(_context, mEmblem_league, mEmblem_team); }

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
        return _context.getResources().getIdentifier(arr.get(_team), "drawable", _context.getPackageName());
    }
}
