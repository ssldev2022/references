package com.example.seanlee_thefootballgallery_2201.e_models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;

import java.util.ArrayList;

public class ProfilePostAdapter extends BaseAdapter {

    public static final String TAG = "NewsAdapter.TAG";

    // BASE ID
    private static final long BASE_ID = 0x1011;
    // reference to our owning screen(context)
    private Context mContext;
    // reference to our collection
    private Post mPost = null;
    private ArrayList<Post> mCollection;

    public ProfilePostAdapter(Context _context, ArrayList<Post> _collection) {
        this.mContext = _context;
        this.mCollection = _collection;
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
    public long getItemId(int position){
        return BASE_ID + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        mPost = (Post) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_profile_posts_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        if( mPost != null){

            vh.tv_filter.setFocusable(false);
            vh.tv_filter.setFocusableInTouchMode(false);
            vh.tv_category.setFocusable(false);
            vh.tv_category.setFocusableInTouchMode(false);
            vh.btn_like.setFocusable(false);
            vh.btn_like.setFocusableInTouchMode(false);
            vh.btn_ycard.setFocusable(false);
            vh.btn_ycard.setFocusableInTouchMode(false);
            vh.btn_comment.setFocusable(false);
            vh.btn_comment.setFocusableInTouchMode(false);
            vh.btn_more.setFocusable(false);
            vh.btn_more.setFocusableInTouchMode(false);

            if(position % 2 == 0){
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
                vh.btn_like.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
                vh.btn_ycard.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
                vh.btn_comment.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
                vh.btn_more.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
            }else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
                vh.btn_like.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
                vh.btn_ycard.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
                vh.btn_comment.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
                vh.btn_more.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
            }

            vh.tv_filter.setText(mPost.getLeagueStr());
            vh.tv_category.setText(mPost.getCategoryStr());
            vh.tv_date.setText(mPost.getTimeGap());
            vh.tv_title.setText(mPost.getTitle());
            vh.tv_desc.setText(mPost.getDescSummary());
            vh.tv_like.setText(String.valueOf(mPost.getLikes()));
            vh.tv_ycard.setText(String.valueOf(mPost.getYCards()));
            vh.tv_comment.setText(String.valueOf(mPost.getComments().size()));

            if(mPost.getDesc().length() <= 0) vh.tv_desc.setVisibility(View.GONE);
        }
        return convertView;

    }

    View.OnClickListener onLikeTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

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

    View.OnClickListener onMoreTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    static class ViewHolder{

        TextView tv_filter;
        TextView tv_category;
        TextView tv_date;
        TextView tv_title;
        TextView tv_desc;
        TextView tv_like;
        TextView tv_ycard;
        TextView tv_comment;

        ImageButton btn_like;
        ImageButton btn_ycard;
        ImageButton btn_comment;
        ImageButton btn_more;

        public ViewHolder(View _layout){

            tv_filter = _layout.findViewById(R.id.lv_base_profile_post_filter);
            tv_category = _layout.findViewById(R.id.lv_base_profile_post_category);
            tv_date = _layout.findViewById(R.id.lv_base_profile_post_timestamp);
            tv_title = _layout.findViewById(R.id.lv_base_profile_post_title);
            tv_desc = _layout.findViewById(R.id.lv_base_profile_post_desc);

            tv_like = _layout.findViewById(R.id.lv_base_profile_post_tv_like);
            tv_ycard = _layout.findViewById(R.id.lv_base_profile_post_tv_ycard);
            tv_comment = _layout.findViewById(R.id.lv_base_profile_post_tv_comment);

            btn_like = _layout.findViewById(R.id.lv_base_profile_post_btn_like);
            btn_ycard = _layout.findViewById(R.id.lv_base_profile_post_btn_ycard);
            btn_comment = _layout.findViewById(R.id.lv_base_profile_post_btn_comment);
            btn_more = _layout.findViewById(R.id.lv_base_profile_post_btn_more);
        }
    }
}
