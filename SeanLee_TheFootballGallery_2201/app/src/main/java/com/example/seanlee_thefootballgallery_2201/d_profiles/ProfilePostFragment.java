package com.example.seanlee_thefootballgallery_2201.d_profiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.b_core.ForumDetailActivity;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.ProfilePostAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;

import java.util.ArrayList;

public class ProfilePostFragment extends Fragment {

    public static final String TAG = "ProfilePostFragment.TAG";
    public static final String ARG_POSTS = "ARG_POSTS";
    public static final String ARG_USER = "ARG_USER";

    private ListView mLv;
    private ArrayList<Post> mCollection;
    private User mUser;

    public ProfilePostFragment() { }

    public static ProfilePostFragment newInstance(User _user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, _user);
        ProfilePostFragment fragment = new ProfilePostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(getArguments().containsKey(ARG_USER)){
            mUser = (User) getArguments().getSerializable(ARG_USER);
            mCollection = mUser.getPosts();
        }
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLv = view.findViewById(R.id.frag_profile_lv);

        mLv.setAdapter(new ProfilePostAdapter(getActivity(), mCollection));
        mLv.setDivider(null);
        mLv.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // maybe sync with adapter?
            Intent intent = new Intent(getActivity(), ForumDetailActivity.class);
            intent.putExtra(ForumDetailActivity.EXTRA_POST, mCollection.get(i));
            intent.putExtra(Intent.EXTRA_USER, mUser);
            startActivity(intent);
        }
    };
}
