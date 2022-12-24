

package com.example.seanlee_thefootballgallery_2201.b_core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.c_inboxes.InboxActivity;
import com.example.seanlee_thefootballgallery_2201.d_profiles.ProfileActivity;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.ForumAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

import java.util.ArrayList;

public class ForumFragment extends Fragment {

    public static final String TAG = "ForumFragment.TAG";

    private static final String ARG_FORUM_ARR = "ARG_FORUM_ARR";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_FILTER_OPTIONS = "ARG_FILTER_OPTIONS";
    private static final String ARG_SORT_OPTION = "ARG_SORT_OPTION";

    private ArrayList<Post> mCollection;
    private User mUser;
    private String mUname;
    private Post mPost;

    private String[] mFilterOptions;
    private boolean[] mCheckedFilterItems;

    private String[] mSortOptions;
    private int mCheckedSortItem;

    private OnOptionSelectedListener mListener;

    public interface OnOptionSelectedListener{
        void replaceForumFragment(boolean[] _filterOptions, int _sortOption);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnOptionSelectedListener){
            mListener = (OnOptionSelectedListener) context;
        }
    }

    public ForumFragment() { }

    public static ForumFragment newInstance(ArrayList<Post> _forumCollection, User _user, boolean[] _filterOptions, int _sortOption) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_FORUM_ARR, _forumCollection);
        args.putSerializable(ARG_USER, _user);
        args.putSerializable(ARG_FILTER_OPTIONS, _filterOptions);
        args.putSerializable(ARG_SORT_OPTION, _sortOption);
        ForumFragment fragment = new ForumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mFilterOptions = getActivity().getResources().getStringArray(R.array.filter_options_forum);
        mSortOptions = getActivity().getResources().getStringArray(R.array.sort_options_forum);
        mCheckedFilterItems = new boolean[mFilterOptions.length];
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.forum_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.forum_menu_filter){
            displayFilterDialog();
        }else if(item.getItemId() == R.id.forum_menu_sort){
            displaySortDialog();
        }else if(item.getItemId() == R.id.forum_menu_inbox){

            Intent intent = new Intent(getActivity(), InboxActivity.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.forum_menu_profile){

            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = (User) getArguments().getSerializable(ARG_USER);
        mCollection = (ArrayList<Post>) getArguments().getSerializable(ARG_FORUM_ARR);
        mCheckedFilterItems = getArguments().getBooleanArray(ARG_FILTER_OPTIONS);
        mCheckedSortItem = getArguments().getInt(ARG_SORT_OPTION);
        ListView lv = view.findViewById(R.id.frag_forum_lv);
        if(mCollection == null) lv.setAdapter(new ForumAdapter(getActivity(), new ArrayList<>()));
        else lv.setAdapter(new ForumAdapter(getActivity(), mCollection));
        lv.setDivider(null);
        lv.setOnItemClickListener(onItemClickListener);
    }

    private void displayFilterDialog(){

        boolean[] original = mCheckedFilterItems;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter Options");
        builder.setMultiChoiceItems(mFilterOptions, mCheckedFilterItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos, boolean isChecked) {
                mCheckedFilterItems[pos] = isChecked;

                int checkedCounter1 = 0;
                int checkedCounter2 = 0;
                for (int i = 0; i< mCheckedFilterItems.length-4; i++){
                    checkedCounter1++;
                }
                for (int i = 5; i< mCheckedFilterItems.length; i++){
                    checkedCounter2++;
                }

                if(checkedCounter1 == 0 || checkedCounter2 == 0){

                    StringUtil.makeToast(getActivity(), "No post will display without any selection.");

                }
            }
        });
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                int checkedCounter1 = 0;
                int checkedCounter2 = 0;
                for (int i = 0; i< mCheckedFilterItems.length-4; i++){
                    checkedCounter1++;
                }
                for (int i = 5; i< mCheckedFilterItems.length; i++){
                    checkedCounter2++;
                }

                if(checkedCounter1 == 0 || checkedCounter2 == 0){
                    StringUtil.makeToast(getActivity(), "minimum selection required. one each from league/category");

                }else{
                    // interface - reload fragment
                    mListener.replaceForumFragment(mCheckedFilterItems, mCheckedSortItem);
                }
            }
        });
        builder.setNeutralButton("Select All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                for (int i = 0; i< mCheckedFilterItems.length; i++){
                    mCheckedFilterItems[i] = true;
                }
                mListener.replaceForumFragment(mCheckedFilterItems, mCheckedSortItem);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mCheckedFilterItems = original;
            }
        });
        AlertDialog dialog = builder.create();

        Resources res = getActivity().getResources();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button neutral = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                positive.setTextColor(res.getColor(R.color.green_02, null));
                negative.setTextColor(res.getColor(R.color.red, null));
                neutral.setTextColor(res.getColor(R.color.green_02, null));
            }
        });

        dialog.show();

    }

    private void displaySortDialog(){

        int original = mCheckedSortItem;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort Options");
        builder.setSingleChoiceItems(mSortOptions, mCheckedSortItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                mCheckedSortItem = pos;
            }
        });
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                mListener.replaceForumFragment(mCheckedFilterItems, mCheckedSortItem);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                mCheckedSortItem = original;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i(TAG, "onItemClick: forum item clicked");
            Intent intent = new Intent(getActivity(), ForumDetailActivity.class);
            intent.putExtra(ForumDetailActivity.EXTRA_POST, mCollection.get(i));
            intent.putExtra(Intent.EXTRA_USER, mUser);
            startActivity(intent);
        }
    };
}
