package com.example.seanlee_thefootballgallery_2201.b_core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Fixture;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.News;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Standing;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Team;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;
import com.example.seanlee_thefootballgallery_2201.f_utils.APIUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.MatchesAsyncTask;
import com.example.seanlee_thefootballgallery_2201.f_utils.NewsAsyncTask;
import com.example.seanlee_thefootballgallery_2201.f_utils.StandingsAsyncTask;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CoreActivity extends AppCompatActivity
        implements NewsAsyncTask.OnFinishNewsAsyncTask, MatchesAsyncTask.OnFinishFixturesAsyncTask, StandingsAsyncTask.OnFinishStandingsAsyncTask,
        NewsFragment.OnOptionSelectedListener, ForumFragment.OnOptionSelectedListener,
        MatchesFragment.OnOptionSelectedListener, StandingsFragment.OnOptionSelectedListener{

    private static final String TAG = "CoreActivity.TAG";

    private static Context mContext;
    private NewsAsyncTask mNewsAsyncTask;
    private MatchesAsyncTask mFixturesAsyncTask;
    private StandingsAsyncTask mStandingsAsyncTask;

    private User mUser;

    private TextView mFilterTag;
    private TextView mSortTag;

    private ArrayList<News> mNewsCollection;
    private ArrayList<Post> mForumCollection;
    private ArrayList<ArrayList<Fixture>> mMatchesCollection;
    private ArrayList<ArrayList<Team>> mStandingCollection;

    private ArrayList<News> mFilteredNews;
    private ArrayList<Post> mFilteredForum;
    private ArrayList<Fixture> mFilteredMatches;
    private ArrayList<Team> mFilteredStandings;

    private boolean[] mNewsFilterSelection = new boolean[]{true, true, true, true, true};
    private boolean[] mForumFilterSelection = new boolean[]{true, true, true, true, true, true, true, true, true};
    private int mForumSortSelection = 0;
    private int mMatchesFilterSelection = 0;
    private int mStandingsFilterSelection = 0;

    private BottomNavigationView mBNV;
    private FloatingActionButton mFAB;

    private int mSort;
    private boolean mIsInitialLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = this.getWindow();
//            Drawable background = this.getResources().getDrawable(R.drawable.gradient_blue_to_green, null);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            //window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent,null));
//            window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent,null));
//            window.setBackgroundDrawable(background);
//        }

        setContentView(R.layout.activity_core);

        toggleProgressDialog(true);

        mFilterTag = findViewById(R.id.core_tv_filter);
        mSortTag = findViewById(R.id.core_tv_sort);

        mContext = this;

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gray_01, null)));

        mBNV = findViewById(R.id.bottom_nav_view);
        mBNV.setOnItemSelectedListener(onBnvClick);
        //mBNV.setSelectedItemId(R.id.bnv_news);

        mFAB = findViewById(R.id.fab_core_add);
        mFAB.setOnClickListener(onFABTap);

        mFAB.setVisibility(GONE);

        DBUtil.user_get_user_info(mContext, GET_USER_INFO);
    }

    RefreshReceiver mGetInfoReceiver = new RefreshReceiver();
    RefreshReceiver mPopulateForumReceiver = new RefreshReceiver();
    RefreshReceiver mUpdateForumReceiver = new RefreshReceiver();
    RefreshReceiver mCheckForumReceiver = new RefreshReceiver();
    RefreshReceiver mRepopForumReceiver = new RefreshReceiver();

    @Override
    protected void onResume() {
        super.onResume();

        if(mBNV.getSelectedItemId() == R.id.bnv_forum){
            populateForumCollection(REPOPULATE_FORUM);
        }


        IntentFilter filter = new IntentFilter();
        filter.addAction(GET_USER_INFO);
        registerReceiver(mGetInfoReceiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(POPULATE_FORUM);
        registerReceiver(mPopulateForumReceiver, filter1);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(UPDATE_FORUM);
        registerReceiver(mUpdateForumReceiver, filter2);

        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(CHECK_FORUM_COUNT);
        registerReceiver(mCheckForumReceiver, filter3);

        IntentFilter filter4 = new IntentFilter();
        filter4.addAction(REPOPULATE_FORUM);
        registerReceiver(mRepopForumReceiver, filter4);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mGetInfoReceiver);
        unregisterReceiver(mPopulateForumReceiver);
        unregisterReceiver(mUpdateForumReceiver);
        unregisterReceiver(mCheckForumReceiver);
        unregisterReceiver(mRepopForumReceiver);

    }

    NavigationBarView.OnItemSelectedListener onBnvClick = new NavigationBarView.OnItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            replaceFragment(item.getItemId());
            return true;
        }
    };

    View.OnClickListener onFABTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mIsInitialLaunch = false; // TODO: MUST APPLY FOR EDIT FORUM AS WELL

            // TODO: open ForumFormActivity
            Intent intent = new Intent(mContext, ForumFormActivity.class);

            intent.putExtra(Intent.EXTRA_USER, mUser);
            startActivity(intent);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void replaceFragment(int _selectedId){

        toggleFAB(false);

        Fragment fragment;
        int layoutId;

        switch (_selectedId){

            case R.id.bnv_news:
                setTitle("News");

                filterNewsCollection();

                fragment = NewsFragment.newInstance(mFilteredNews, mNewsFilterSelection);
                break;
            case R.id.bnv_forum:
                setTitle("Forum");
                toggleFAB(true);

                filterForumCollection();
                sortForumCollection();

                fragment = ForumFragment.newInstance(mFilteredForum, mUser, mForumFilterSelection, mForumSortSelection);
                break;
            case R.id.bnv_matches:
                setTitle("Matches");

                filterMatchesCollection();
                fragment = MatchesFragment.newInstance(mFilteredMatches, mMatchesFilterSelection);
                break;
            case R.id.bnv_standings:
                setTitle("Standings");

                filterStandingsCollection();
                fragment = StandingsFragment.newInstance(mFilteredStandings, mStandingsFilterSelection);
                break;
            default:
                fragment = null;
                break;
        }

        editOptionTags(_selectedId);
        toggleProgressDialog(false);
        if(fragment != null) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void toggleProgressDialog(boolean _isLoading){
        ConstraintLayout coreLayout = findViewById(R.id.entire_core_view);
        FrameLayout progressLayout = findViewById(R.id.core_progressBarView);

        if(_isLoading){
            coreLayout.setVisibility(GONE);
            progressLayout.setVisibility(VISIBLE);
            setTitle("The Football Gallery");
        }else{
            coreLayout.setVisibility(VISIBLE);
            progressLayout.setVisibility(GONE);
        }
    }

    private void editOptionTags(int _bnvIndex){
        int visibility = GONE;

        String filterText = "";
        String sortText = "";

        if(_bnvIndex == R.id.bnv_news){

            int x = 0;
            for (boolean bool: mNewsFilterSelection) { if(!bool){ x++;} }
            if(x>0) filterText = "custom";
            else filterText = "all";

        }else if(_bnvIndex == R.id.bnv_forum){

            visibility = VISIBLE;
            int x = 0;
            for (boolean bool: mForumFilterSelection) { if(!bool){ x++;} }
            if(x>0) filterText = "custom";
            else filterText = "all";

            if(mForumSortSelection == 0){ sortText = "newest"; }
            else{ sortText = "popular"; }

        }else if(_bnvIndex == R.id.bnv_matches){

            if(mMatchesFilterSelection == 0){ filterText = "EPL"; }
            else if(mMatchesFilterSelection == 1){ filterText = "La Liga"; }
            else if(mMatchesFilterSelection == 2){ filterText = "Bundesliga"; }
            else if(mMatchesFilterSelection == 3){ filterText = "Serie A"; }
            else if(mMatchesFilterSelection == 4){ filterText = "Ligue 1"; }

        }else if(_bnvIndex == R.id.bnv_standings){

            if(mStandingsFilterSelection == 0){ filterText = "EPL"; }
            else if(mStandingsFilterSelection == 1){ filterText = "La Liga"; }
            else if(mStandingsFilterSelection == 2){ filterText = "Bundesliga"; }
            else if(mStandingsFilterSelection == 3){ filterText = "Serie A"; }
            else if(mStandingsFilterSelection == 4){ filterText = "Ligue 1"; }
        }

        mFilterTag.setText(StringUtil.getFilterTagText(filterText));
        mSortTag.setText(StringUtil.getSortTagText(sortText));
        mSortTag.setVisibility(visibility);

    }

    private void toggleFAB(boolean _isShow) {

        if (mFAB != null) {
            if (_isShow) mFAB.setVisibility(View.VISIBLE);
            else mFAB.setVisibility(GONE);
        }
    }

    private static final String ADDRESS_NEWS_API = "https://livescore6.p.rapidapi.com/news/v2/list-by-sport?category=2021020913320920836&page=1";

    private void populateNewsCollection(){

        mNewsAsyncTask = new NewsAsyncTask(CoreActivity.this);
        mNewsAsyncTask.execute(ADDRESS_NEWS_API);
    }

    private void populateForumCollection(String _action){

        DBUtil.forum_get_posts(mContext, _action);
    }

    private void filterNewsCollection(){

        //temp
        mFilteredNews = mNewsCollection;
        //mFilteredNews = new ArrayList<>();

    }

    private void filterForumCollection(){

        if(mForumCollection != null){

            ArrayList<Post> leagueFiltered = new ArrayList<>();
            ArrayList<Post> categoryFiltered = new ArrayList<>();

            // TODO: first, filter by league selection - selections index 0 ~ 4

            for (int i=0; i<mForumFilterSelection.length-4; i++){

                for (int j=0; j<mForumCollection.size(); j++){

                    if(mForumFilterSelection[i]){ // ex) EPL is selected

                        if(mForumCollection.get(j).getLeagueInt() == i){ // ex) when selection index is EPL, and if post's league is EPL

                            leagueFiltered.add(mForumCollection.get(j));
                        }
                    }
                }
            }

            // TODO: second, filter by category selection - selections index 5 ~ 8

            for (int i=5; i<mForumFilterSelection.length; i++){

                for(int j=0; j<leagueFiltered.size(); j++){

                    if(mForumFilterSelection[i]){ // ex) Discussion is selected

                        if(leagueFiltered.get(j).getCategoryInt() == i-5){ // ex) when selection index is Discussion, and if post's category is Discussion

                            categoryFiltered.add(leagueFiltered.get(j));
                        }
                    }
                }
            }
            mFilteredForum = categoryFiltered;
        }else{
            mFilteredForum = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortForumCollection(){

        if(mFilteredForum != null){
            if(mForumSortSelection == 0){ // by date

                mFilteredForum.sort((o1, o2) -> o2.getDateDate().compareTo(o1.getDateDate()));


            }else{ // by popular

                // TODO: sort by likes + comments count. comments are doubled when calculating.
                mFilteredForum.sort((o1, o2) -> o2.getPopularity().compareTo(o1.getPopularity()));

            }
        }
    }

    private void filterMatchesCollection(){
        mFilteredMatches = mMatchesCollection.get(mMatchesFilterSelection);
    }

    private void filterStandingsCollection(){
        mFilteredStandings = mStandingCollection.get(mStandingsFilterSelection);
    }

    private void populateMatchesCollection(){
        // TODO
        mFixturesAsyncTask = new MatchesAsyncTask(CoreActivity.this);
        mFixturesAsyncTask.execute("");
    }

    private void populateStandingsCollection(){
        // TODO
        mStandingsAsyncTask = new StandingsAsyncTask(CoreActivity.this);
        mStandingsAsyncTask.execute("");
    }

    //
    // TODO: Post-AsyncTask
    //

    @Override
    public void onPostNewsAsync(String _result) { // TODO: post-execution NewsAsyncTask

        Log.i(TAG, "onPostNewsAsync");

        //mNewsCollection = APIUtil.getRedditNewsJSON(_result);
        mNewsCollection = APIUtil.getLSNewsJSON(_result);

        // TODO: get forum
        populateForumCollection(POPULATE_FORUM);

        // TODO: maybe get rid of it?
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, NewsFragment.newInstance(mNewsCollection)).commit();
    }

    @Override
    public void onPostFixturesAsync(ArrayList<String> _results) {

        mMatchesCollection = new ArrayList<>();

        for (String result: _results) {
            mMatchesCollection.add(APIUtil.getMatchesJSON(result));
        }

        populateStandingsCollection();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPostStandingsAsync(ArrayList<String> _results) {

        mStandingCollection = new ArrayList<>();

        for (String result: _results) {
            mStandingCollection.add(APIUtil.getStandingsJSON(mContext, result));
        }



        mBNV.setSelectedItemId(0);
        replaceFragment(mBNV.getSelectedItemId());
    }



    //
    // TODO: Pre-population
    //

    // TODO: backend
    public static final String GET_USER_INFO = "GET_USER_INFO";

    // TODO: core
    public static final String POPULATE_FORUM = "POPULATE_FORUM";

    public static final String CHECK_FORUM_COUNT = "CHECK_FORUM_COUNT";

    public static final String UPDATE_FORUM = "UPDATE_FORUM";

    public static final String REPOPULATE_FORUM = "REPOPULATE_FORUM";

    @Override
    public void replaceNewsFragment(boolean[] _filterOptions) {
        mNewsFilterSelection = _filterOptions;
        editOptionTags(R.id.bnv_news);
        filterNewsCollection();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, NewsFragment.newInstance(mFilteredNews, _filterOptions)).commit();
    }

    @Override
    public void replaceForumFragment(boolean[] _filterOptions, int _sortOptions) {
        mForumFilterSelection = _filterOptions;
        mForumSortSelection = _sortOptions;
        editOptionTags(R.id.bnv_forum);
        filterForumCollection();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ForumFragment.newInstance(mFilteredForum, mUser, _filterOptions, _sortOptions)).commit();
    }

    @Override
    public void replaceMatchesFragment(int _filterOption) {
        mMatchesFilterSelection = _filterOption;
        editOptionTags(R.id.bnv_matches);
        filterMatchesCollection();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MatchesFragment.newInstance(mFilteredMatches, _filterOption)).commit();
    }

    @Override
    public void replaceStandingsFragment(int _filterOption) {
        mStandingsFilterSelection = _filterOption;
        editOptionTags(R.id.bnv_standings);
        filterStandingsCollection();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, StandingsFragment.newInstance(mFilteredStandings, _filterOption)).commit();
    }


    class RefreshReceiver extends BroadcastReceiver{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean result = intent.getBooleanExtra(DBUtil.EXTRA_BOOLEAN, false);

            //
            // TODO: Pre-load
            //
            if(intent.getAction() == GET_USER_INFO){
                if(result){
                    if(intent.hasExtra(DBUtil.EXTRA_SERIALIZABLE) && intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE) != null){
                        mUser = (User) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);

                        Log.i(TAG, "Receiver: get_user_info");

                        populateNewsCollection(); // TODO: check for request count?
                        //populateForumCollection();

                        //replaceFragment(mBNV.getSelectedItemId());
                    }else{

                    }
                }else {

                }
            }else if(intent.getAction() == POPULATE_FORUM){
                if(result){
                    if(intent.hasExtra(DBUtil.EXTRA_SERIALIZABLE)){ // serializable null check removed
                        // TODO: populate forum collection
                        mForumCollection = (ArrayList<Post>) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);

                        Log.i(TAG, "Receiver: populate forum");
                        // TODO: populate matches collection
                        populateMatchesCollection();

                        // TODO: replace fragment
//                        mBNV.setSelectedItemId(0);
//                        replaceFragment(mBNV.getSelectedItemId());

                    }else{

                    }
                }else {

                }

            }
            //
            // TODO: on Update
            //
            else if(intent.getAction() == UPDATE_FORUM) {

                DBUtil.forum_get_posts(mContext, REPOPULATE_FORUM);

            }else if(intent.getAction() == REPOPULATE_FORUM){

                if(result){

                    if(intent.hasExtra(DBUtil.EXTRA_SERIALIZABLE) && intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE) != null){

                        // TODO: re-populate forum collection
                        mForumCollection = (ArrayList<Post>) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);

                        // TODO: populate matches/standings
                        replaceFragment(mBNV.getSelectedItemId());

                    }else{

                    }


                }else {

                }

            }else if(intent.getAction() == CHECK_FORUM_COUNT){
                if(result){// diff count
                    DBUtil.forum_get_posts(mContext, REPOPULATE_FORUM);
                    //mIsInitialLaunch = true;
                }else{

                }
            }


        }
    }
}
