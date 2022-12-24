package com.example.seanlee_thefootballgallery_2201.a_authentications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seanlee_thefootballgallery_2201.R;
import com.example.seanlee_thefootballgallery_2201.f_utils.DBUtil;
import com.example.seanlee_thefootballgallery_2201.f_utils.StringUtil;

public class AuthSignUpEmail extends AppCompatActivity {

    private static final String TAG = "AuthSignUpEmail.TAG";

    private Context mContext;
    private EditText mEt_email;
    private Button mBtn_verify, mBtn_next;

    //
    // TODO: Life-cycle
    //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_email);


        mContext = this;

        mEt_email = findViewById(R.id.auth_signup_email_et_email);
        mBtn_verify = findViewById(R.id.auth_signup_email_btn_verify);
        mBtn_next = findViewById(R.id.auth_signup_email_btn_next);

        mBtn_verify.setOnClickListener(onVerifyTap);
        mBtn_next.setOnClickListener(onNextTap);

        toggleBtns(false);
    }

    Receiver mVerificationReceiver = new Receiver();

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(VERIFY_EMAIL);
        registerReceiver(mVerificationReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mVerificationReceiver);
    }

    //
    // TODO: UIs
    //

    View.OnClickListener onVerifyTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String result = StringUtil.isEmailValid(mEt_email);
            if(result.equals(StringUtil.MSG_EMAIL_VALID)){
                // TODO: firebase email verification
                DBUtil.auth_verify_email(mContext, VERIFY_EMAIL, mEt_email.getText().toString(), DBUtil.CASE_SIGNUP);
            }else {
                StringUtil.makeToast(mContext, result);
            }
        }
    };

    View.OnClickListener onNextTap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, AuthSignUpProfile.class);
            intent.putExtra(Intent.EXTRA_TEXT, mEt_email.getText().toString());
            startActivity(intent);
        }
    };

    //
    // TODO: Methods
    //

    private void toggleBtns(boolean _isVerified){

        int btnGray = R.color.gray_03;
        int btnGreen = R.color.green_01;

        if(!_isVerified){
            mBtn_verify.setClickable(true);
            mBtn_verify.setBackgroundColor(getResources().getColor(btnGreen, null));

            mBtn_next.setClickable(false);
            mBtn_next.setBackgroundColor(getResources().getColor(btnGray, null));
        }else {
            mBtn_verify.setText("Email Verified");
            mBtn_verify.setClickable(false);
            mBtn_verify.setBackgroundColor(getResources().getColor(btnGray, null));

            mBtn_next.setClickable(true);
            mBtn_next.setBackgroundColor(getResources().getColor(btnGreen, null));
        }
    }

    private static final String VERIFY_EMAIL = "VERIFY_EMAIL";

    class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean result = intent.getBooleanExtra(DBUtil.EXTRA_BOOLEAN, false);

            if(intent.getAction() == VERIFY_EMAIL){
                if(result){
                    toggleBtns(true);
                }else {
                    StringUtil.makeToast(mContext, "Email verification failed..");
                }
            }
        }
    }
}
