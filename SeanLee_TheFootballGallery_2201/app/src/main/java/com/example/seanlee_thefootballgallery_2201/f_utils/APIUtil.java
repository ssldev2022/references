package com.example.seanlee_thefootballgallery_2201.f_utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.seanlee_thefootballgallery_2201.e_models.objects.Fixture;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.News;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class APIUtil {

    public static final String TAG = "APIUtil.TAG";


    public static ArrayList<News> getLSNewsJSON(String _result){

        ArrayList<News> newsArr = new ArrayList<>();

        try {

            JSONObject object = new JSONObject(_result);
            JSONArray data = object.getJSONArray("data");
            for(int i=0; i<data.length(); i++){
                JSONObject child = (JSONObject) data.get(i);

                //String author = child.getString("title");
                String author = "livescore.com"; // TODO
                String title = child.getString("title");
                String date = child.getString("published_at");

                String desc = "";

                JSONArray body = child.getJSONArray("body");
                for(int j=0; j<body.length(); j++){
                    JSONObject child_body = (JSONObject) body.get(j);
                    JSONObject data_inner = child_body.getJSONObject("data");

                    if(data_inner.has("content")){

                        String content = data_inner.getString("content");

                        String tag = "<p>";
                        String tag1 = "</p>";
                        String tag2 = "&nbsp;";
                        String tag3 = "<br>";
                        String tag4 = "<strong>";
                        String tag5 = "</strong>";
                        String tag6 = "<i>";
                        String tag7 = "</i>";

                        content = content.replaceAll(tag, "");
                        content = content.replaceAll(tag1, "\n\n ");
                        content = content.replaceAll(tag2, " ");
                        content = content.replaceAll(tag3, "");
                        content = content.replaceAll(tag4, "");
                        content = content.replaceAll(tag5, "");
                        content = content.replaceAll(tag6, "");
                        content = content.replaceAll(tag7, "\n\n ");

                        desc += content;
                    }
                }
                newsArr.add(new News(author, date, title, desc));
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return newsArr;
    }

    public static ArrayList<Fixture> getMatchesJSON(String _result){

        ArrayList<Fixture> retVal = new ArrayList<>();

        String round, date, venue, status, hometeam, awayteam, homelogo, awaylogo;
        int timestamp, homegoal, awaygoal;

        try {

            JSONObject jObj = new JSONObject(_result);

            JSONArray responses = jObj.getJSONArray("response");
            for(int i=0; i<responses.length(); i++){
                JSONObject item = (JSONObject) responses.get(i);

                JSONObject fixture = item.getJSONObject("fixture");
                //date = fixture.getString("date");
                //timestamp = fixture.getInt("timestamp");
                date = StringUtil.unixTimeToDateString(fixture.getLong("timestamp"));
                venue = fixture.getJSONObject("venue").getString("name");
                status = fixture.getJSONObject("status").getString("short");

                JSONObject league = item.getJSONObject("league");
                round = league.getString("round");

                JSONObject teams = item.getJSONObject("teams");
                hometeam = teams.getJSONObject("home").getString("name");
                homelogo = teams.getJSONObject("home").getString("logo");
                awayteam = teams.getJSONObject("away").getString("name");
                awaylogo = teams.getJSONObject("away").getString("logo");

                JSONObject goals = item.getJSONObject("goals");



                if(goals.get("home").equals(JSONObject.NULL)){
                    homegoal = -1;
                }else {
                    homegoal = goals.getInt("home");
                }

                if(goals.get("away").equals(JSONObject.NULL)){
                    awaygoal = -1;
                }else {
                    awaygoal = goals.getInt("away");
                }

                retVal.add(new Fixture(round, date, venue, status, hometeam, awayteam, homelogo, awaylogo, homegoal, awaygoal));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return retVal;
    }

    public static ArrayList<Team> getStandingsJSON(Context _context, String _result){

        ArrayList<Team> retVal = new ArrayList<>();

        int rank,  mp, pts, w, d, l, gd;
        String logoUrl, teamName, lastFive;

        try {
            JSONObject jObj = new JSONObject(_result);

            JSONArray response = jObj.getJSONArray("response");
            JSONObject item = response.getJSONObject(0);
            JSONObject league = item.getJSONObject("league");

            //logoUrl = league.getString("logo"); -> this is league logo

            int leagueId = league.getInt("id");

            JSONArray standings = league.getJSONArray("standings");
            JSONArray items = standings.getJSONArray(0);

            for (int i=0; i<items.length(); i++) {

                // TODO: save logo to sharedPref

                //SharedPreferences sharedPref = _context.getSharedPreferences(Context.MODE_PRIVATE);

                JSONObject team = items.getJSONObject(i); // might just do .get()

                rank = team.getInt("rank");
                JSONObject teamDetail = team.getJSONObject("team");

                teamName = teamDetail.getString("name");
                logoUrl = teamDetail.getString("logo");

                pts = team.getInt("points");
                gd = team.getInt("goalsDiff");
                lastFive = team.getString("form");

                JSONObject recordDetail = team.getJSONObject("all");

                mp = recordDetail.getInt("played");
                w = recordDetail.getInt("win");
                d = recordDetail.getInt("draw");
                l = recordDetail.getInt("lose");

                retVal.add(new Team(rank, logoUrl, teamName, mp, pts, w, d, l, gd, lastFive));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return retVal;
    }
}
