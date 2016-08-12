package cooleye.trip.app.login.page;

import android.content.Intent;
import android.os.Bundle;

import butterknife.InjectView;
import butterknife.OnClick;
import cooleye.trip.R;
import cooleye.trip.app.base.BaseActivity;
import cooleye.trip.app.component.ClearableEditText;
import cooleye.trip.app.component.PasswordEditText;
import cooleye.trip.app.login.support.InputChecker;


public class LoginActivity extends BaseActivity {

    @InjectView(R.id.login_phone)
    ClearableEditText mPhoneEt;
    @InjectView(R.id.password)
    PasswordEditText mPasswordEt;

    @OnClick(R.id.login_btn)
    public void login() {
        if (InputChecker.checkPhone(mPhoneEt) && InputChecker.checkPassword(mPasswordEt)) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @OnClick(R.id.login_forget_password)
    public void onClick() {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        intent.putExtra(SetPasswordActivity.FROM, SetPasswordActivity.LOGIN);
        startActivity(intent);
    }
}
