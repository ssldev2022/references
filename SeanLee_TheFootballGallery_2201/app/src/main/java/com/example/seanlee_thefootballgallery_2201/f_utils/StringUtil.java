package com.example.seanlee_thefootballgallery_2201.f_utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seanlee_thefootballgallery_2201.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringUtil {

    public static final String TAG = "StringUtil.TAG";

    // TODO: fails
    public static final String MSG_FIELD_EMPTY = "Please fill out the forms.";

    public static final String MSG_INCORRECT_FORMAT = "Please follow required format.";

    public static final String MSG_INCORRECT_INPUT = "Incorrect user information. Try again";
    public static final String MSG_NAME_EXISTS = "Username exists. Choose a different username.";

    public static final String MSG_NO_PHOTO = "Please take a photo.";

    public static final String MSG_PWD_MATCH = "Passwords doesn't match.";
    public static final String MSG_PWD_LENGTH = "Password must be at least 8 characters.";

    // TODO: successful
    public static final String MSG_EMAIL_VALID = "Email valid";
    public static final String MSG_PWD_VALID = "Pwd valid";
    public static final String MSG_LOGIN_SUCCESSFUL = "Login Successful";

//    public static Drawable getDrawableTag(){
//
//    }

    public static boolean isFieldValid(EditText[] _fields){

        for (EditText field: _fields) {
            String input = field.getText().toString();
            if (input.trim().length() <= 0 || input.length() <= 0 || input == null){ return false; }
        }
        return true;
    }

    public static void makeToast(Context _context, String _message){
        Toast.makeText(_context, _message,
                Toast.LENGTH_LONG).show();
    }

    public static String isEmailValid(EditText _field){
        CharSequence input = _field.getText().toString();
        if(!TextUtils.isEmpty(input)){
            if(Patterns.EMAIL_ADDRESS.matcher(input).matches()){
                return MSG_EMAIL_VALID;
            }else{
                return MSG_INCORRECT_FORMAT;
            }
        }else{
            return MSG_FIELD_EMPTY;
        }
    }

    public static boolean isUNameValid(EditText _field){
        return true;
    }

    public static boolean isPhotoValid(Drawable _givenPhoto){
        if(!_givenPhoto.equals(R.drawable.ic_baseline_add_48)){
            return true;
        }else{
            return false;
        }
    }

    public static String isPwdValid(EditText[] _pwdFields){

        if(_pwdFields.length == 1){
            String f1 = _pwdFields[0].getText().toString();

            if(f1.length() >= 8) return MSG_PWD_VALID;
            else return MSG_PWD_LENGTH;
        }else {
            String f1 = _pwdFields[0].getText().toString();
            String f2 = _pwdFields[1].getText().toString();

            if(f1.equals(f2)){
                if(f1.length() >= 8 && f2.length() >= 8) return MSG_PWD_VALID;
                else return MSG_PWD_LENGTH;
            }else{
                return MSG_PWD_MATCH;
            }
        }
    }

    public static boolean isLoginValid(EditText[] _fields){
        return true;
    }

    // TODO
    public static int getDrawableIdByString(Context _context, String _string){

        return _context.getResources().getIdentifier(_string.split("\\.")[0], "drawble", _context.getPackageName());
    }

    public static String getTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        return sdf.format(new Date());
    }

    public static String formatNewsDate(String _date){
        String plusRemoved = _date.split("\\+")[0];
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = null;
        try {
            date = originalFormat.parse(plusRemoved);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return newFormat.format(date);
    }

    public static String dateToString(Date _date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        return sdf.format(_date);
    }

    public static Date stringToDate(String _date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date date = null;
        try {
            date = sdf.parse(_date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public static String calculateTimeDiff(Date _date){

        int value = 0;
        String unit = "";

        Date curr = new Date();
        long diffLong = curr.getTime() - _date.getTime();
        double diff = (double) diffLong;

        double seconds = diff / 1000;
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;
        double weeks = days / 7;
        double months = weeks / 4;
        double years = months / 12;

        if(minutes < 1){ // less than a minute -> just now
            unit = "just now";
            value = 0;
        }else if(1 < minutes && hours < 1){ // less than an hour -> 31 min
            unit = "min";
            value = (int) Math.ceil(minutes);
        }else if(1 < hours && days < 1){ // less than a day -> 19 hrs
            unit = "h";
            value = (int) Math.ceil(hours);
        }else if(1 < days && weeks < 1){ // less than a week -> 3 days
            unit = "d";
            //value = (int) Math.ceil(hours)
            value = (int) Math.ceil(days);
        }else if(1 < weeks && months < 1){ // less than a month -> 3 weeks
            unit = "w";
            value = (int) Math.ceil(weeks);
        }else if(1 < months && years < 1){ // less than a year -> 5 months
            unit = "mo";
            value = (int) Math.ceil(months);
        }else if(1 < years){ // more than a year -> 3 years
            unit = "y";
            value = (int) Math.ceil(years);
        }

        if(value == 0) return unit;
        else return String.valueOf(value) + unit;
    }

    public static Date extractDateFromDocumentId(String _docId){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        Date date = null;

        try {
            date = sdf.parse(_docId.split("_")[1]);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public static String getFilterTagText(String _text){
        return " filter: " + _text + " ";
    }

    public static String getSortTagText(String _text){
        return " sort: " + _text + " ";
    }

    private static final String ID_POST_PREFIX = "post_";
    private static final String ID_COMMENT_PREFIX = "comment_";

    public static final int CASE_POST = 0;
    public static final int CASE_COMMENT = 1;

    public static String generateId(int _case){
        if(_case == CASE_POST) return ID_POST_PREFIX + StringUtil.getTimeStamp();
        else return ID_COMMENT_PREFIX + StringUtil.getTimeStamp();
    }

    public static String summarize(String _toSummarize, int _letterCount){

        String[] words = _toSummarize.split(" ");
        String retval = "";
        int x = 0;

        while (retval.length() < _letterCount - 3){
            if((retval+words[x]).length() > _letterCount - 3) break;
            retval += words[x] + " "; x++;
        }
        return retval + "...";
    }

    public static String unixTimeToDateString(long _unixTimestamp){

        Date date = new Date(_unixTimestamp * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        return sdf.format(date);
    }
}
