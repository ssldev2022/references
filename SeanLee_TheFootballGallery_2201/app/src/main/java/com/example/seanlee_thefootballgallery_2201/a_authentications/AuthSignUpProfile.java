package com.example.seanlee_thefootballgallery_2201.a_authentications;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.b_core.CoreActivity;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.SpinnerAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AuthSignUpProfile extends AppCompatActivity {

    private static final String TAG = "AuthSignUpProfile.TAG";

    private Context mContext;

    private User mUser;

    private ImageButton mBtn_photo;
    private EditText mEt_uname, mEt_pwd, mEt_pwd_conf;
    private TextView mTv_err_uname, mTv_err_pwd;
    private Button mBtn_done;
    private Spinner mSp_emblem_league, mSp_emblem_team;

    private String mEmail;
    private String mUri;
    private Uri mCameraImgUri;

    private ArrayList<String> mTitles_EPL, mTitles_LaLiga, mTitles_Bundes, mTitles_SerieA, mTitles_Ligue1;
    private ArrayList<Integer> mLogoIds_EPL, mLogoIds_LaLiga, mLogoIds_Bundes, mLogoIds_SerieA, mLogoIds_Ligue1;

    //
    // TODO: Life-cycle
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_profile);

        mContext = this;

        mEmail = getIntent().getStringExtra(Intent.EXTRA_TEXT); // email received from AuthSignUpEmail Activity

        setUpArrays(); // populate arrays for spinners

        mBtn_photo = findViewById(R.id.auth_signup_profile_btn_photo);
        mEt_uname = findViewById(R.id.auth_signup_profile_et_uname);
        mEt_pwd = findViewById(R.id.auth_signup_profile_et_pwd);
        mEt_pwd_conf = findViewById(R.id.auth_signup_profile_et_conf_pwd);
        mTv_err_uname = findViewById(R.id.auth_signup_profile_err_uname);
        mTv_err_pwd = findViewById(R.id.auth_signup_profile_err_pwd);
        mBtn_done = findViewById(R.id.auth_signup_profile_btn_done);
        mSp_emblem_league = findViewById(R.id.auth_signup_profile_spinner_emblem_league);
        mSp_emblem_team = findViewById(R.id.auth_signup_profile_spinner_emblem_team);

        mTv_err_uname.setVisibility(View.GONE);
        mTv_err_pwd.setVisibility(View.GONE);

        setUpSpinner(0, 12); // TODO: initial setup - EPL / Man Utd
        mBtn_photo.setOnClickListener(onPhotoTap);
        mEt_uname.setOnFocusChangeListener(onUNameTap);
        mEt_pwd.setOnFocusChangeListener(onPwdTap);
        mEt_pwd_conf.setOnFocusChangeListener(onPwdConfTap);
        mBtn_done.setOnClickListener(onDoneTap);

        mSp_emblem_league.setOnItemSelectedListener(onLeagueChange);
    }

    Receiver mVerifyUnameReceiver = new Receiver();
    Receiver mCreateAccReceiver = new Receiver();
    Receiver mCreateUserReceiver = new Receiver();

    @Override
    protected void onResume() { // receiver registrations
        super.onResume();

        IntentFilter filter0 = new IntentFilter();
        filter0.addAction(USER_VERIFY_USERNAME);
        registerReceiver(mVerifyUnameReceiver, filter0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(AUTH_CREATE_ACCOUNT);
        registerReceiver(mCreateAccReceiver, filter);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(AUTH_CREATE_USER_IN_USERS);
        registerReceiver(mCreateUserReceiver, filter2);
    }

    @Override
    protected void onPause() { // receiver unregistrations
        super.onPause();
        unregisterReceiver(mCreateAccReceiver);
        unregisterReceiver(mCreateUserReceiver);
    }

    AdapterView.OnItemSelectedListener onLeagueChange = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "onItemSelected: i: " + i);
            updateSpinner(i); // update team spinner on first spinner value change
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }
    };

    //
    // TODO: Adapters
    //

    private void setUpArrays(){

        mTitles_EPL = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.teams_epl)));
        mLogoIds_EPL = new ArrayList<>();
        for (String league: getResources().getStringArray(R.array.arr_epl_logo_str)) {
            mLogoIds_EPL.add(StringUtil.getDrawableIdByString(mContext, league));
        }

        mTitles_LaLiga = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.teams_laliga)));
        mLogoIds_LaLiga = new ArrayList<>();
        for (String league: getResources().getStringArray(R.array.arr_laliga_logo_str)) {
            mLogoIds_LaLiga.add(StringUtil.getDrawableIdByString(mContext, league));
        }

        mTitles_Bundes = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.teams_bundes)));
        mLogoIds_Bundes = new ArrayList<>();
        for (String league: getResources().getStringArray(R.array.arr_bundes_logo_str)) {
            mLogoIds_Bundes.add(StringUtil.getDrawableIdByString(mContext, league));
        }

        mTitles_SerieA = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.teams_seriea)));
        mLogoIds_SerieA = new ArrayList<>();
        for (String league: getResources().getStringArray(R.array.arr_serie_logo_str)) {
            mLogoIds_SerieA.add(StringUtil.getDrawableIdByString(mContext, league));
        }

        mTitles_Ligue1 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.teams_ligue1)));
        mLogoIds_Ligue1 = new ArrayList<>();
        for (String league: getResources().getStringArray(R.array.arr_ligue1_logo_str)) {
            mLogoIds_Ligue1.add(StringUtil.getDrawableIdByString(mContext, league));
        }
    }

    private void setUpSpinner(int _leagueIndex, int _teamIndex){

        ArrayList<Integer> leagueLogoIds = new ArrayList<>();
        for (String league: getResources().getStringArray(R.array.leagues_logo_str)) {
            leagueLogoIds.add(StringUtil.getDrawableIdByString(mContext, league));
        }

        SpinnerAdapter leagueAdapter
//                = new SpinnerAdapter(mContext,
//                (ArrayList<String>) Arrays.asList(getResources().getStringArray(R.array.leagues_full)),
//                leagueLogoIds);
                = new SpinnerAdapter(mContext,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.leagues_full))),
                leagueLogoIds);
        mSp_emblem_league.setAdapter(leagueAdapter);
        mSp_emblem_league.setSelection(_leagueIndex); // initial league - EPL

        SpinnerAdapter teamAdapter = new SpinnerAdapter(mContext,
                mTitles_EPL, mLogoIds_EPL);
        mSp_emblem_team.setAdapter(teamAdapter);
        mSp_emblem_team.setSelection(_teamIndex); // initial team - Manchester United
    }

    private void updateSpinner(int _leagueIndex){

        ArrayList<String> itemTitles = new ArrayList<>();
        ArrayList<Integer> logoIds_int = new ArrayList<>();

        switch (_leagueIndex){
            case 0: // league pl
                itemTitles = mTitles_EPL;
                logoIds_int = mLogoIds_EPL;
                break;
            case 1: // league laliga
                itemTitles = mTitles_LaLiga;
                logoIds_int = mLogoIds_LaLiga;
                break;
            case 2: // league bundes
                itemTitles = mTitles_Bundes;
                logoIds_int = mLogoIds_Bundes;
                break;
            case 3: // league serie
                itemTitles = mTitles_SerieA;
                logoIds_int = mLogoIds_SerieA;
                break;
            case 4: // league ligue1
                itemTitles = mTitles_Ligue1;
                logoIds_int = mLogoIds_Ligue1;
                break;
            default:
                itemTitles = null;
                logoIds_int = null;
                Log.i(TAG, "updateSpinner: default reached");
        }
        SpinnerAdapter teamAdapter = new SpinnerAdapter(mContext, itemTitles, logoIds_int);
        mSp_emblem_team.setAdapter(teamAdapter);
        mSp_emblem_team.setSelection(0);
    }

    //
    // TODO: Buttons
    //

    View.OnFocusChangeListener onUNameTap = new View.OnFocusChangeListener() {
        @Override public void onFocusChange(View view, boolean b) { toggleErrFeedback(false, TOGGLE_CASE_UNAME); }
    };

    View.OnFocusChangeListener onPwdTap = new View.OnFocusChangeListener() {
        @Override public void onFocusChange(View view, boolean b) { toggleErrFeedback(false, TOGGLE_CASE_PWD); }
    };

    View.OnFocusChangeListener onPwdConfTap = new View.OnFocusChangeListener() {
        @Override public void onFocusChange(View view, boolean b) { toggleErrFeedback(false, TOGGLE_CASE_PWD); }
    };

    View.OnClickListener onPhotoTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) { // options - camera or album

            final CharSequence[] options = {"Take Photo", "Choose from album"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Add a photo");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(options[i].equals("Take Photo")) getImageFromCamera();
                    else getImageFromAlbum();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    };

    View.OnClickListener onDoneTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(StringUtil.isFieldValid(new EditText[]{mEt_uname, mEt_pwd, mEt_pwd_conf})){ // field validations

                String pwdValidation = StringUtil.isPwdValid(new EditText[]{mEt_pwd, mEt_pwd_conf});
                if(pwdValidation == StringUtil.MSG_PWD_VALID){ // pwd validation

                    if(mUri != null){
                        // TODO: username validation
                        DBUtil.user_verify_username(mContext, USER_VERIFY_USERNAME, mEt_uname.getText().toString());
                    }else {
                        StringUtil.makeToast(mContext, StringUtil.MSG_NO_PHOTO);
                    }
                }else{
                    mTv_err_pwd.setVisibility(View.VISIBLE);
                    StringUtil.makeToast(mContext, pwdValidation);
                }
            }else{
                StringUtil.makeToast(mContext, StringUtil.MSG_FIELD_EMPTY);
            }
        }
    };

    //
    // TODO: Methods
    //

    private static final int TOGGLE_CASE_UNAME = 0;
    private static final int TOGGLE_CASE_PWD = 1;

    private void toggleErrFeedback(boolean _isShow, int _case){
        int visibility;

        if(_isShow) visibility = View.VISIBLE;
        else visibility = View.GONE;

        if(_case == TOGGLE_CASE_UNAME){
            mTv_err_uname.setVisibility(visibility);
        }else if(_case == TOGGLE_CASE_PWD){
            mTv_err_pwd.setVisibility(visibility);
        }
    }

    private int SELECT_IMAGE = 200;

    private void getImageFromAlbum(){

        //TODO manifest
        Intent selectIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectIntent.setType("image/*");
        galleryResultLauncher.launch(selectIntent);

    }

    public static final String PKG_NAME = "com.example.seanlee_thefootballgallery_2201";
    public static final String IMAGE_FOLDER = "images";
    public static final String IMAGE_NAME_POSTFIX = ".jpg";

    private void getImageFromCamera(){

//        File imageRef = createImageFile();
//        Uri imageFileUri = FileProvider.getUriForFile(mContext, PKG_NAME, imageRef);
//
//        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        camIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

//
        File imageRef = createImageFile();
        mCameraImgUri = FileProvider.getUriForFile(mContext, PKG_NAME, imageRef);

        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImgUri);
