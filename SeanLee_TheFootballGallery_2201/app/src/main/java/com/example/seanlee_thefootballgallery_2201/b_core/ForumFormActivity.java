package com.example.seanlee_thefootballgallery_2201.b_core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.SpinnerAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class ForumFormActivity extends AppCompatActivity {

    public static final String TAG = "ForumFormActivity.TAG";

    private Context mContext;
    private Post mNewPost;
    private Post mOldPost;
    private ArrayList<String> mOldPostIds;
    private boolean mIsNewForm = false;
    private User mAuthor;

    private Spinner mSp_league, mSp_category;
    private TextInputEditText mEt_title, mEt_content;

    public static final String EXTRA_OLD_POST_IDs = "OLD_POST_IDs";
    public static final String EXTRA_POST_TO_EDIT = "POST_TO_EDIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_form);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gray_01, null)));

        mContext = this;


        Button btnPost = findViewById(R.id.forum_form_btn_post);

        mSp_league = findViewById(R.id.forum_form_sp_league);
        mSp_category = findViewById(R.id.forum_form_sp_category);
        mEt_title = findViewById(R.id.forum_form_tf_title);
        mEt_content = findViewById(R.id.forum_form_tf_content);

        setupSpinner();

        btnPost.setOnClickListener(onPostTap);

        // UI
        if(getIntent().hasExtra(Intent.EXTRA_USER)){
            mAuthor = (User) getIntent().getSerializableExtra(Intent.EXTRA_USER);
            mOldPostIds = mAuthor.getPostIds();

            if(getIntent().hasExtra(EXTRA_POST_TO_EDIT)){
                mOldPost = (Post) getIntent().getSerializableExtra(EXTRA_POST_TO_EDIT);
                mEt_title.setText(mOldPost.getTitle());
                mEt_content.setText(mOldPost.getDesc());
                mSp_category.setSelection(mOldPost.getCategoryInt());
                mSp_league.setSelection(mOldPost.getLeagueInt());
            }else {
                mOldPost = null;
                mSp_league.setSelection(0);
                mSp_category.setSelection(0);
            }
        }else{
            // error
        }

        DBUtil.user_get_user_detail_with_email(mContext, GET_USER_INFO, FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    private Receiver mUserInfoReceiver = new Receiver();
    private Receiver mPostsCreateReceiver = new Receiver();
    private Receiver mPostsUpdateReceiver = new Receiver();
    private Receiver mUsersUpdateReceiver = new Receiver();

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        IntentFilter filter2 = new IntentFilter();
        IntentFilter filter3 = new IntentFilter();
        IntentFilter filter4 = new IntentFilter();

        filter.addAction(UPDATE_POST_IN_POSTS);
        filter2.addAction(UPDATE_POST_IN_USERS);
        filter3.addAction(CREATE_POST_IN_POSTS);
        filter4.addAction(GET_USER_INFO);

        registerReceiver(mPostsUpdateReceiver, filter);
        registerReceiver(mUsersUpdateReceiver, filter2);
        registerReceiver(mPostsCreateReceiver, filter3);
        registerReceiver(mUserInfoReceiver, filter4);

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mPostsUpdateReceiver);
        unregisterReceiver(mUsersUpdateReceiver);
        unregisterReceiver(mPostsCreateReceiver);
        unregisterReceiver(mUserInfoReceiver);
    }

    View.OnClickListener onPostTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO: check league, category, title

            int category = mSp_category.getSelectedItemPosition();
            int league = mSp_league.getSelectedItemPosition();

            mNewPost = new Post(mEt_title.getText().toString(), mEt_content.getText().toString(), category, league);
            mNewPost.setAuthor(mAuthor.getId());
            mNewPost.setLeague(mSp_league.getSelectedItemPosition());
            mNewPost.setCategory(mSp_category.getSelectedItemPosition());
            mNewPost.setAuthorUname(mAuthor.getUName());
            mNewPost.setAuthorEmblemLeagueInt(mAuthor.getEmblemLeagueInt());
            mNewPost.setAuthorEmblemTeamInt(mAuthor.getEmblemTeamInt());

            // TODO: check title
            if(StringUtil.isFieldValid(new EditText[]{mEt_title})){
                if(mOldPost != null){
                    DBUtil.forum_update_post(mContext, UPDATE_POST_IN_POSTS, mOldPost, mNewPost);
                }else{
                    DBUtil.forum_create_post(mContext, CREATE_POST_IN_POSTS, mNewPost);
                }
            }else {
                StringUtil.makeToast(mContext, "Title cannot be empty.");
            }
        }
    };

    private void setupSpinner(){

        ArrayList<Integer> leagueLogoIds = new ArrayList<>();
        for (String league: getResources().getStringArray(R.array.leagues_logo_str)) {
            leagueLogoIds.add(StringUtil.getDrawableIdByString(mContext, league));
        }

        SpinnerAdapter leagueAdapter = new SpinnerAdapter(mContext,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.leagues_abbv))),
                leagueLogoIds);

        mSp_league.setAdapter(leagueAdapter);

        ArrayAdapter<CharSequence> cateAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.category, android.R.layout.simple_spinner_item);
        cateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp_category.setAdapter(cateAdapter);
        mSp_category.setOnItemSelectedListener(cateSpinnerListener);
    }

    AdapterView.OnItemSelectedListener cateSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }
    };

    private void endActivityWithBroadcast(){
        // TODO: interface -> update forum?
        Intent broadcastIntent = new Intent(CoreActivity.UPDATE_FORUM);
        mContext.sendBroadcast(broadcastIntent);
        finish();
    }

    public static final String GET_USER_INFO = "GET_USER_INFO";
    public static final String CREATE_POST_IN_POSTS = "CREATE_POST_IN_POSTS";
    public static final String UPDATE_POST_IN_POSTS = "UPDATE_POST_IN_POSTS";
    public static final String UPDATE_POST_IN_USERS = "UPDATE_POST_IN_USERS";

    class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean result = intent.getBooleanExtra(DBUtil.EXTRA_BOOLEAN, false);

            if(intent.getAction() == GET_USER_INFO){

                mAuthor = (User) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);

            }else if(intent.getAction() == CREATE_POST_IN_POSTS){ // add

                if(result){
                    DBUtil.user_update_posts_value(mContext, UPDATE_POST_IN_USERS, mOldPostIds, mNewPost.getId(), 1); // case add
                }else {

                }

            }else if(intent.getAction() == UPDATE_POST_IN_POSTS){ // edit

                if(result){
                    finish();
                }else{

                }

            }else if(intent.getAction() == UPDATE_POST_IN_USERS){

                if(result){
                    endActivityWithBroadcast();
                }else {

                }

            }
        }
    }
}
