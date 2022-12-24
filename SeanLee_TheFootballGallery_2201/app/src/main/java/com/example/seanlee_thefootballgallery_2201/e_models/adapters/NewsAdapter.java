package com.example.seanlee_thefootballgallery_2201.e_models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.News;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

    public static final String TAG = "NewsAdapter.TAG";

    // BASE ID
    private static final long BASE_ID = 0x1011;
    // reference to our owning screen(context)
    private Context mContext;
    // reference to our collection
    private News mNews = null;
    private ArrayList<News> mCollection;

    public NewsAdapter(Context _context, ArrayList<News> _collection) {
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
        mNews = (News) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_news_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        if( mNews != null){

//            if(position % 2 == 0){
//                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_01, null));
//            }else {
//                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_03, null));
//            }

            if(position % 2 == 0){
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
            }else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
            }

            vh.tv_author.setText(mNews.getAuthor());
            vh.tv_date.setText(StringUtil.formatNewsDate(mNews.getDate()));
            vh.tv_title.setText(mNews.getFullTitle());
            vh.tv_desc.setText(mNews.getDescSummary());
        }
        return convertView;

    }

    static class ViewHolder{

        TextView tv_author;
        TextView tv_date;
        TextView tv_title;
        TextView tv_desc;

        public ViewHolder(View _layout){
            tv_author = _layout.findViewById(R.id.lv_base_news_item_author);
            tv_date = _layout.findViewById(R.id.lv_base_news_item_date);
            tv_title = _layout.findViewById(R.id.lv_base_news_item_title);
            tv_desc = _layout.findViewById(R.id.lv_base_news_item_desc);
        }
    }
}
