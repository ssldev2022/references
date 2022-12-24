package com.example.seanlee_thefootballgallery_2201.d_profiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.a_authentications.AuthHome;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "ProfileActivity.TAG";

    private Context mContext;
    private User mUser;
    private boolean mIsInitialLaunch = true;

    PostReceiver mGetInfoReceiver = new PostReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = this;
    }

    @Override
    protected void onResume() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(GET_USER_INFO);
        registerReceiver(mGetInfoReceiver, filter);

        // get userinfo
        DBUtil.user_get_user_info(mContext, GET_USER_INFO);

        super.onResume();
    }

    @Override
    protected void onPause() {

        unregisterReceiver(mGetInfoReceiver);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu); // TODO
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.profile_menu_edit){
            // edit activity intent with user
            Intent intent = new Intent(mContext, ProfileEditActivity.class);
            intent.putExtra(Intent.EXTRA_USER, mUser);
            startActivity(intent);
        }else if(item.getItemId() == R.id.profile_menu_sign_out){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(mContext, AuthHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        mIsInitialLaunch = false;
        return super.onOptionsItemSelected(item); // TODO
    }

    private void populateUserInfo(){
        TextView tv_uname = findViewById(R.id.profile_tv_uname);
        ImageView iv_emblem = findViewById(R.id.profile_iv_emblem);
        ImageView iv_photo = findViewById(R.id.profile_iv_photo);

        tv_uname.setText(mUser.getUName());

        iv_emblem.setImageResource(mUser.getEmblemForImageView(mContext));
        iv_photo.setImageURI(Uri.parse(mUser.getPhotoString()));
        iv_photo.setBackground(getDrawable(R.drawable.round_btn));
        iv_photo.setScaleType(ImageView.ScaleType.FIT_XY);
        //content://com.example.seanlee_thefootballgallery_2201/app_images/10-02-2022-09-17-01.jpg
        //content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F25/ORIGINAL/NONE/image%2Fjpeg/967503137
        //content://com.android.providers.media.documents/document/image%3A28
    }

    private void reloadFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_profile_lv, ProfilePostFragment.newInstance(mUser)).commit();
    }

//    @Override
//    public void onEditFinish() {
//        // update fragment
//        DBUtil.user_get_user_info(mContext, UPDATE_USER_INFO);
//    }

    public static final String GET_USER_INFO = "GET_USER_INFO";
    public static final String UPDATE_USER_INFO = "UPDATE_USER_INFO";

    class PostReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean result = intent.getBooleanExtra(DBUtil.EXTRA_BOOLEAN, false);

            if(intent.getAction() == GET_USER_INFO){
                if(result){
                    if(intent.hasExtra(DBUtil.EXTRA_SERIALIZABLE) && intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE) != null){
                        mUser = (User) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);
                        populateUserInfo();
                        reloadFragment();
                    }else {

                    }
                }else {

                }
            }
//            else if(intent.getAction() == UPDATE_USER_INFO){
//                if(result){
//                    if(intent.hasExtra(DBUtil.EXTRA_SERIALIZABLE) && intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE) != null){
//                        mUser = (User) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);
//                        populateUserInfo();
//                        reloadFragment();
//                    }else {
//
//                    }
//                }else {
//
//                }
//            }
        }
    }
}
