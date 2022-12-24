package com.example.seanlee_thefootballgallery_2201.b_core;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.e_models.adapters.CommentAdapter;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Comment;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;

import java.util.ArrayList;

public class ForumDetailCommentFragment extends Fragment implements CommentAdapter.Callback {

    private static final String ARG_COMMENTS = "ARG_COMMENTS";
    private static final String ARG_CURR_USER = "ARG_CURR_USER";

    private ArrayList<Comment> mComments;
    private User mCurrUser;

    public static ForumDetailCommentFragment newInstance(User _currentUser, ArrayList<Comment> _comments) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_COMMENTS, _comments);
        args.putSerializable(ARG_CURR_USER, _currentUser);
        ForumDetailCommentFragment fragment = new ForumDetailCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum_detail_comments, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mComments = (ArrayList<Comment>) getArguments().getSerializable(ARG_COMMENTS);
        mCurrUser = (User) getArguments().getSerializable(ARG_CURR_USER);

        if(mComments != null) {
            organizeComments();
            ListView lv_comments = view.findViewById(R.id.frag_forum_detail_comments_lv);
            lv_comments.setAdapter(new CommentAdapter(getActivity(), mComments));
            lv_comments.setDivider(null);
        }
        // adapter

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void organizeComments(){

        ArrayList<Comment> organized = new ArrayList<>();
        ArrayList<Comment> layer1 = new ArrayList<>();
        ArrayList<Comment> layer2 = new ArrayList<>();

//        for (Comment comment: mComments) {
//            if(comment.getLayer() == 0) layer1.add(comment);
//            else layer2.add(comment);
//        }

        for (Comment comment: mComments) {

            if(mCurrUser.getCommentLikes().contains(comment.getId())){
                comment.setIsLiked(true);
            }else{
                comment.setIsLiked(false);
            }

            if(mCurrUser.getCommentYCards().contains(comment.getId())){
                comment.setIsYCarded(true);
            }else{
                comment.setIsYCarded(false);
            }

            if(comment.getParentId().length() == 0) layer1.add(comment);
            else layer2.add(comment);
        }

        layer1.sort((o1, o2) -> o1.getDateDate().compareTo(o2.getDateDate()));
        layer2.sort((o1, o2) -> o1.getDateDate().compareTo(o2.getDateDate()));

        for(int i=0; i<layer1.size(); i++){
            Comment parent = layer1.get(i);
            organized.add(parent);

            for (int j=0; j< layer2.size(); j++){
                Comment child = layer2.get(j);

                if(child.getParentId().equals(parent.getId())){
                    organized.add(child);
                }
            }
        }

        mComments = organized;
    }


    @Override
    public void onLike(Comment _comment, int _caseUser, int _caseComment) {

    }

    @Override
    public void onYCard(Comment _comment) {

    }

    @Override
    public void onReply(String parentId, String _mentionId, String _tagUName) {

    }
}
