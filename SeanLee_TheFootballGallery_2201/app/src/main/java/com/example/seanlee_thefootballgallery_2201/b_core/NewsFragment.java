package com.example.seanlee_thefootballgallery_2201.b_core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.c_inboxes.InboxActivity;
import com.example.seanlee_thefootballgallery_2201.d_profiles.ProfileActivity;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.NewsAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.News;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    public static final String TAG = "NewsFragment.TAG";

    public NewsFragment() { }

    private static final String ARG_NEWS_ARR = "ARG_NEWS_ARR";
    private static final String ARG_FILTER_OPTION = "ARG_FILTER_OPTION";
    private ArrayList<News> mNewsArr;
    private News mNews;

    private String[] mFilterOptions;
    private boolean[] mCheckedFilterItems;

    public interface OnOptionSelectedListener{
        void replaceNewsFragment(boolean[] _filterOptions);
    }

    private OnOptionSelectedListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnOptionSelectedListener){
            mListener = (OnOptionSelectedListener) context;
        }
    }

    public static NewsFragment newInstance(ArrayList<News> _newsArr, boolean[] _filterOptions) {
        
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEWS_ARR, _newsArr);
        args.putBooleanArray(ARG_FILTER_OPTION, _filterOptions);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mFilterOptions = getActivity().getResources().getStringArray(R.array.filter_options_default);
        return inflater.inflate(R.layout.fragment_core, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.default_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.default_menu_filter){
            displayFilterDialog();
        }else if(item.getItemId() == R.id.default_menu_inbox){
            Intent intent = new Intent(getActivity(), InboxActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.default_menu_profile){
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayFilterDialog(){

        boolean[] original = mCheckedFilterItems;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort Options");
        builder.setMultiChoiceItems(mFilterOptions, mCheckedFilterItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos, boolean isChecked) {
                mCheckedFilterItems[pos] = isChecked;
            }
        });
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                int checkedCounter = 0;
                for (int i = 0; i< mCheckedFilterItems.length; i++){
                    checkedCounter++;
                }
                if (checkedCounter == 0){
                    StringUtil.makeToast(getActivity(), "minimum selectino required.");
                }else {
                    mListener.replaceNewsFragment(mCheckedFilterItems);
                }

            }
        });
        builder.setNeutralButton("Select All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                for (int i = 0; i< mCheckedFilterItems.length; i++){
                    mCheckedFilterItems[i] = true;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                mCheckedFilterItems = original;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNewsArr = (ArrayList<News>) getArguments().getSerializable(ARG_NEWS_ARR);
        mCheckedFilterItems = getArguments().getBooleanArray(ARG_FILTER_OPTION);
        ListView lv = view.findViewById(R.id.frag_core_lv);
        lv.setAdapter(new NewsAdapter(getActivity(), mNewsArr));
        lv.setDivider(null);
        lv.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // TODO: start News Detail Activity
            Log.i(TAG, "onItemClick: news item clicked");
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra(NewsDetailActivity.EXTRA_NEWS, mNewsArr.get(i));
            startActivity(intent);
        }
    };
}