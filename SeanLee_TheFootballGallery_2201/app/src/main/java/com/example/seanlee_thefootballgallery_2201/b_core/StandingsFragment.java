package com.example.seanlee_thefootballgallery_2201.b_core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.c_inboxes.InboxActivity;
import com.example.seanlee_thefootballgallery_2201.d_profiles.ProfileActivity;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.ForumAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.StandingsAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Team;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;

import java.util.ArrayList;

public class StandingsFragment extends Fragment {

    public static final String TAG = "StandingsFragment.TAG";
    private static final String ARG_COLLECTION = "ARG_COLLECTION";
    private static final String ARG_FILTER_OPTION = "ARG_FILTER_OPTIONS";

    private ArrayList<Team> mCollection;
    private String[] mFilterOptions;
    private int mCheckedFilterItem;

    public interface OnOptionSelectedListener{
        void replaceStandingsFragment(int _filterOption);
    }

    private OnOptionSelectedListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnOptionSelectedListener){
            mListener = (OnOptionSelectedListener) context;
        }
    }

    public StandingsFragment() { }

    public static StandingsFragment newInstance(ArrayList<Team> _collection, int _filterOption) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_COLLECTION, _collection);
        args.putInt(ARG_FILTER_OPTION, _filterOption);
        StandingsFragment fragment = new StandingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mFilterOptions = getActivity().getResources().getStringArray(R.array.filter_options_default);
        return inflater.inflate(R.layout.fragment_standings, container, false);
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

        int original = mCheckedFilterItem;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort Options");
        builder.setSingleChoiceItems(mFilterOptions, mCheckedFilterItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                mCheckedFilterItem = pos;
            }
        });
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                mListener.replaceStandingsFragment(mCheckedFilterItem);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                mCheckedFilterItem = original;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCollection = (ArrayList<Team>) getArguments().getSerializable(ARG_COLLECTION);
        mCheckedFilterItem = getArguments().getInt(ARG_FILTER_OPTION);
        ListView lv = view.findViewById(R.id.frag_standings_lv);
        lv.setAdapter(new StandingsAdapter(getActivity(), mCollection));
        lv.setDivider(null);
    }
}
