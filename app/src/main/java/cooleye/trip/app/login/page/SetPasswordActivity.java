package cooleye.trip.app.login.page;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.InjectView;
import butterknife.OnClick;
import cooleye.trip.R;

/**
 * Created by cool on 16-6-15.
 */
public class SetPasswordActivity extends CommonActivity {

    public static final String FROM = "form";

    public static final int LOGIN = 0;
    public static final int GUIDE = 1;
    public static final int SETTING = 2;

    @InjectView(R.id.btn_set_password)
    Button mSetPwdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        initView();
    }

    private void initView() {
        int from = getIntent().getIntExtra(FROM, GUIDE);
    }

    @OnClick(R.id.get_captcha)
    void getCaptcha(View v) {
    }

    @OnClick(R.id.btn_set_password)
    void setPassword(View v) {
        if (checkInput()) {
        }
    }
}
