package com.example.seanlee_thefootballgallery_2201.e_models.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Fixture;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MatchesAdapter extends BaseAdapter {

    public static final String TAG = "MatchesAdapter.TAG";

    // BASE ID
    private static final long BASE_ID = 0x1011;
    // reference to our owning screen(context)
    private Context mContext;
    // reference to our collection
    private Fixture mFixture = null;
    private ArrayList<Fixture> mCollection;

    public MatchesAdapter(Context _context, ArrayList<Fixture> _collection) {
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
        mFixture = (Fixture) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_matches_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        if( mFixture != null){

            if(position % 2 == 0){
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray2, null));
            }else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.testGray1, null));
            }

            vh.tv_date.setText(mFixture.getDate());
            vh.tv_venue.setText(mFixture.getVenue());
            vh.tv_status.setText(mFixture.getStatus());
            vh.tv_score.setText(mFixture.getScore());
            vh.tv_homeTeam.setText(mFixture.getHomeTeam());
            vh.tv_awayTeam.setText(mFixture.getAwayTeam());
            Picasso.get().load(mFixture.getHomeLogo()).into(vh.iv_home_logo);
            Picasso.get().load(mFixture.getAwayLogo()).into(vh.iv_away_logo);
//            vh.iv_home_logo.setImageURI(Uri.parse(mFixture.getHomeLogo()));
//            vh.iv_away_logo.setImageURI(Uri.parse(mFixture.getAwayLogo()));
        }
        return convertView;

    }

    View.OnClickListener onPrevTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener onNextTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    static class ViewHolder{

        TextView tv_date;
        TextView tv_venue;
        TextView tv_status;
        TextView tv_score;
        TextView tv_homeTeam;
        TextView tv_awayTeam;
        ImageView iv_home_logo;
        ImageView iv_away_logo;

        public ViewHolder(View _layout){

            tv_date = _layout.findViewById(R.id.lv_base_matches_date);
            tv_venue = _layout.findViewById(R.id.lv_base_matches_venue);
            tv_status = _layout.findViewById(R.id.lv_base_matches_status);
            tv_score = _layout.findViewById(R.id.lv_base_matches_score);
            tv_homeTeam = _layout.findViewById(R.id.lv_base_matches_home_name);
            tv_awayTeam = _layout.findViewById(R.id.lv_base_matches_away_name);

            iv_home_logo = _layout.findViewById(R.id.lv_base_matches_home_logo);
            iv_away_logo = _layout.findViewById(R.id.lv_base_matches_away_logo);
        }
    }
}
