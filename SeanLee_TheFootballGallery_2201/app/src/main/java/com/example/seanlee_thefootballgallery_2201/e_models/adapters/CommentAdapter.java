package com.example.seanlee_thefootballgallery_2201.e_models.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Comment;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    public static final String TAG = "ForumAdapter.TAG";

    // BASE ID
    private static final long BASE_ID = 0x1011;
    // reference to our owning screen(context)
    private Context mContext;
    // reference to our collection
    private Comment mComment = null;
    private ArrayList<Comment> mCollection;

    public CommentAdapter(Context _context, ArrayList<Comment> _collection) {
        mCallbackListener = (Callback) _context;
        this.mContext = _context;
        this.mCollection = _collection;
    }

    public interface Callback{
        void onLike(Comment _comment, int _caseUser, int _caseComment);
        void onYCard(Comment _comment);
        void onReply(String parentId, String _mentionId, String _tagUname);
    }

    private Callback mCallbackListener;


    @Override
    public int getCount() {
        if(mCollection != null) return mCollection.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mCollection != null && position >= 0 && position < mCollection.size()){
            return mCollection.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return BASE_ID + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        mComment = (Comment) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_comment_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }


        if( mComment != null){

//            vh.tv_filter_league.setFocusable(false);
//            vh.tv_filter_league.setFocusableInTouchMode(false);

            vh.btn_like.setFocusable(false);
            vh.btn_like.setFocusableInTouchMode(false);

            vh.btn_ycard.setFocusable(false);
            vh.btn_ycard.setFocusableInTouchMode(false);

            vh.btn_reply.setFocusable(false);
            vh.btn_reply.setFocusableInTouchMode(false);

            // TODO: loop through user's like list.
            // TODO: if contains, change the icon's color

            if(mComment.getLayer() == 0) vh.child_divider.setVisibility(View.GONE);
            else vh.child_divider.setVisibility(View.VISIBLE);

            vh.iv_emblem.setImageResource(mComment.getAuthorEmblem(mContext));
            vh.tv_author.setText(mComment.getAuthorUser().getUName());
            vh.tv_timestamp.setText(mComment.getTimeGap());
            //vh.tv_desc.setText("@" + mComment.getMentionId() + " " + mComment.getDesc());
            vh.tv_desc.setText(mComment.getDesc());
            vh.tv_like.setText(String.valueOf(mComment.getLike()));
            vh.tv_ycard.setText(String.valueOf(mComment.getYCards()));

            vh.btn_like.setBackgroundColor(mContext.getResources().getColor(R.color.testGray3, null));
            vh.btn_ycard.setBackgroundColor(mContext.getResources().getColor(R.color.testGray3, null));

            if(mComment.getAreClicked()[0]){
                vh.btn_like.setBackground(mContext.getDrawable(R.drawable.ic_like_clicked));
            }

            if(mComment.getAreClicked()[1]){
                vh.btn_ycard.setBackground(mContext.getDrawable(R.drawable.ic_ycard_clicked));
            }

            vh.btn_like.setOnClickListener(new OnLikeClickListener(position));
            vh.btn_ycard.setOnClickListener(new OnYCardClickListener(position));

            vh.btn_reply.setOnClickListener(new PosOnClickListener(position));
        }
        return convertView;
    }

    private void toggleBtnColor(boolean _isClicked){

        // TODO: change both like && ycard
        if(_isClicked){ // already liked

        }
    }

    public class OnLikeClickListener implements View.OnClickListener{

        int mPos;

        public OnLikeClickListener(int _pos) {
            mPos = _pos;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {

            Comment tappedComment = mCollection.get(mPos);

            if(view.getId() == R.drawable.ic_like_clicked){ // is already clicked
                view.setBackground(mContext.getDrawable(R.drawable.ic_like));
                tappedComment.updateLikeValue(-1);
                mCallbackListener.onLike(tappedComment, DBUtil.CASE_REMOVE, DBUtil.CASE_UPDATE);
            }else{ // unclicked
                view.setBackground(mContext.getDrawable(R.drawable.ic_like_clicked));
                tappedComment.updateLikeValue(1);
                mCallbackListener.onLike(tappedComment, DBUtil.CASE_ADD, DBUtil.CASE_UPDATE);
            }

        }
    }

    public class OnYCardClickListener implements View.OnClickListener{

        int mPos;

        public OnYCardClickListener(int _pos) {
            // TODO: get post or comment id
            mPos = _pos;
        }

        @Override
        public void onClick(View view) {

            Comment tappedComment = mCollection.get(mPos);

            // TODO: send post or comment id to user.
            tappedComment.updateYCardValue();
            mCallbackListener.onYCard(tappedComment);
            // TODO: update ui
        }
    }

    public class PosOnClickListener implements View.OnClickListener{

        int mPos;

        public PosOnClickListener(int _pos) {
            this.mPos = _pos;
        }

        @Override
        public void onClick(View view) {

            Comment tappedComment = mCollection.get(mPos);

            if(tappedComment.getParentId().length() <= 0){ // tapped comment's layer is 0

                mCallbackListener.onReply(tappedComment.getId(), tappedComment.getId(), tappedComment.getAuthorUser().getUName()); // parent: tapped comment, mention: null

            }else{ // tapped comment has parent

                mCallbackListener.onReply(tappedComment.getParentId(), tappedComment.getId(), tappedComment.getAuthorUser().getUName()); // parent: top layer, mention: tapped comment
            }
        }
    }

    static class ViewHolder{

        int pos;
        // TODO:
        LinearLayout child_divider;
        ImageView iv_emblem;
        TextView tv_author, tv_timestamp, tv_desc, tv_like, tv_ycard, btn_reply;
        ImageButton btn_like, btn_ycard;

        public ViewHolder(View _layout){

            child_divider = _layout.findViewById(R.id.lv_base_forum_detail_divider_layer_2);

            iv_emblem = _layout.findViewById(R.id.lv_base_forum_detail_iv_emblem);

            tv_author = _layout.findViewById(R.id.lv_base_forum_detail_tv_uname);
            tv_desc = _layout.findViewById(R.id.lv_base_forum_detail_tv_desc);
            tv_timestamp = _layout.findViewById(R.id.lv_base_forum_detail_tv_timestamp);

            tv_like  = _layout.findViewById(R.id.lv_base_forum_detail_tv_like);
            tv_ycard = _layout.findViewById(R.id.lv_base_forum_detail_tv_ycard);

            btn_reply = _layout.findViewById(R.id.lv_base_forum_detail_btn_reply);
            btn_like = _layout.findViewById(R.id.lv_base_forum_detail_btn_like);
            btn_ycard = _layout.findViewById(R.id.lv_base_forum_detail_btn_ycard);
        }
    }
}
