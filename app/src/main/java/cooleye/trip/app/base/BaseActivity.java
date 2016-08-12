package cooleye.trip.app.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cooleye.utils.utils.DialogUtil;

/**
 * In normal case,all activities should extends this activity.
 * Created by cool on 16-8-8.
 */

public class BaseActivity extends AppCompatActivity {

    public DialogUtil mDialog;
    protected Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        mActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        if (mDialog != null) {
            mDialog.onDestroy();
        }
    }
}
