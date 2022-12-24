package com.example.seanlee_thefootballgallery_2201.e_models.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seanlee_thefootballgallery_2201.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpinnerAdapter extends BaseAdapter {

    public static final String TAG = "SpinnerAdapter.TAG";

    // BASE ID
    private static final long BASE_ID = 0x1011;

    private Context mContext;
    private ArrayList<String> mItemTitles;
    private ArrayList<Integer> mItemLogoIds;

    public static int mLogoResId, mTitleResId;

    private Map<String, Integer> mMap;


    public SpinnerAdapter(Context _context, ArrayList<String> _itemTitles, ArrayList<Integer> _itemLogoIds) {
    //public SpinnerAdapter(Context _context, ArrayList<String> _itemTitles, ArrayList<Integer> _itemLogoIds, int _logoResId, int _titleResId) {
        this.mContext = _context;

//        this.mTitleResId = _titleResId;
//        this.mLogoResId = _logoResId;

        this.mItemTitles = _itemTitles;
        this.mItemLogoIds = _itemLogoIds;

        this.mMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        if(mItemTitles != null && mItemLogoIds != null && mItemTitles.size() == mItemLogoIds.size()) return mItemTitles.size();
        return 0;
    }

    @Override
    public Object getItem(int pos) {
        if(mItemTitles != null && mItemLogoIds != null && pos >= 0 && pos < mItemTitles.size() && pos < mItemLogoIds.size()) return mItemTitles.get(pos);
        return null;
    }

    @Override
    public long getItemId(int pos) { return BASE_ID + pos; }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        String title = (String) getItem(pos);
        Integer logo = mItemLogoIds.get(pos);

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sp_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        if(title != null && logo != null){
            vh.iv.setImageResource(logo);
            vh.tv.setText(title);
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tv;
        ImageView iv;

        public ViewHolder(View _layout){
            tv = _layout.findViewById(R.id.sp_tv_title); // TODO: dangerous to store res id publicly?
            iv = _layout.findViewById(R.id.sp_iv_logo);
        }
    }
}
