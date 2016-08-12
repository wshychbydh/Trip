package cooleye.trip.app.login.support;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;

import cooleye.trip.R;
import cooleye.utils.utils.StringUtil;

/**
 * Created by cool on 16-6-20.
 */
public class InputChecker {


    /**
     * 手机号、密码判断
     */
    public static final int PHONE_LENGTH = 11;

    private final static int PASSWORD_MIN_LENGTH = 6;
    private final static int PASSWORD_MAX_LENGTH = 14;

    public static boolean checkPhone(@NonNull EditText phoneEditText) {
        if (TextUtils.isEmpty(phoneEditText.getText())) {
            phoneEditText.setError(phoneEditText.getContext()
                    .getString(R.string.account_phone_num_hint));
            return false;
        }
        if (!StringUtil.isMobileNum(phoneEditText.getText().toString())) {
            phoneEditText.setError(phoneEditText.getContext()
                    .getString(R.string.account_phone_num_not_avalid));
            return false;
        }
        return true;
    }

    public static boolean checkCaptcha(@NonNull EditText captchaEditText) {
        if (TextUtils.isEmpty(captchaEditText.getText())) {
            captchaEditText.setError(captchaEditText.getContext()
                    .getString(R.string.account_check_num_hint));
            return false;
        }
        return true;
    }

    public static boolean checkPassword(@NonNull EditText passwordEditText) {
        String password = passwordEditText.getText().toString();
        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            passwordEditText.setError(passwordEditText.getContext()
                    .getString(R.string.account_password_6_14));
            return false;
        }
        return true;
    }
}
