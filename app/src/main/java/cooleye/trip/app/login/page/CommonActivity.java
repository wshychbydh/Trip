package cooleye.trip.app.login.page;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

import butterknife.InjectView;
import butterknife.OnTextChanged;
import cooleye.trip.R;
import cooleye.trip.app.base.BaseActivity;
import cooleye.trip.app.component.ClearableEditText;
import cooleye.trip.app.component.PasswordEditText;
import cooleye.trip.app.login.support.InputChecker;
import cooleye.trip.app.login.support.SMSBroadcastReceiver;
import cooleye.trip.server.UserEntity;
import cooleye.trip.server.ResponseImpListener;

/**
 * Created by cool on 16-7-1.
 * 这个类用于提取注册/设置密码页面的公共功能部分
 * 包括：倒计时，读取短信，注册/设置密码回调等
 */
public class CommonActivity extends BaseActivity {

    /*Captcha*/
    public static final int CAPTCHA_MILLIS_UTIL = 60000;
    public static final int CAPTCHA_INTERVAL = 1000;

    @InjectView(R.id.captcha)
    EditText mCaptchaEditText;

    @InjectView(R.id.get_captcha)
    Button mGetCaptchaButton;

    @InjectView(R.id.phone_num)
    ClearableEditText mPhoneEditText;

    @InjectView(R.id.password)
    PasswordEditText mPasswordEditText;

    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    /**
     * 当点击注册/设置密码按钮时，其回调功能都是一致的，故可以提取到一处统一处理
     */
    protected ResponseImpListener<UserEntity> mResponseListener =
            new ResponseImpListener<UserEntity> (this) {

                @Override
                public void onSucceed(UserEntity userInfo) {
                    //TODO save userinfo if needed
                    finish();
                }
            };

    /**
     * 时间倒计时器
     */
    private CountDownTimer mCountDownTimer = new CountDownTimer(CAPTCHA_MILLIS_UTIL,
            CAPTCHA_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished < CAPTCHA_MILLIS_UTIL) {
                mGetCaptchaButton.setEnabled(false);
                mGetCaptchaButton.setText(getString(R.string.account_get_captcha_left_time,
                        millisUntilFinished / 1000));
            }
        }

        @Override
        public void onFinish() {
            resetCaptchaButton();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerSMSBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注销短信监听广播
        unregisterReceiver(mSMSBroadcastReceiver);
    }

    private void registerSMSBroadcastReceiver() {
        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(SMSBroadcastReceiver.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver
                .MessageListener() {
            @Override
            public void onReceived(String message) {
                if (mCaptchaEditText != null) {
                    mCaptchaEditText.setText(message);
                    mCaptchaEditText.setSelection(message.length());
                }
            }
        });
    }

    protected boolean checkInput() {
        return InputChecker.checkPhone(mPhoneEditText)
                && InputChecker.checkCaptcha(mCaptchaEditText)
                && InputChecker.checkPassword(mPasswordEditText);
    }

    private void resetCaptchaButton() {
        mPhoneEditText.setEnabled(true);
        mCountDownTimer.cancel();
        mGetCaptchaButton.setEnabled(true);
        mGetCaptchaButton.setText(R.string.account_get_phone_check_num);
    }

    protected void getCaptcha(String action) {
        if (InputChecker.checkPhone(mPhoneEditText)) {
            mPhoneEditText.setEnabled(false);
            mCaptchaEditText.requestFocus();
            mCountDownTimer.start();
        }
    }

    @OnTextChanged(R.id.phone_num)
    void afterTextChanged(Editable s) {
        if (s.length() >= InputChecker.PHONE_LENGTH) {
            mGetCaptchaButton.setEnabled(true);
            mGetCaptchaButton.setSelected(true);
        } else {
            mGetCaptchaButton.setEnabled(false);
            mGetCaptchaButton.setSelected(false);
        }
    }

    protected void toActivity(Class<? extends BaseActivity> clazz) {
        startActivity(new Intent(this, clazz));
        finish();
    }

    @Override
    protected void onDestroy() {
        mCountDownTimer.cancel();
        super.onDestroy();
    }
}
