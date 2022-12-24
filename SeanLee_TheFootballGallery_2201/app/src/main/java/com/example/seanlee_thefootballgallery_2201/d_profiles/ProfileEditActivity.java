package com.example.seanlee_thefootballgallery_2201.d_profiles;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.SpinnerAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProfileEditActivity extends AppCompatActivity {

    public static final String TAG = "ProfileEditActivity.TAG";

    private Context mContext;
    private User mUser;
    private User mNewUser;
    private String mUri;

    EditText et_uname;
    Spinner sp_league, sp_team;
    ImageView iv_photo;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mContext = this;

        mUser = (User) getIntent().getSerializableExtra(Intent.EXTRA_USER);

        et_uname = findViewById(R.id.profile_edit_et_uname);
        sp_league = findViewById(R.id.profile_edit_sp_league);
        sp_team = findViewById(R.id.profile_edit_sp_team);
        iv_photo = findViewById(R.id.profile_edit_iv_photo);
        btn_save = findViewById(R.id.profile_edit_btn_save);

        et_uname.setText(mUser.getUName());
        iv_photo.setImageURI(Uri.parse(mUser.getPhotoString()));

        setUpArrays();
        sp_league.setOnItemSelectedListener(onLeagueChange);
        setUpSpinner(mUser.getEmblemLeagueInt(), mUser.getEmblemTeamInt());

        sp_team.setSelection(mUser.getEmblemTeamInt());

        btn_save.setOnClickListener(onSaveTap);
    }

    VerificationReceiver mVerifyUNameReceiver = new VerificationReceiver();
    VerificationReceiver mUpdateInfoReceiver = new VerificationReceiver();

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(VERIFY_USERNAME);
        registerReceiver(mVerifyUNameReceiver, filter);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(UPDATE_PROFILE);
        registerReceiver(mUpdateInfoReceiver, filter2);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mVerifyUNameReceiver);
        unregisterReceiver(mUpdateInfoReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.profile_edit_menu_album) getImageFromAlbum();
        else if(item.getItemId() == R.id.profile_edit_menu_camera) getImageFromCamera();

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemSelectedListener onLeagueChange = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            updateSpinner(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }
    };

    View.OnClickListener onSaveTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(mUri == null){
                mUri = mUser.getPhotoString();
            }

            if(StringUtil.isFieldValid(new EditText[]{et_uname})) {
                if (!et_uname.getText().toString().equals(mUser.getUName())) {
                    DBUtil.user_verify_username(mContext, VERIFY_USERNAME, et_uname.getText().toString());
                } else {
                    mNewUser = new User(mUser.getId(), et_uname.getText().toString(), mUser.getPosts(), mUser.getPostLikes(), mUser.getPostYCards(), mUser.getCommentLikes(), mUser.getCommentYCards(), mUri, sp_league.getSelectedItemPosition(), sp_team.getSelectedItemPosition());
                    DBUtil.user_update_profile(mContext, UPDATE_PROFILE, mUser, mNewUser);
                }
            }else {
                StringUtil.makeToast(mContext, StringUtil.MSG_FIELD_EMPTY);
            }
        }
    };

    private ArrayList<String> mTitles_EPL, mTitles_LaLiga, mTitles_Bundes, mTitles_SerieA, mTitles_Ligue1;
    private ArrayList<Integer> mLogoIds_EPL, mLogoIds_LaLiga, mLogoIds_Bundes, mLogoIds_SerieA, mLogoIds_Ligue1;

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
                = new SpinnerAdapter(mContext,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.leagues_full))),
                leagueLogoIds);
        sp_league.setAdapter(leagueAdapter);
        sp_league.setSelection(_leagueIndex); // initial league - EPL

        ArrayList<String> titles = null;
        ArrayList<Integer> logos = null;

        if(_leagueIndex == 0){
            titles = mTitles_EPL;
            logos = mLogoIds_EPL;
        }else if(_leagueIndex == 1){
            titles = mTitles_LaLiga;
            logos = mLogoIds_LaLiga;
        }else if(_leagueIndex == 2){
            titles = mTitles_Bundes;
            logos = mLogoIds_Bundes;
        }else if(_leagueIndex == 3){
            titles = mTitles_SerieA;
            logos = mLogoIds_SerieA;
        }else if(_leagueIndex == 4){
            titles = mTitles_Ligue1;
            logos = mLogoIds_Ligue1;
        }

        SpinnerAdapter teamAdapter = new SpinnerAdapter(mContext,
                titles, logos);
        sp_team.setAdapter(teamAdapter);
//        sp_team.setSelection(_teamIndex); // initial team - Manchester United
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
        }
        SpinnerAdapter teamAdapter = new SpinnerAdapter(mContext, itemTitles, logoIds_int);
        sp_team.setAdapter(teamAdapter);
        if(mUser.getEmblemTeamInt() > itemTitles.size()){
            sp_team.setSelection(0);
        }else{
            sp_team.setSelection(mUser.getEmblemTeamInt());
        }
    }

    public static final String PKG_NAME = "com.example.seanlee_thefootballgallery_2201";
    public static final String IMAGE_FOLDER = "images";
    public static final String IMAGE_NAME_POSTFIX = ".jpg";

    private Uri mCameraImgUri;

    private void getImageFromAlbum(){

        //TODO manifest
        Intent selectIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectIntent.setType("image/*");
        galleryResultLauncher.launch(selectIntent);

    }

    private void getImageFromCamera(){

        File imageRef = createImageFile();
        mCameraImgUri = FileProvider.getUriForFile(mContext, PKG_NAME, imageRef);

        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImgUri);

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
                        iv_photo.setImageURI(selectedImg);
                        mUri = selectedImg.toString();
                    }
                }
            });

    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        iv_photo.setImageURI(mCameraImgUri);
                        mUri = mCameraImgUri.toString();
                    }
                }
            });

    public static final String VERIFY_USERNAME = "VERIFY_USERNAME";
    public static final String UPDATE_PROFILE = "UPDATE_PROFILE";

    class VerificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean result = intent.getBooleanExtra(DBUtil.EXTRA_BOOLEAN, false);

            if(intent.getAction() == VERIFY_USERNAME){
                if (result){
                    mNewUser = new User(mUser.getId(), et_uname.getText().toString(), mUser.getPosts(), mUser.getPostLikes(), mUser.getPostYCards(), mUser.getCommentLikes(), mUser.getCommentYCards(), mUri, sp_league.getSelectedItemPosition(), sp_team.getSelectedItemPosition());
                    DBUtil.user_update_profile(mContext, UPDATE_PROFILE, mUser, mNewUser);
                }else {
                    StringUtil.makeToast(mContext, "Username exists.");
                }
            }else if(intent.getAction() == UPDATE_PROFILE){
                if(result){
                    finish();
                }else {
                    StringUtil.makeToast(mContext, "Failed to save edited data.");
                }
            }
        }
    }
}
