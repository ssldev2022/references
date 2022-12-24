package com.example.seanlee_thefootballgallery_2201.b_core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.CommentAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Comment;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ForumDetailActivity extends AppCompatActivity implements CommentAdapter.Callback {

    public static final String TAG = "ForumDetailActivity.TAG";

    public static final String EXTRA_POST = "EXTRA_POST";

    private Context mContext;
    private Post mPost;
    private Post mNewPost;
    private ArrayList<Comment> mComments;
    private User mAuthor;
    private User mCurrentUser;
    private boolean mIsMyPost = false;
    private String mCurrentParentId = "";
    private String mCurrentMentionId = "";

    private EditText mEt_comment;
    private TextView mTv_reply_desc;
    private ImageButton mBtn_undo_reply;
    private FrameLayout mCommentFrame;

    private ImageButton mBtn_like;
    private ImageButton mBtn_ycard;

    private Comment mComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gray_01, null)));

        mContext = this;

        // get intent
        // get author -> mod menu
        mPost = (Post) getIntent().getSerializableExtra(EXTRA_POST);
        mAuthor = (User) getIntent().getSerializableExtra(Intent.EXTRA_USER);
        //mUname = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if(mPost.getAuthor().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) mIsMyPost = true;

        DBUtil.user_get_user_info(mContext, GET_CURRENT_USER);
