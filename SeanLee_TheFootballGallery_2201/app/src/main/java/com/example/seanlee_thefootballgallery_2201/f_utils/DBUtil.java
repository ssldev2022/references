package com.example.seanlee_thefootballgallery_2201.f_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.seanlee_thefootballgallery_2201.e_models.objects.Comment;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.Post;
import com.example.seanlee_thefootballgallery_2201.e_models.objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class DBUtil {

    private static final String TAG = "DBUtil.TAG";

    // TODO: Global variables

    private static FirebaseAuth mAuth;
    private static FirebaseUser mFBUser;
    private static FirebaseFirestore mDb;

    // TODO: Firestore AUTH

    public static void auth_login(Context _context, String _action, String _email, String _password){

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(_email, _password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.i(TAG, "auth_login success");
                Log.i(TAG, "step 2");
                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getLocalizedMessage());
                Log.i(TAG, "onFailure: step 2");
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static final int CASE_LOGIN = 0;
    public static final int CASE_SIGNUP = 1;

    public static void auth_verify_email(Context _context, String _action, String _email, int _case){

        mAuth = FirebaseAuth.getInstance();
        mAuth.fetchSignInMethodsForEmail(_email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                Log.i(TAG, "task.result.signinmethods - " + task.getResult().getSignInMethods().size());
                boolean retval;
                if(task.getResult().getSignInMethods().size() > 0){
                    if(_case == CASE_LOGIN) retval = true;
                    else retval = false;

                }else{ // no result found
                    if(_case == CASE_LOGIN) retval = false;
                    else retval = true;
                }
                Log.i(TAG, "step 1: " + retval);
                sendBroadcast(_context, _action, retval, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getLocalizedMessage());
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static void auth_signup_create_account(Context _context, String _action, String _email, String _password){
        mAuth = FirebaseAuth.getInstance();

        Log.i(TAG, "auth_signup_create_acc: mAUth" + mAuth.getUid());

        mAuth.createUserWithEmailAndPassword(_email, _password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getLocalizedMessage());
                sendBroadcast(_context, _action, false, null);
            }
        });
    }


    // TODO: Firestore DB

    private static final String TABLE_USERS = "users";
    private static final String TABLE_POSTS = "posts";

    // TODO: USER

    private static final String FIELD_USERNAME = "username";
    //private static final String FIELD_UID = "FIELD_UID";
    private static final String FIELD_PHOTO = "photo";
    private static final String FIELD_POSTS = "posts";
    private static final String FIELD_EMBLEM_LEAGUE = "emblem_league";
    private static final String FIELD_EMBLEM_TEAM = "emblem_team";


    public static void user_create_in_users(Context _context, String _action, User _user){
        mDb = FirebaseFirestore.getInstance();

        Map<String, Object> userForUsers = new HashMap<>();
        userForUsers.put(FIELD_USERNAME, _user.getUName());
        userForUsers.put(FIELD_PHOTO, _user.getPhotoString());
        userForUsers.put(FIELD_EMBLEM_LEAGUE, _user.getEmblemLeagueInt());
        userForUsers.put(FIELD_EMBLEM_TEAM, _user.getEmblemTeamInt());
        userForUsers.put(FIELD_POSTS, new ArrayList<String>());

        // TODO:

        userForUsers.put("postLikes", new ArrayList<String>());
        userForUsers.put("postYCards", new ArrayList<String>());

        userForUsers.put("commentLikes", new ArrayList<String>());
        userForUsers.put("commentYCards", new ArrayList<String>());

        mDb.collection(TABLE_USERS).document(_user.getId()).set(userForUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getLocalizedMessage());
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static void user_verify_username(Context _context, String _action, String _username){
        mDb = FirebaseFirestore.getInstance();

        mDb.collection(TABLE_USERS).whereEqualTo(FIELD_USERNAME, _username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if(task.getResult().getDocuments().size() <= 0){
                        // return true
                        sendBroadcast(_context, _action, true, null);
                    }else {
                        sendBroadcast(_context, _action, false, null);
                    }
                }else {
                    sendBroadcast(_context, _action, false, null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getLocalizedMessage());
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static void user_update_profile(Context _context, String _action, User _oldUser, User _newUser){
        mDb = FirebaseFirestore.getInstance();

        Map<String, Object> oldUser = new HashMap<>();
        oldUser.put("emblem_league", _oldUser.getEmblemLeagueInt());
        oldUser.put("emblem_team", _oldUser.getEmblemTeamInt());
        oldUser.put("photo", _oldUser.getPhotoString());
        oldUser.put("posts", _oldUser.getPostIds());
        oldUser.put("username", _oldUser.getUName());

        // TODO:
        oldUser.put("postLikes", _oldUser.getPostLikes());
        oldUser.put("postYCards", _oldUser.getPostYCards());
        oldUser.put("commentLikes", _oldUser.getCommentLikes());
        oldUser.put("commentYCards", _oldUser.getCommentYCards());

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("emblem_league", _newUser.getEmblemLeagueInt());
        newUser.put("emblem_team", _newUser.getEmblemTeamInt());
        newUser.put("photo", _newUser.getPhotoString());
        newUser.put("posts", _newUser.getPostIds());
        newUser.put("username", _newUser.getUName());

        newUser.put("postLikes", _newUser.getPostLikes());
        newUser.put("postYCards", _newUser.getPostYCards());
        newUser.put("commentLikes", _newUser.getCommentLikes());
        newUser.put("commentYCards", _newUser.getCommentYCards());

        // TODO: update or remove-add?

        mDb.collection("users").document(_oldUser.getId()).update(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static final int CASE_REMOVE = -1;
    public static final int CASE_UPDATE = 0;
    public static final int CASE_ADD = 1;

    public static void user_update_posts_value(Context _context, String _action, ArrayList<String> _oldPostList, String _newValue, int _case){

        mDb = FirebaseFirestore.getInstance();

        String docId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = mDb.collection("users").document(docId);

        if (_case == CASE_ADD){
            docRef.update("posts", FieldValue.arrayUnion(_newValue)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendBroadcast(_context, _action, false, null);
                }
            });
        }else if(_case == CASE_REMOVE){
            docRef.update("posts", FieldValue.arrayRemove(_newValue)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendBroadcast(_context, _action, false, null);
                }
            });
        }
    }

    public static void user_update_postLikes_value(Context _context, String _action, String _newValue, int _case){
        mDb = FirebaseFirestore.getInstance();

        String docId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = mDb.collection("users").document(docId);

        if (_case == CASE_ADD){
            docRef.update("postLikes", FieldValue.arrayUnion(_newValue)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendBroadcast(_context, _action, false, null);
                }
            });
        }else if(_case == CASE_REMOVE){
            docRef.update("postLikes", FieldValue.arrayRemove(_newValue)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendBroadcast(_context, _action, false, null);
                }
            });
        }
    }


    public static void user_update_commentLikes_value(Context _context, String _action, String _newValue, int _case){
        mDb = FirebaseFirestore.getInstance();

        String docId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = mDb.collection("users").document(docId);

        if (_case == CASE_ADD){
            docRef.update("commentLikes", FieldValue.arrayUnion(_newValue)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendBroadcast(_context, _action, false, null);
                }
            });
        }else if(_case == CASE_REMOVE){
            docRef.update("commentLikes", FieldValue.arrayRemove(_newValue)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendBroadcast(_context, _action, false, null);
                }
            });
        }
    }

    public static void user_update_postYCard_value(Context _context, String _action, String _newValue){
        mDb = FirebaseFirestore.getInstance();

        String docId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = mDb.collection("users").document(docId);


        docRef.update("postYCards", FieldValue.arrayUnion(_newValue)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });

    }

    public static void user_update_commentYCard_value(Context _context, String _action, String _newValue){
        mDb = FirebaseFirestore.getInstance();

        String docId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docRef = mDb.collection("users").document(docId);

        docRef.update("commentYCards", FieldValue.arrayUnion(_newValue)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }


    public static void user_get_user_detail_with_email(Context _context, String _action, String _userEmail){
        mDb = FirebaseFirestore.getInstance();

        mDb.collection("users").document(_userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int emblem_league = Integer.valueOf(documentSnapshot.get("emblem_league").toString());
                int emblem_team = Integer.valueOf(documentSnapshot.get("emblem_team").toString());
                String photo = documentSnapshot.getString("photo");
                String uname = documentSnapshot.getString("username");
                ArrayList<String> postIds = (ArrayList<String>) documentSnapshot.get("posts");
                // TODO
                ArrayList<String> postLikes = (ArrayList<String>) documentSnapshot.get("postLikes");
                ArrayList<String> postYCards = (ArrayList<String>) documentSnapshot.get("postYCards");
                ArrayList<String> commentLikes = (ArrayList<String>) documentSnapshot.get("commentLikes");
                ArrayList<String> commentYCards = (ArrayList<String>) documentSnapshot.get("commentYCards");

                mDb.collection("posts").whereEqualTo("userId", _userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<Post> posts = new ArrayList<>();

                        for (DocumentSnapshot document: queryDocumentSnapshots) {

                            String id = document.getId();
                            String desc = document.getString("description");
                            String title = document.getString("title");
                            ArrayList<String> commIds = (ArrayList<String>) document.get("comments");
                            int cate = Integer.valueOf(document.get("category").toString());
                            int filt = Integer.valueOf(document.get("filter").toString());
                            int like = Integer.valueOf(document.get("like").toString());
                            int ycard = Integer.valueOf(document.get("yellowcard").toString());

                            String uid = document.getString("userId");

                            Post post = new Post(id, uid,title, desc, cate, filt, like, ycard, commIds);
                            // TODO - removed
//                            post.setAuthorEmblemLeagueInt(Integer.valueOf(document.get("user_emblem_league").toString()));
//                            post.setAuthorEmblemTeamInt(Integer.valueOf(document.get("user_emblem_team").toString()));
//                            post.setAuthorUname(document.getString("user_username"));

                            posts.add(post);
                        }
                        User user = new User(_userEmail, uname, posts, postLikes, postYCards, commentLikes, commentYCards, photo, emblem_league, emblem_team);

                        mDb.collection("posts").whereEqualTo("userId", _userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            }
                        });

                        sendBroadcast(_context, _action, true, user);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        sendBroadcast(_context, _action, false, null);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static void user_get_user_info(Context _context, String _action){
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFBUser = mAuth.getCurrentUser();
        String userEmail = mFBUser.getEmail();

        mDb.collection("users").document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                int emblem_league = Integer.valueOf(documentSnapshot.get("emblem_league").toString());
                int emblem_team = Integer.valueOf(documentSnapshot.get("emblem_team").toString());
                String photo = documentSnapshot.getString("photo");
                String uname = documentSnapshot.getString("username");
                ArrayList<String> postIds = (ArrayList<String>) documentSnapshot.get("posts");

                ArrayList<String> postLikes = (ArrayList<String>) documentSnapshot.get("postLikes");
                ArrayList<String> postYCards = (ArrayList<String>) documentSnapshot.get("postYCards");
                ArrayList<String> commentLikes = (ArrayList<String>) documentSnapshot.get("commentLikes");
                ArrayList<String> commentYCards = (ArrayList<String>) documentSnapshot.get("commentYCards");

                mDb.collection("posts").whereEqualTo("userId", userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<Post> posts = new ArrayList<>();

                        for (DocumentSnapshot document: queryDocumentSnapshots) {

                            String id = document.getId();

                            String desc = document.getString("description");
                            String title = document.getString("title");
                            ArrayList<String> commIds = (ArrayList<String>) document.get("comments");
                            int cate = Integer.valueOf(document.get("category").toString());
                            int filt = Integer.valueOf(document.get("filter").toString());
                            int like = Integer.valueOf(document.get("like").toString());
                            int ycard = Integer.valueOf(document.get("yellowcard").toString());

                            String uid = document.getString("userId");

                            Post post = new Post(id, uid,title, desc, cate, filt, like, ycard, commIds);
                            // TODO - removed
//                            post.setAuthorEmblemLeagueInt(Integer.valueOf(document.get("emblem_league").toString()));
//                            post.setAuthorEmblemTeamInt(Integer.valueOf(document.get("emblem_team").toString()));
//                            post.setAuthorUname(document.getString("user_username"));
                            posts.add(post);
                        }
                        User user = new User(userEmail, uname, posts, postLikes, postYCards, commentLikes, commentYCards, photo, emblem_league, emblem_team);

                        sendBroadcast(_context, _action, true, user);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        sendBroadcast(_context, _action, false, null);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    // TODO: FORUM

    private static final String FIELD_AUTHOR = "FIELD_EMBLEM";

    public static void forum_check_update(Context _context, String _action, int _originalCount){
        mDb = FirebaseFirestore.getInstance();
        mDb.collection("posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(_originalCount != queryDocumentSnapshots.size()){
                    Log.i(TAG, "doc size: " + queryDocumentSnapshots.size());
                    sendBroadcast(_context, _action, true, null);
                }else{
                    sendBroadcast(_context, _action, false, null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static void forum_get_posts(Context _context, String _action){

        mDb = FirebaseFirestore.getInstance();

        ArrayList<String> postIds = new ArrayList<>();
        ArrayList<String> userIds = new ArrayList<>();

        mDb.collection("posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(DocumentSnapshot document: queryDocumentSnapshots){

                    postIds.add(document.getId());
                    userIds.add(document.getString("userId"));

                }

                if(postIds.size() > 0 && userIds.size() > 0){
                    forum_get_post_step_two(_context, _action, postIds, userIds);
                }else{
                    sendBroadcast(_context, _action, true, null);
                }
            }
        });
    }

    public static void forum_get_post_step_two(Context _context, String _action, ArrayList<String> _postIds, ArrayList<String> _uIds){

        mDb = FirebaseFirestore.getInstance();

        ArrayList<Task> tasks1 = new ArrayList<>();
        ArrayList<Task> tasks2 = new ArrayList<>();
        ArrayList<Post> combined = new ArrayList<>();

        for (int i=0; i< _postIds.size(); i++){
            //tasks1.add(mDb.collection("comments").whereEqualTo("userId", _retVal.get(i).getAuthorUser().getId()).get());
            tasks1.add(mDb.collection("posts").document(_postIds.get(i)).get());
        }

        for (int i=0; i< _uIds.size(); i++){
            tasks2.add(mDb.collection("users").document(_uIds.get(i)).get());
        }

        Tasks.whenAllSuccess(tasks1).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {

                Tasks.whenAllSuccess(tasks2).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {

                        for (int i=0; i< tasks1.size(); i++){

                            DocumentSnapshot doc1 = (DocumentSnapshot) tasks1.get(i).getResult();
                            DocumentSnapshot doc2 = (DocumentSnapshot) tasks2.get(i).getResult();

                            String docId = doc1.getId();
                            int category = Integer.valueOf(doc1.get("category").toString()) ;
                            ArrayList<String> comments = (ArrayList<String>) doc1.get("comments");
                            String desc = doc1.getString("description");
                            int filter = Integer.valueOf(doc1.get("filter").toString());
                            int likes = Integer.valueOf(doc1.get("like").toString());
                            int ycard = Integer.valueOf(doc1.get("yellowcard").toString());
                            String title = doc1.getString("title");
                            String uId = doc1.getString("userId");

                            Post toAdd = new Post(title, desc, category, filter);
                            toAdd.setLikes(likes);
                            toAdd.setYCard(ycard);
                            toAdd.setId(docId);
                            toAdd.setComments(comments);
                            toAdd.setCategory(category);
                            toAdd.setLeague(filter);

                            String uname = doc2.getString("username");
                            int leagueInt = Integer.valueOf(doc2.get("emblem_league").toString());
                            int teamInt = Integer.valueOf(doc2.get("emblem_team").toString());

                            toAdd.setAuthor(uId);
                            toAdd.setAuthorUname(uname);
                            toAdd.setAuthorEmblemLeagueInt(leagueInt);
                            toAdd.setAuthorEmblemTeamInt(teamInt);

                            combined.add(toAdd);
                        }
                        sendBroadcast(_context, _action, true, combined);
                    }
                });
            }
        });
    }

    public static void forum_create_post(Context _context, String _action, Post _newPost){

        mDb = FirebaseFirestore.getInstance();

        Map<String, Object> newPost = new HashMap<>();
        newPost.put("comments", _newPost.getComments());
        newPost.put("description", _newPost.getDesc());
        newPost.put("like", _newPost.getLikes());
        newPost.put("title", _newPost.getTitle());
        newPost.put("yellowcard", _newPost.getYCards());
        newPost.put("category", _newPost.getCategoryInt());
        newPost.put("filter", _newPost.getLeagueInt());
        newPost.put("userId", _newPost.getAuthor());


        mDb.collection(TABLE_POSTS).document(_newPost.getId()).set(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null); // TODO: send broadcast
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static void forum_update_post(Context _context, String _action, Post _oldPost, Post _newPost){

        mDb = FirebaseFirestore.getInstance();

        // remove
        Map<String, Object> oldPost = new HashMap<>();
        oldPost.put("category", _oldPost.getCategoryInt());
        oldPost.put("comments", _oldPost.getComments());
        oldPost.put("description", _oldPost.getDesc());
        oldPost.put("filter", _oldPost.getLeagueInt());
        oldPost.put("like", _oldPost.getLikes());
        oldPost.put("title", _oldPost.getTitle());
        oldPost.put("userId", _oldPost.getAuthor());
        oldPost.put("yellowcard", _oldPost.getYCards());
        // TODO
//        oldPost.put("user_emblem_league", _oldPost.getAuthorEmblemLeagueInt());
//        oldPost.put("user_emblem_team", _oldPost.getAuthorEmblemTeamInt());
//        oldPost.put("user_username", _oldPost.getAuthorUName());

        // add
        Map<String, Object> newPost = new HashMap<>();
        newPost.put("category", _newPost.getCategoryInt());
        newPost.put("comments", _newPost.getComments());
        newPost.put("description", _newPost.getDesc());
        newPost.put("filter", _newPost.getLeagueInt());
        newPost.put("like", _newPost.getLikes());
        newPost.put("title", _newPost.getTitle());
        newPost.put("userId", _newPost.getAuthor());
        newPost.put("yellowcard", _newPost.getYCards());
        // TODO
//        newPost.put("user_emblem_league", _newPost.getAuthorEmblemLeagueInt());
//        newPost.put("user_emblem_team", _newPost.getAuthorEmblemTeamInt());
//        newPost.put("user_username", _newPost.getAuthorUName());

        // keep docId
        DocumentReference docRef = mDb.collection("posts").document(_oldPost.getId());

        docRef.update(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static void forum_get_post_with_id(Context _context, String _action, String _postId){
        mDb = FirebaseFirestore.getInstance();

        mDb.collection("posts").document(_postId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String docId = documentSnapshot.getId();

                int category = Integer.valueOf(documentSnapshot.get("category").toString()) ;
                ArrayList<String> comments = (ArrayList<String>) documentSnapshot.get("comments");
                String desc = documentSnapshot.getString("description");
                int filter = Integer.valueOf(documentSnapshot.get("filter").toString());
                int likes = Integer.valueOf(documentSnapshot.get("like").toString());
                int ycard = Integer.valueOf(documentSnapshot.get("yellowcard").toString());
                String title = documentSnapshot.getString("title");
                String uId = documentSnapshot.getString("userId");


                Post newPost = new Post(title, desc, category, filter);
                newPost.setLikes(likes);
                newPost.setYCard(ycard);
                newPost.setId(docId);
                newPost.setComments(comments);
                newPost.setCategory(category);
                newPost.setLeague(filter);
                newPost.setAuthor(uId);
                // TODO
                mDb.collection("users").document(uId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        newPost.setAuthorUname(documentSnapshot.getString("username"));
                        newPost.setAuthorEmblemLeagueInt(Integer.valueOf(documentSnapshot.get("emblem_league").toString()));
                        newPost.setAuthorEmblemTeamInt(Integer.valueOf(documentSnapshot.get("emblem_team").toString()));
                        sendBroadcast(_context, _action, true, newPost);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                //sendBroadcast(_context, _action, true, newPost);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }


    public static void forum_delete_post(Context _context, String _action, String _postId){

        mDb = FirebaseFirestore.getInstance();

        mDb.collection("posts").document(_postId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    public static void forum_create_comment(Context _context, String _action, Comment _comment){

        mDb = FirebaseFirestore.getInstance();

        Map<String, Object> newComment = new HashMap<>();
        newComment.put("description", _comment.getDesc());
        newComment.put("postId", _comment.getPostId());
        newComment.put("userId", _comment.getAuthorUser().getId());
        newComment.put("like", _comment.getLike());
        newComment.put("yellowcard", _comment.getYCards());
        newComment.put("parentId", _comment.getParentId());
        newComment.put("mentionId", _comment.getMentionId());

        mDb.collection("comments").document(_comment.getId()).set(newComment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });

    }

    public static void forum_get_comments(Context _context, String _action, ArrayList<String> _commentIds){

        mDb = FirebaseFirestore.getInstance();

        ArrayList<String> commentIds = new ArrayList<>();
        ArrayList<String> authorIds = new ArrayList<>();

        mDb.collection("comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (DocumentSnapshot document: queryDocumentSnapshots) {

                        if(_commentIds.contains(document.getId())){

                            commentIds.add(document.getId());
                            authorIds.add(document.getString("userId"));
                        }
                    }
                if(commentIds.size() > 0 && authorIds.size() > 0){

                    forum_get_comments_step_two(_context, _action, commentIds, authorIds);

                }else {
                    sendBroadcast(_context, _action, true, null);
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendBroadcast(_context, _action, false, null);
            }
        });
    }

    // TODO: get actual data using the ids.
    public static void forum_get_comments_step_two(Context _context, String _action, ArrayList<String> _commentIds, ArrayList<String> _authorIds) {

        ArrayList<Task> tasks1 = new ArrayList<>();
        ArrayList<Task> tasks2 = new ArrayList<>();
        ArrayList<Comment> combined = new ArrayList<>();

        // add to queries
        for (int i=0; i< _commentIds.size(); i++){
            tasks1.add(mDb.collection("comments").document(_commentIds.get(i)).get());
        }

        for (int i=0; i< _authorIds.size(); i++){
            tasks2.add(mDb.collection("users").document(_authorIds.get(i)).get());
        }

        ArrayList<String> mentionIds = new ArrayList<>();
        ArrayList<User> authors = new ArrayList<>();
        ArrayList<DocumentSnapshot> snapshots1 = new ArrayList<>();

        Tasks.whenAllSuccess(tasks1).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                Tasks.whenAllSuccess(tasks2).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {

                        for(int i=0; i< tasks1.size(); i++){

                            // check type? instanceOf
                            DocumentSnapshot doc1 = (DocumentSnapshot) tasks1.get(i).getResult();
                            DocumentSnapshot doc2 = (DocumentSnapshot) tasks2.get(i).getResult();

                            User user = new User(doc2.getId(), doc2.getString("username"),
                                    Integer.parseInt(doc2.get("emblem_league").toString()),
                                    Integer.parseInt(doc2.get("emblem_team").toString()));

                            String mentionId = doc1.getString("mentionId");

                            mentionIds.add(mentionId);
                            authors.add(user);

                            snapshots1.add(doc1);

                        }
                        forum_get_comments_step_three(_context, _action, snapshots1, mentionIds, authors);
                    }
                });
            }
        });
    }

    // TODO: get all comments, get userId using the mentionId(commentId)
    private static void forum_get_comments_step_three(Context _context, String _action, ArrayList<DocumentSnapshot> _snapshot1, ArrayList<String> _mentionIds, ArrayList<User> _authors){

        Task tempTask3 = mDb.collection("comments").get();

        ArrayList<String> mentionUserIds = new ArrayList<>();

        Tasks.whenAllSuccess(tempTask3).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                // whatever class
                QuerySnapshot snapshots = (QuerySnapshot) tempTask3.getResult();

                for (int i=0; i< _mentionIds.size(); i++){

                    for (DocumentSnapshot document: snapshots) {

                        if(_mentionIds.get(i).equals(document.getId())){

                            mentionUserIds.add(document.getString("userId")); // TODO: this array's count is DIFFERENT from mentionIds and authorIds.
                        }
                    }
                }
                forum_get_comments_step_four(_context, _action, _snapshot1, _mentionIds, _authors, mentionUserIds);
            }
        });
    }

    // TODO: combine data into one.
    // TODO: (mentionIds.size == mentionUsernames.size) << (authors.size == snapshot1.size)
    private static void forum_get_comments_step_four(Context _context, String _action, ArrayList<DocumentSnapshot> _snapshot1, ArrayList<String> _mentionIds, ArrayList<User> _authors, ArrayList<String> _mentionUserIds){

        Task task4 = mDb.collection("users").get();

        ArrayList<String> mentionUsernames = new ArrayList<>();
        ArrayList<Comment> finalComments = new ArrayList<>();

        Tasks.whenAllSuccess(task4).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {

                QuerySnapshot snapshots = (QuerySnapshot) task4.getResult();

                for (int i=0; i< _mentionUserIds.size(); i++){

                    for (DocumentSnapshot document: snapshots) {

                        if(_mentionUserIds.get(i).equals(document.getId())){

                            mentionUsernames.add(document.getString("username"));
                        }
                    }
                }

                // TODO: fill out mention arrays with stubs, correlating to authors.
                ArrayList<String> newMentionUsernames = new ArrayList<>();
                int x = 0;
                for (int i=0; i<_snapshot1.size(); i++) {

                    if(_snapshot1.get(i).getString("mentionId").length() > 0){ // is child
                        newMentionUsernames.add(mentionUsernames.get(x));
                        x++;
                    }else{ // is parent
                        newMentionUsernames.add("");
                    }
                }

                String mentionUsername;
                for (int i=0; i< _authors.size(); i++){

                    Comment toAdd = new Comment(_snapshot1.get(i).getString("postId"), _authors.get(i), newMentionUsernames.get(i), _snapshot1.get(i).getString("description"),
                            _snapshot1.get(i).getString("parentId"), _snapshot1.get(i).getString("mentionId"));
                    toAdd.setId(_snapshot1.get(i).getId());

                    finalComments.add(toAdd);
                }
                sendBroadcast(_context, _action, true, finalComments);
            }
        });
    }

    public static void forum_update_comment_in_comments(Context _context, String _action, Comment _comment, int _case){
        mDb = FirebaseFirestore.getInstance();

        Map<String, Object> comment = new HashMap<>();
        comment.put("description", _comment.getRawDesc());
        comment.put("postId", _comment.getPostId());
        comment.put("userId", _comment.getAuthorUser().getId());
        comment.put("like", _comment.getLike());
        comment.put("yellowcard", _comment.getYCards());
        comment.put("parentId", _comment.getParentId());
        comment.put("mentionId", _comment.getMentionId());

        // case delete
        if(_case == CASE_ADD) {
            mDb.collection("comments").document(_comment.getId()).set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendBroadcast(_context, _action, false, null);
                }
            });
        }else if(_case == CASE_UPDATE){
            mDb.collection("comments").document(_comment.getId()).update(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            });
        }
        // case add
        else if(_case == CASE_REMOVE){
            mDb.collection("comments").document(_comment.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    sendBroadcast(_context, _action, true, null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    sendBroadcast(_context, _action, false, null);
                }
            });
        }
    }

    public static void forum_delete_like_on_post(Context _context, String _action, Comment _post){
        // TODO: update like
        // TODO: update ycard
    }

    public static void forum_update_like_on_comment(Context _context, String _action, Comment _comment, int _case){

        mDb = FirebaseFirestore.getInstance();

        Map<String, Object> newComment = new HashMap<>();

        newComment.put("description", _comment.getRawDesc());
        if(_case == CASE_ADD) newComment.put("like", _comment.getLike() + 1);
        else newComment.put("like", _comment.getLike() - 1);
        newComment.put("mentionId", _comment.getMentionId());
        newComment.put("parentId", _comment.getParentId());
        newComment.put("postId", _comment.getPostId());
        newComment.put("userId", _comment.getAuthorUser().getId());
        newComment.put("yellowcard", _comment.getYCards());

        mDb.collection("comments").document(_comment.getId()).update(newComment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendBroadcast(_context, _action, true, null);
            }
        });
    }


    public static final String EXTRA_BOOLEAN = "EXTRA_BOOLEAN";
    public static final String EXTRA_SERIALIZABLE = "EXTRA_SERIALIZABLE";
    public static final String EXTRA_USER = "EXTRA_USER";

    private static void sendBroadcast(Context _context, String _action, boolean _result, Serializable _serializable){
        Intent intent = new Intent(_action);
        intent.putExtra(EXTRA_BOOLEAN, _result);
        if(_serializable != null){
            intent.putExtra(EXTRA_SERIALIZABLE, _serializable);
        }
        _context.sendBroadcast(intent);
    }

}
