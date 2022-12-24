package com.example.seanlee_thefootballgallery_2201.a_authentications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.b_core.CoreActivity;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;

public class AuthHome extends AppCompatActivity {

    private static final String TAG = "AuthHome.TAG";

    FirebaseAuth mAuth;
    Context mContext;

    EditText mEt_email, mEt_pwd;
    TextView mBtn_forgot_pwd, mBtn_signup, mTv_err_id, mTv_err_pwd;
    Button mBtn_login;

    //
    // TODO: Life-cycle
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_home);


        mAuth = FirebaseAuth.getInstance();
        mContext = this;

        mEt_email = findViewById(R.id.auth_home_et_email);
        mEt_pwd = findViewById(R.id.auth_home_et_pwd);
        mBtn_forgot_pwd = findViewById(R.id.auth_home_btn_forgot);
        mBtn_login = findViewById(R.id.auth_home_btn_login);
        mBtn_signup = findViewById(R.id.auth_home_btn_signup);
        mTv_err_id = findViewById(R.id.auth_home_err_id);
        mTv_err_pwd = findViewById(R.id.auth_home_err_pwd);

        mBtn_signup.setOnClickListener(onSignUpTap);
        mBtn_login.setOnClickListener(onLoginTap);
        mBtn_forgot_pwd.setOnClickListener(onForgotTap);
        mEt_email.setOnFocusChangeListener(onIdTap);
        mEt_pwd.setOnFocusChangeListener(onPwdTap);

        toggleErrFeedback(false, TOGGLE_CASE_BOTH);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startMainActivity();
        }
    }

    Receiver mEmailCheckReceiver = new Receiver();
    Receiver mLoginReceiver = new Receiver();

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(AUTH_EMAIL_CHECK);
        registerReceiver(mEmailCheckReceiver, filter);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(AUTH_LOGIN);
        registerReceiver(mLoginReceiver, filter2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mEmailCheckReceiver);
        unregisterReceiver(mLoginReceiver);
    }


    //
    // TODO: Methods
    //

    private void startMainActivity(){
        Intent intent = new Intent(mContext, CoreActivity.class);
        startActivity(intent);
    }

    View.OnClickListener onLoginTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String emailMsg = StringUtil.isEmailValid(mEt_email);
            String pwdMsg = StringUtil.isPwdValid(new EditText[]{mEt_pwd});
            if(emailMsg.equals(StringUtil.MSG_EMAIL_VALID)){
                if(pwdMsg.equals(StringUtil.MSG_PWD_VALID)){
                    // TODO: 1. try login - email check
                    DBUtil.auth_verify_email(mContext, AUTH_EMAIL_CHECK, mEt_email.getText().toString(), DBUtil.CASE_LOGIN);
                }else {
                    mTv_err_pwd.setText(pwdMsg);
                    toggleErrFeedback(true, TOGGLE_CASE_PWD);
                }
            }else{
                mTv_err_id.setText(emailMsg);
                toggleErrFeedback(true, TOGGLE_CASE_ID);
            }



        }
    };

    View.OnClickListener onForgotTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO:

        }
    };

    View.OnClickListener onSignUpTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, AuthSignUpEmail.class);
            startActivity(intent);
        }
    };

    View.OnFocusChangeListener onIdTap = new View.OnFocusChangeListener() {
        @Override public void onFocusChange(View view, boolean b) { toggleErrFeedback(false, TOGGLE_CASE_ID); }
    };

    View.OnFocusChangeListener onPwdTap = new View.OnFocusChangeListener() {
        @Override public void onFocusChange(View view, boolean b) { toggleErrFeedback(false, TOGGLE_CASE_PWD); }
    };

    private static final int TOGGLE_CASE_ID = 0;
    private static final int TOGGLE_CASE_PWD = 1;
    private static final int TOGGLE_CASE_BOTH = 2;

    private void toggleErrFeedback(boolean _isShow, int _case){
        int visibility;

        if(_isShow) visibility = View.VISIBLE;
        else visibility = View.GONE;

        if(_case == TOGGLE_CASE_ID){
            mTv_err_id.setVisibility(visibility);
        }else if(_case == TOGGLE_CASE_PWD){
            mTv_err_pwd.setVisibility(visibility);
        }else{
            mTv_err_id.setVisibility(visibility);
            mTv_err_pwd.setVisibility(visibility);
        }
    }

    //
    // TODO: Receivers
    //

    private static final String AUTH_EMAIL_CHECK = "AUTH_EMAIL_CHECK";
    private static final String AUTH_LOGIN = "AUTH_LOGIN";

    class Receiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean success = intent.getBooleanExtra(DBUtil.EXTRA_BOOLEAN, false);
            String action = intent.getAction();

            if(action == AUTH_EMAIL_CHECK){
                if(success){ // TODO: 1.1. email check success - try login
                    toggleErrFeedback(false, TOGGLE_CASE_ID);
                    Log.i(TAG, "step 1 receiver");
                    DBUtil.auth_login(mContext, AUTH_LOGIN, mEt_email.getText().toString(), mEt_pwd.getText().toString());
                }else{ // TODO: 1.2. email check fail - user feedback: username doesn't exists
                    mTv_err_id.setText(StringUtil.MSG_INCORRECT_INPUT);
                    toggleErrFeedback(true, TOGGLE_CASE_ID);
                }
            }else if(action == AUTH_LOGIN){
                if(success){ // TODO: 1.1.1. login success - start news activity
                    Log.i(TAG, "step 2 receiver");
                    toggleErrFeedback(false, TOGGLE_CASE_BOTH);
                    startMainActivity();
                }else{ // TODO: 1.1.2. login fail - user feedback: pwd error
                    mTv_err_id.setText(StringUtil.MSG_PWD_MATCH);
                    toggleErrFeedback(true, TOGGLE_CASE_PWD);
                }
            }
        }
    }
}




























