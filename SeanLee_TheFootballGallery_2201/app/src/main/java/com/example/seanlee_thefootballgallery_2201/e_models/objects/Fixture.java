package com.example.seanlee_thefootballgallery_2201.e_models.objects;

import java.io.Serializable;

public class Fixture implements Serializable {

    public static final String STATUS_NS = "STATUS_NS";

    private String mRound;
    private String mDate;
    private String mVenue;
    private String mStatus; // if NS, display vs.
    private String mHomeTeam;
    private String mAwayTeam;
    private String mHomeLogo;
    private String mAwayLogo;
    private int mHomeGoal;
    private int mAwayGoal;

    public Fixture(String mRound, String mDate, String mVenue, String mStatus, String mHomeTeam, String mAwayTeam, String mHomeLogo, String mAwayLogo, int mHomeGoal, int mAwayGoal) {
        this.mRound = mRound;
        this.mDate = mDate;
        this.mVenue = mVenue;
        this.mStatus = mStatus;
        this.mHomeTeam = mHomeTeam;
        this.mAwayTeam = mAwayTeam;
        this.mHomeLogo = mHomeLogo;
        this.mAwayLogo = mAwayLogo;
        this.mHomeGoal = mHomeGoal;
        this.mAwayGoal = mAwayGoal;
    }

    public int getRoundInt(){ return Integer.valueOf(mRound.split(" ")[3]); }
    public String getRound(){ return mRound; }
    public String getDate(){ return mDate; }
    public String getVenue(){ return mVenue; }
    public String getStatus(){ return mStatus; }
    public String getHomeTeam(){ return mHomeTeam; }
    public String getAwayTeam(){ return mAwayTeam; }
    public String getHomeLogo(){ return mHomeLogo; }
    public String getAwayLogo(){ return mAwayLogo; }
    public String getScore(){
        if(mHomeGoal == -1 || mAwayGoal == -1) return " vs. ";
        else return mHomeGoal + " : " + mAwayGoal;
    }
    public int getHomeGoal(){ return mHomeGoal; }
    public int getAwayGoal(){ return mAwayGoal; }
}