//        tv_like.setText(mPost.getLikes());
//        tv_ycard.setText(mPost.getYCards());
//        tv_comment.setText(mPost.getComments().size());
    }

    UserReceiver mReceiver = new UserReceiver();
    UserReceiver mPostDeleteReceiver = new UserReceiver();
    UserReceiver mPostDeleteInUserReceiver = new UserReceiver();
    UserReceiver mUpdateReceiver = new UserReceiver();
    UserReceiver mCurrUserReceiver = new UserReceiver();
    UserReceiver mCommentResultReceiver = new UserReceiver();
    UserReceiver mGetCommentsReceiver = new UserReceiver();
    UserReceiver mUpdateValueReceiver = new UserReceiver();

    // TODO
    UserReceiver mReceiver1 = new UserReceiver();
    UserReceiver mReceiver2 = new UserReceiver();
    UserReceiver mReceiver3 = new UserReceiver();
    UserReceiver mReceiver4 = new UserReceiver();
    UserReceiver mReceiver5 = new UserReceiver();
    UserReceiver mReceiver6 = new UserReceiver();
    UserReceiver mReceiver7 = new UserReceiver();
    UserReceiver mReceiver8 = new UserReceiver();
    UserReceiver mReceiver9 = new UserReceiver();



    @Override
    protected void onResume() {
        super.onResume();

        // get post
        DBUtil.forum_get_post_with_id(mContext, UPDATE_FIELDS, mPost.getId());

        IntentFilter filter4 = new IntentFilter();
        filter4.addAction(GET_CURRENT_USER);
        registerReceiver(mCurrUserReceiver, filter4);

        IntentFilter filter = new IntentFilter();
        filter.addAction(GET_AUTHOR_INFO);
        registerReceiver(mReceiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(DELETE_POST_IN_POSTS);
        registerReceiver(mPostDeleteReceiver, filter1);

        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(DELETE_POST_IN_USERS);
        registerReceiver(mPostDeleteInUserReceiver, filter3);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(UPDATE_FIELDS);
        registerReceiver(mUpdateReceiver, filter2);

        IntentFilter filter5 = new IntentFilter();
        filter5.addAction(UPDATE_COMMENT);
        registerReceiver(mCommentResultReceiver, filter5);

        IntentFilter filter6 = new IntentFilter();
        filter6.addAction(FORUM_GET_COMMENTS);
        registerReceiver(mGetCommentsReceiver, filter6);

        IntentFilter filter7 = new IntentFilter();
        filter7.addAction(UPDATE_VALUE);
        registerReceiver(mUpdateValueReceiver, filter7);

        // TODO
        IntentFilter filter8 = new IntentFilter();
        filter8.addAction(UPDATE_USER_LIKE);
        registerReceiver(mReceiver1, filter8);

        IntentFilter filter9 = new IntentFilter();
        filter9.addAction(UPDATE_USER_YCARD);
        registerReceiver(mReceiver2, filter9);

        IntentFilter filter10 = new IntentFilter();
        filter10.addAction(UPDATE_POST_AFTER_LIKE);
        registerReceiver(mReceiver3, filter10);

        IntentFilter filter11 = new IntentFilter();
        filter11.addAction(UPDATE_POST_AFTER_YCARD);
        registerReceiver(mReceiver4, filter11);

        IntentFilter filter12 = new IntentFilter();
        filter12.addAction(USER_UPDATE_LIKE_VALUE_COMMENT);
        registerReceiver(mReceiver5, filter12);

        IntentFilter filter13 = new IntentFilter();
        filter13.addAction(COMMENT_UPDATE_LIKE_VALUE);
        registerReceiver(mReceiver6, filter13);

        IntentFilter filter14 = new IntentFilter();
        filter14.addAction(GET_UPDATED_POST);
        registerReceiver(mReceiver7, filter14);

        IntentFilter filter15 = new IntentFilter();
        filter15.addAction(USER_UPDATE_YCARD_VALUE_COMMENT);
        registerReceiver(mReceiver8, filter15);

        IntentFilter filter16 = new IntentFilter();
        filter16.addAction(COMMENT_UPDATE_YCARD_VALUE);
        registerReceiver(mReceiver9, filter16);

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mCurrUserReceiver);
        unregisterReceiver(mReceiver);
        unregisterReceiver(mPostDeleteReceiver);
        unregisterReceiver(mUpdateReceiver);
        unregisterReceiver(mPostDeleteInUserReceiver);
        unregisterReceiver(mCommentResultReceiver);
        unregisterReceiver(mGetCommentsReceiver);
        unregisterReceiver(mUpdateValueReceiver);
    }

    private void populateInfo(){

        TextView tv_filter = findViewById(R.id.forum_detail_tv_filter);
        TextView tv_category = findViewById(R.id.forum_detail_tv_category);
        ImageView iv_emblem = findViewById(R.id.forum_detail_iv_emblem);
        TextView tv_uname = findViewById(R.id.forum_detail_tv_uname);
        TextView tv_date = findViewById(R.id.forum_detail_tv_date);
        TextView tv_title = findViewById(R.id.forum_detail_tv_title);
        TextView tv_desc = findViewById(R.id.forum_detail_tv_desc);

        TextView tv_like = findViewById(R.id.forum_detail_tv_like);
        TextView tv_ycard = findViewById(R.id.forum_detail_tv_ycard);
        TextView tv_comment = findViewById(R.id.forum_detail_tv_comment);

        TextView tv_literal_comment = findViewById(R.id.forum_detail_text_comments);

        mCommentFrame = findViewById(R.id.fragment_container_forum_detail_comment_lv);

        mEt_comment = findViewById(R.id.forum_detail_et_comment);

        Button btn_post_comment = findViewById(R.id.forum_detail_btn_post_comment);
        btn_post_comment.setOnClickListener(onCommentTap);

        mBtn_like = findViewById(R.id.forum_detail_btn_like);
        mBtn_like.setOnClickListener(onLikeIconTap);
        mBtn_ycard = findViewById(R.id.forum_detail_btn_ycard);
        mBtn_ycard.setOnClickListener(onYCardIconTap);

        if(mCurrentUser.getPostLikes().contains(mPost.getId())){
            mBtn_like.setBackground(getDrawable(R.drawable.ic_like_clicked));
        }

        if(mCurrentUser.getPostYCards().contains(mPost.getId())){
            mBtn_like.setBackground(getDrawable(R.drawable.ic_ycard_clicked));
            mBtn_like.setClickable(false);
        }

        // TODO: going to implement on tap comment icon?
        ImageButton btn_comment = findViewById(R.id.forum_detail_btn_comment);
        btn_comment.setOnClickListener(onCommentIconTap);

        mBtn_undo_reply = findViewById(R.id.forum_detail_btn_undo_reply);
        mBtn_undo_reply.setOnClickListener(onUndoReplyTap);

        mTv_reply_desc = findViewById(R.id.forum_detail_tv_reply_desc);

        tv_filter.setText(mPost.getLeagueStr());
        tv_filter.setBackground(mPost.getDrawableTags(mContext)[1]);
        tv_category.setText(mPost.getCategoryStr());
        tv_category.setBackground(mPost.getDrawableTags(mContext)[0]);
        //iv_emblem.setImageResource(mAuthor.getEmblemForImageView(mContext)); // TODO: get User data
        iv_emblem.setImageResource(mPost.getEmblemForImageView(mContext));
//        tv_uname.setText(mAuthor.getUName());
        tv_uname.setText(mPost.getAuthorUName());
        tv_date.setText(mPost.getTimeGap());
        tv_title.setText(mPost.getTitle());
        tv_desc.setText(mPost.getDesc());
        if(mPost.getDesc().length() == 0) tv_desc.setVisibility(View.GONE);
        if(mPost.getCommentSize() == 0) {
            tv_literal_comment.setVisibility(View.GONE);
            mCommentFrame.setVisibility(View.GONE);
        }

        tv_like.setText(String.valueOf(mPost.getLikes()));
        tv_ycard.setText(String.valueOf(mPost.getYCards()));
        tv_comment.setText(String.valueOf(mPost.getCommentSize()));

        // TODO: implement buttons

        // TODO: ListFragment

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_forum_detail_comment_lv, ForumDetailCommentFragment.newInstance(mCurrentUser, mComments)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mIsMyPost) getMenuInflater().inflate(R.menu.forum_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.forum_detail_menu_edit){

            Intent intent = new Intent(mContext, ForumFormActivity.class);
            intent.putExtra(Intent.EXTRA_USER, mAuthor);
            intent.putExtra(ForumFormActivity.EXTRA_POST_TO_EDIT, mPost);
            startActivity(intent);

        }else if(item.getItemId() == R.id.forum_detail_menu_delete){

            DBUtil.forum_delete_post(mContext, DELETE_POST_IN_POSTS, mPost.getId());
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onCommentTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Log.i(TAG, "onClickComment ");

            // TODO: to get currParentId/currMentionId handle on reply tap from adapter

            String commentText = mEt_comment.getText().toString();
            if(StringUtil.isFieldValid(new EditText[]{mEt_comment})){

                Comment comment = new Comment(mPost.getId(), mCurrentUser, "", commentText, mCurrentParentId, mCurrentMentionId);

                mNewPost = mPost;
                ArrayList<String> newComments = new ArrayList<>();
                if(mPost.getComments() == null){
                    mNewPost.setComments(new ArrayList<>());
                }

                newComments.add(comment.getId());
                mNewPost.appendComments(newComments);

                mCommentFrame.setVisibility(View.VISIBLE);

                //TODO: reload comments
                DBUtil.forum_update_comment_in_comments(mContext, UPDATE_COMMENT, comment, DBUtil.CASE_ADD);
            }
        }
    };

    View.OnClickListener onLikeIconTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // handle duplicate

            // TODO: update post's like value
            mNewPost = mPost;
            int retCase = 0;
            // if unchecked
            if(mBtn_like.getBackground() == mContext.getResources().getDrawable(R.drawable.ic_like, null)){
                mNewPost.setLikes(mPost.getLikes() + 1);
                retCase = DBUtil.CASE_ADD;
            }else if(mBtn_like.getBackground() == mContext.getResources().getDrawable(R.drawable.ic_like_clicked, null)){
                mNewPost.setLikes(mPost.getLikes() - 1);
                retCase = DBUtil.CASE_REMOVE;
            }

            // TODO: update user's post like value
            DBUtil.user_update_postLikes_value(mContext, UPDATE_USER_LIKE, mPost.getId(), retCase);
        }
    };

    View.OnClickListener onYCardIconTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO: update user's post like value
            DBUtil.user_update_postYCard_value(mContext, UPDATE_USER_YCARD, mPost.getId());
        }
    };

    View.OnClickListener onCommentIconTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // change focus to comment
            mEt_comment.requestFocus();
        }
    };

    // TODO
    public static final String UPDATE_USER_LIKE = "UPDATE_USER_LIKE";
    public static final String UPDATE_USER_YCARD = "UPDATE_USER_YCARD";
    public static final String UPDATE_POST_AFTER_LIKE = "UPDATE_POST_AFTER_LIKE";
    public static final String UPDATE_POST_AFTER_YCARD = "UPDATE_POST_AFTER_YCARD";
    public static final String USER_UPDATE_LIKE_VALUE_COMMENT = "USER_UPDATE_LIKE_VALUE_COMMENT";
    public static final String COMMENT_UPDATE_LIKE_VALUE = "COMMENT_UPDATE_LIKE_VALUE";
    public static final String GET_UPDATED_POST = "GET_UPDATED_POST";
    public static final String USER_UPDATE_YCARD_VALUE_COMMENT = "USER_UPDATE_YCARD_VALUE_COMMENT";
    public static final String COMMENT_UPDATE_YCARD_VALUE = "COMMENT_UPDATE_YCARD_VALUE";

    public static final String GET_CURRENT_USER = "GET_CURRENT_USER";
    public static final String UPDATE_FIELDS = "UPDATE_FIELDS";
    public static final String GET_AUTHOR_INFO = "GET_USER_INFO";
    public static final String DELETE_POST_IN_POSTS = "DELETE_POST_IN_POSTS";
    public static final String DELETE_POST_IN_USERS = "DELETE_POST_IN_USERS";
    public static final String FORUM_GET_COMMENTS = "FORUM_GET_COMMENTS";
    public static final String UPDATE_VALUE = "UPDATE_VALUE";
    public static final String UPDATE_COMMENT = "UPDATE_COMMENT";

    @Override
    public void onLike(Comment _comment, int _caseUser, int _caseComment) { // comment's on like
        // update user's comment like value
        mComment = _comment;
        DBUtil.user_update_commentLikes_value(mContext, USER_UPDATE_LIKE_VALUE_COMMENT, _comment.getId(), _caseUser);

    }

    @Override
    public void onYCard(Comment _comment) { // comment's on ycard
        mComment = _comment;
        DBUtil.user_update_commentYCard_value(mContext, USER_UPDATE_YCARD_VALUE_COMMENT, _comment.getId());
    }

    View.OnClickListener onUndoReplyTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            toggleReplyPopup(false, "");
        }
    };

    @Override
    public void onReply(String _parentId, String _mentionId, String _tagUName) {
        // change tv's value

        mCurrentParentId = _parentId;
        mCurrentMentionId = _mentionId;

        Log.i(TAG, "onReply: mET: " + mEt_comment.toString());

        // TODO: set focus
        mEt_comment.requestFocus();

        toggleReplyPopup(true, _tagUName);

    }

    private void toggleReplyPopup(boolean _isOn, String _uname){
        if(_isOn){
            mTv_reply_desc.setVisibility(View.VISIBLE);
            mTv_reply_desc.setText("Replying to " + _uname);

            mBtn_undo_reply.setVisibility(View.VISIBLE);
        }else{
            mCurrentParentId = "";
            mCurrentMentionId = "";

            mTv_reply_desc.setVisibility(View.GONE);
            mTv_reply_desc.setText(_uname);

            mBtn_undo_reply.setVisibility(View.GONE);
        }

    }

    private void clearCommentField(){
        mEt_comment.setText("");
        mCurrentMentionId = "";
        mCurrentParentId = "";
        toggleReplyPopup(false, "");
    }

    class UserReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean result = intent.getBooleanExtra(DBUtil.EXTRA_BOOLEAN, false);

            if(intent.getAction() == GET_CURRENT_USER){

                if(result){
                    if(intent.hasExtra(DBUtil.EXTRA_SERIALIZABLE) && intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE) != null){
                        mCurrentUser = (User) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);
                        DBUtil.user_get_user_detail_with_email(mContext, GET_AUTHOR_INFO, mPost.getAuthor());
                    }else {

                    }
                }

            }else if(intent.getAction() == GET_AUTHOR_INFO){

                if(result){
                    if(intent.hasExtra(DBUtil.EXTRA_SERIALIZABLE) && intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE) != null){
                        mAuthor = (User) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);
                        DBUtil.forum_get_comments(mContext, FORUM_GET_COMMENTS, mPost.getComments());
                    }else {

                    }

                }else {

                }

            }else if(intent.getAction() == DELETE_POST_IN_POSTS){
                if(result){
                    // TODO: delete from user-posts
                    DBUtil.user_update_posts_value(mContext, DELETE_POST_IN_USERS, null, mPost.getId(), -1); // case delete
                }
            }else if(intent.getAction() == DELETE_POST_IN_USERS){

                if(result){
                    // remove comments as well??
                    finish();
                }else{

                }

            }else if(intent.getAction() == UPDATE_FIELDS){ // after getting post value
                if(result){
                    if(intent.hasExtra(DBUtil.EXTRA_SERIALIZABLE)){
                        mPost = (Post) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);
                        DBUtil.forum_get_comments(mContext, FORUM_GET_COMMENTS, mPost.getComments());
                    }
                }
            }else if(intent.getAction() == FORUM_GET_COMMENTS){
                if(result){
                    if(intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE) != null){
                        mComments = (ArrayList<Comment>) intent.getSerializableExtra(DBUtil.EXTRA_SERIALIZABLE);
                    }else{
//                            mComments = new ArrayList<>();
                    }
                    populateInfo();
                    clearCommentField();
                }

            }else if(intent.getAction() == UPDATE_VALUE){

                mCurrentMentionId = "";
                mCurrentParentId = "";
                DBUtil.forum_get_post_with_id(mContext, UPDATE_FIELDS, mPost.getId());

            }else if(intent.getAction() == UPDATE_COMMENT){

                if(result){
                    DBUtil.forum_update_post(mContext, UPDATE_VALUE, mPost, mNewPost);
                }
            }else if(intent.getAction() == UPDATE_USER_LIKE){

                if(result){
                    DBUtil.forum_update_post(mContext, UPDATE_POST_AFTER_LIKE, mPost, mNewPost);

                    // TODO: reload post detail
                }
            }else if(intent.getAction() == UPDATE_POST_AFTER_LIKE){

                if(result){
                    // TODO: reload post detail
                    // TODO: reload post detail
                    // TODO: reload post detail
                    DBUtil.forum_get_post_with_id(mContext, UPDATE_FIELDS, mPost.getId());
                }
            }else if(intent.getAction() == GET_UPDATED_POST){

                if(result){

                }
            }else if(intent.getAction() == UPDATE_USER_YCARD){

                if(result){
                    // TODO: update post's like value
                    mNewPost = mPost;

                    if(mBtn_ycard.getBackground() == mContext.getResources().getDrawable(R.drawable.ic_ycard, null)){
                        mNewPost.setLikes(mPost.getLikes() + 1);
                    }

                    DBUtil.forum_update_post(mContext, UPDATE_POST_AFTER_YCARD, mPost, mNewPost);
                }
            }else if(intent.getAction() == UPDATE_POST_AFTER_YCARD){

                if(result){
                    // TODO: reload post detail
                    DBUtil.forum_get_post_with_id(mContext, UPDATE_FIELDS, mPost.getId());
                    // TODO: alert dialog?
                }
            }



            else if(intent.getAction() == USER_UPDATE_LIKE_VALUE_COMMENT){

                if(result){
                    // update comment's like value
                    DBUtil.forum_update_comment_in_comments(mContext, COMMENT_UPDATE_LIKE_VALUE, mComment, DBUtil.CASE_UPDATE);

                }
            }else if(intent.getAction() == COMMENT_UPDATE_LIKE_VALUE){

                if(result){
                    DBUtil.forum_get_post_with_id(mContext, UPDATE_FIELDS, mPost.getId());
                }
            }

            else if(intent.getAction() == USER_UPDATE_YCARD_VALUE_COMMENT){

                if(result){
                    DBUtil.forum_update_comment_in_comments(mContext, COMMENT_UPDATE_YCARD_VALUE, mComment, DBUtil.CASE_UPDATE);



                }
            }else if(intent.getAction() == COMMENT_UPDATE_YCARD_VALUE){

                if(result){
                    DBUtil.forum_get_post_with_id(mContext, GET_UPDATED_POST, mPost.getId());
                }
            }



        }
    }
}
