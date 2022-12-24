package com.example.seanlee_thefootballgallery_2201.e_models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;

import java.util.ArrayList;

public class ForumAdapter extends BaseAdapter{

    public static final String TAG = "ForumAdapter.TAG";

    // BASE ID
    private static final long BASE_ID = 0x1011;
    // reference to our owning screen(context)
    private Context mContext;
    // reference to our collection
    private Post mPost = null;
    private ArrayList<Post> mCollection;

    public ForumAdapter(Context _context, ArrayList<Post> _collection) {
        this.mContext = _context;
        this.mCollection = _collection;
    }

    private Callback mCallbackListener;

    public interface Callback{
        void onLikeTap();
        void onYCardTap();
        void onCommentTap();
    }

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
        mPost = (Post) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_forum_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        if( mPost != null){

            vh.tv_filter_league.setFocusable(false);
            vh.tv_filter_league.setFocusableInTouchMode(false);
            vh.tv_filter_category.setFocusable(false);
            vh.tv_filter_category.setFocusableInTouchMode(false);
            vh.btn_like.setFocusable(false);
            vh.btn_like.setFocusableInTouchMode(false);
            vh.btn_ycard.setFocusable(false);
            vh.btn_ycard.setFocusableInTouchMode(false);
            vh.btn_comment.setFocusable(false);
            vh.btn_comment.setFocusableInTouchMode(false);

            vh.btn_like.setOnClickListener(onLikeTap);
            vh.btn_ycard.setOnClickListener(onYCardTap);
            vh.btn_comment.setOnClickListener(onCommentTap);

            if(position % 2 == 0){
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
                vh.btn_like.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
                vh.btn_ycard.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
                vh.btn_comment.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
            }else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
                vh.btn_like.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
                vh.btn_ycard.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
                vh.btn_comment.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
            }

            vh.tv_filter_league.setText(mPost.getLeagueStr());
            vh.tv_filter_league.setBackground(mPost.getDrawableTags(mContext)[1]);
            vh.tv_filter_category.setText(mPost.getCategoryStr());
            vh.tv_filter_category.setBackground(mPost.getDrawableTags(mContext)[0]);

            vh.iv_emblem.setImageResource(mPost.getEmblemForImageView(mContext));
            vh.tv_author.setText(mPost.getAuthorUName());
            vh.tv_date.setText(mPost.getTimeGap());
            vh.tv_title.setText(mPost.getTitle());
            if(mPost.getDesc().length() <= 0) vh.tv_desc.setVisibility(View.GONE);
            vh.tv_desc.setText(mPost.getDescSummary());

            vh.tv_like.setText(String.valueOf(mPost.getLikes()));
            vh.tv_ycard.setText(String.valueOf(mPost.getYCards()));
            String size;
            if(mPost.getComments() == null || mPost.getComments().size() == 0) size = "0";
            else size = String.valueOf(mPost.getComments().size());
            vh.tv_comment.setText(size); // TODO
            //vh.tv_share.setText(mPost.getS);
        }
        return convertView;
    }

    View.OnClickListener onLikeTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //DBUtil.forum_update_
        }
    };

    View.OnClickListener onYCardTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener onCommentTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    static class ViewHolder{

        // TODO:
        TextView tv_filter_league, tv_filter_category, tv_author, tv_date,
                tv_title, tv_desc, tv_like, tv_ycard, tv_comment;//, tv_share;
        ImageButton btn_like, btn_ycard, btn_comment;//, btn_share; TODO
        ImageView iv_emblem;

        public ViewHolder(View _layout){

            tv_filter_league = _layout.findViewById(R.id.lv_base_forum_item_filter_league);
            tv_filter_category = _layout.findViewById(R.id.lv_base_forum_item_filter_category);

            iv_emblem = _layout.findViewById(R.id.lv_base_forum_iv_emblem);
            tv_author = _layout.findViewById(R.id.lv_base_forum_item_author);
            tv_date = _layout.findViewById(R.id.lv_base_forum_item_timestamp);
            tv_title = _layout.findViewById(R.id.lv_base_forum_item_title);
            tv_desc = _layout.findViewById(R.id.lv_base_forum_item_desc);

            tv_like  = _layout.findViewById(R.id.lv_base_forum_tv_like_count);
            tv_ycard = _layout.findViewById(R.id.lv_base_forum_tv_ycard_count);
            tv_comment = _layout.findViewById(R.id.lv_base_forum_tv_comment_count);
            //tv_share = _layout.findViewById(R.id.lv_base_forum_tv_share_count);

            btn_like = _layout.findViewById(R.id.lv_base_forum_btn_like);
            btn_ycard = _layout.findViewById(R.id.lv_base_forum_btn_ycard);
            btn_comment = _layout.findViewById(R.id.lv_base_forum_btn_comment);
            //btn_share = _layout.findViewById(R.id.lv_base_forum_btn_share);
        }
    }
}

