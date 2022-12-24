package com.example.seanlee_thefootballgallery_2201.b_core;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.News;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = "NewsDetailActivity.TAG";
    public static final String EXTRA_NEWS = "EXTRA_NEWS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gray_01, null)));
        News news = (News) getIntent().getSerializableExtra(EXTRA_NEWS);

        // TODO: ScrollView Movement?
        TextView tv_author = findViewById(R.id.news_detail_tv_author);
        TextView tv_date = findViewById(R.id.news_detail_tv_date);
        TextView tv_title = findViewById(R.id.news_detail_tv_title);
        TextView tv_desc = findViewById(R.id.news_detail_tv_desc);

        tv_author.setText(news.getAuthor());
        tv_date.setText(StringUtil.formatNewsDate(news.getDate()));
        tv_title.setText(news.getFullTitle());
        tv_desc.setText(news.getFullDesc());
    }
}
