package com.example.seanlee_thefootballgallery_2201.e_models.objects;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

    private String mId;
    private String mDesc;
    private User mAuthor;
    private String mMentionUsername;
    private int mLikes;
    private int mYCards;
    private String mParentId;
    private String mMentionId;
    private String mPostId;
    private boolean mIsLiked;
    private boolean mIsYCarded;

    public Comment(String _postId, User _author, String _mentionUsername, String _desc, String _parentId, String _mentionId){

        this.mId = StringUtil.generateId(StringUtil.CASE_COMMENT);
        this.mPostId = _postId;
        this.mAuthor = _author;
        this.mMentionUsername = _mentionUsername;
        this.mMentionId = _mentionId;
        this.mDesc = _desc;
        this.mLikes = 0;
        this.mYCards = 0;
        this.mParentId = _parentId;
    }

    public void updateLikeValue(int _calculation){
        mLikes += _calculation;
    }

    public void updateYCardValue(){
        mYCards++;
    }

    public void setIsLiked(boolean _isClicked){
        mIsLiked = _isClicked;
    }

    public void setIsYCarded(boolean _isClicked){
        mIsYCarded = _isClicked;
    }

    public boolean[] getAreClicked(){ return new boolean[]{mIsLiked, mIsYCarded}; }
    public void setId(String _id){
        this.mId = _id;
    }

    public String getId(){ return mId; }
    public String getAuthorId(){ return mAuthor.getId(); }
    public int getLayer(){
        if(mParentId.isEmpty() || mParentId.length() <= 0){
            return 0;
        }else{
            return 1;
        }
    }

    public String getMentionUsername(){ return mMentionUsername; }

    public String getPostId(){ return mPostId; }
    public User getAuthorUser(){ return mAuthor; }

    public String getMentionId(){ return mMentionId; }

    public Spanned getMentionIdFormatted(){
        String strToFormat = "<i>@" + mMentionUsername + " </i> ";
        return Html.fromHtml(strToFormat);
    }
    public Date getDateDate(){
        return StringUtil.extractDateFromDocumentId(mId);
    }
    public String getTimeGap(){

        return StringUtil.calculateTimeDiff(StringUtil.extractDateFromDocumentId(mId));
    }
    public int getAuthorEmblem(Context _context){ return mAuthor.getEmblemForImageView(_context); }
    public String getParentId(){ return mParentId; }
    public String getTimestamp(){ return mId; }

    public String getDesc(){
        if(mMentionId.length() > 0) return getMentionIdFormatted() + mDesc;
        else return mDesc;
    }
    public String getRawDesc(){
        return mDesc;
    }
    public int getLike(){ return mLikes; }
    public int getYCards(){ return mYCards; }

}