//
        cameraResultLauncher.launch(camIntent);
    }

    private File createImageFile(){
        File external = getExternalFilesDir(IMAGE_FOLDER);
        File imageFileRef = new File(external, StringUtil.getTimeStamp() + IMAGE_NAME_POSTFIX);
        try {
            imageFileRef.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        return imageFileRef;
    }

    ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                Uri selectedImg = result.getData().getData();
                mBtn_photo.setImageURI(selectedImg);
                mUri = selectedImg.toString();
            }
        }
    });

    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                mBtn_photo.setImageURI(mCameraImgUri);
                mUri = mCameraImgUri.toString();
            }
        }
    });

    //
    // TODO: Receiver
    //

    private static final String USER_VERIFY_USERNAME = "USERS_VERIFY_USERNAME";

    private static final String AUTH_CREATE_ACCOUNT = "AUTH_CREATE_ACCOUNT";
    private static final String AUTH_CREATE_USER_IN_USERS = "AUTH_CREATE_USER_IN_USERS";



    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean success = intent.getBooleanExtra(DBUtil.EXTRA_BOOLEAN, false);
            String action = intent.getAction();

            if(action == USER_VERIFY_USERNAME){
                if(success){
                    DBUtil.auth_signup_create_account(mContext, AUTH_CREATE_ACCOUNT, mEmail, mEt_pwd.getText().toString());
                }else{
                    mTv_err_uname.setVisibility(View.VISIBLE);
                    StringUtil.makeToast(mContext, "Failed to verify username");
                }
            }else if(action == AUTH_CREATE_ACCOUNT){
                if(success){
                    mUser = new User(mEmail, mEt_uname.getText().toString(), null, null, null, null, null, mUri, mSp_emblem_league.getSelectedItemPosition(), mSp_emblem_team.getSelectedItemPosition());
                    DBUtil.user_create_in_users(mContext, AUTH_CREATE_USER_IN_USERS, mUser);
                }else{
                    StringUtil.makeToast(mContext, "Failed to create Auth account");
                }
            }else if(action == AUTH_CREATE_USER_IN_USERS){
                if(success){
                    // TODO: move to receiver
                    Intent actIntent = new Intent(mContext, CoreActivity.class);
                    startActivity(actIntent);
                }else{
                    StringUtil.makeToast(mContext, "Failed to create user in Users");
                }
            }
        }
    }
}
