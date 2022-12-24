package com.example.seanlee_thefootballgallery_2201.e_models.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Team;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StandingsAdapter extends BaseAdapter{


    public static final String TAG = "MatchesAdapter.TAG";

    // BASE ID
    private static final long BASE_ID = 0x1011;
    // reference to our owning screen(context)
    private Context mContext;
    // reference to our collection
    private Team mTeam = null;
    private ArrayList<Team> mCollection;

    public StandingsAdapter(Context _context, ArrayList<Team> _collection) {
        this.mContext = _context;
        this.mCollection = _collection;
    }

    @Override
    public int getCount() {
        if (mCollection != null) return mCollection.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mCollection != null && position >= 0 && position < mCollection.size()) {
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
        mTeam = (Team) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_standings_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (mTeam != null) {

            if (position % 2 == 0) {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
            } else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
            }

            Picasso.get().load(mTeam.getLogoUrl()).into(vh.iv_logo);
            //vh.iv_logo.setImageURI(Uri.parse(mTeam.getLogoUrl()));
            vh.tv_rank.setText(String.valueOf(mTeam.getRank()));
            vh.tv_teamName.setText(mTeam.getTeamName());
            vh.tv_mp.setText(String.valueOf(mTeam.getMP()));
            vh.tv_pts.setText(String.valueOf(mTeam.getPts()));
            vh.tv_w.setText(String.valueOf(mTeam.getW()));
            vh.tv_d.setText(String.valueOf(mTeam.getD()));
            vh.tv_l.setText(String.valueOf(mTeam.getL()));
            vh.tv_gd.setText(String.valueOf(mTeam.getGD()));
        }
        return convertView;

    }

    static class ViewHolder {

        TextView tv_rank;
        ImageView iv_logo;
        TextView tv_teamName;
        TextView tv_mp;
        TextView tv_pts;
        TextView tv_w;
        TextView tv_d;
        TextView tv_l;
        TextView tv_gd;

        public ViewHolder(View _layout) {

            tv_rank = _layout.findViewById(R.id.lv_base_standings_rank);
            iv_logo = _layout.findViewById(R.id.lv_base_standings_logo);
            tv_teamName = _layout.findViewById(R.id.lv_base_standings_teamTitle);
            tv_mp = _layout.findViewById(R.id.lv_base_standings_MP);
            tv_pts = _layout.findViewById(R.id.lv_base_standings_pts);
            tv_w = _layout.findViewById(R.id.lv_base_standings_W);
            tv_d = _layout.findViewById(R.id.lv_base_standings_D);
            tv_l = _layout.findViewById(R.id.lv_base_standings_L);
            tv_gd = _layout.findViewById(R.id.lv_base_standings_GD);
        }
    }
}