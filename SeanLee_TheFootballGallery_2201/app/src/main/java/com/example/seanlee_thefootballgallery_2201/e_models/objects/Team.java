package com.example.seanlee_thefootballgallery_2201.e_models.objects;

import java.io.Serializable;

public class Team implements Serializable {

    private int mRank;
    private String mLogoUrl;
    private String mTeamName;
    private int mMP;
    private int mPts;
    private int mW;
    private int mD;
    private int mL;
    private int mGD;
    private String mLastFive;

    public Team(int mRank, String mLogoUrl, String mTeamName, int mMP, int mPts, int mW, int mD, int mL, int mGD, String mLastFive) {
        this.mRank = mRank;
        this.mLogoUrl = mLogoUrl;
        this.mTeamName = mTeamName;
        this.mMP = mMP;
        this.mPts = mPts;
        this.mW = mW;
        this.mD = mD;
        this.mL = mL;
        this.mGD = mGD;
        this.mLastFive = mLastFive;
    }

    public int getRank(){ return mRank; }
    public String getLogoUrl(){ return mLogoUrl; }
    public String getTeamName(){ return mTeamName; }
    public int getMP(){ return mMP; }
    public int getPts(){ return mPts; }
    public int getW(){ return mW; }
    public int getD(){ return mD; }
    public int getL(){ return mL; }
    public int getGD(){ return mGD; }

    //public String getLastFive(){ return mLastFive; }
}
