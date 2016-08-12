package cooleye.trip.app.login.page;

import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;
import cooleye.trip.R;

/**
 * Created by cool on 16-6-15.
 */
public class RegisterActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle();
    }

    private void setTitle() {
    }

    @OnClick(R.id.get_captcha)
    void getCaptcha(View v) {
    }

    @OnClick(R.id.btn_register)
    void register(View v) {
        if (checkInput()) {

        }
    }

    @OnClick(R.id.register_clause)
    void registerClouse(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
