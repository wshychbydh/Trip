package cooleye.trip.server;

import android.support.annotation.NonNull;

import cooleye.service.Utils.ServerException;
import cooleye.service.subscribers.ResponseListener;
import cooleye.trip.app.base.BaseActivity;
import cooleye.utils.utils.DialogUtil;
import cooleye.utils.utils.ToastUtil;

/**
 * Created by cool on 16-8-9.
 */
public class ResponseImpListener<T> extends ResponseListener<T> {

    private BaseActivity mActivity;

    public ResponseImpListener() {
    }

    public ResponseImpListener(@NonNull BaseActivity activity) {
        mActivity = activity;
        mActivity.mDialog = DialogUtil.get(mActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        startProgressDialog();
    }

    @Override
    public void onFailure(ServerException exception) {
        super.onFailure(exception);
        showToast(exception.getMessage());
    }

    @Override
    public final void onSuccess(T t) {
        super.onSuccess(t);
        if (t == null) {
            onSucceed();
        } else {
            onSucceed(t);
        }
    }

    public void onSucceed() {
    }

    public void onSucceed(@NonNull T t) {}

    @Override
    public void onCancel() {
        super.onCancel();
        dismissProgressDialog();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissProgressDialog();
    }

    private void showToast(CharSequence cs) {
        if (mActivity != null) {
            ToastUtil.show(mActivity, cs);
        }
    }

    private void startProgressDialog() {
        if (mActivity != null && mActivity.mDialog != null) {
            mActivity.mDialog.startProgressDialog();
        }
    }

    private void dismissProgressDialog() {
        if (mActivity != null && mActivity.mDialog != null) {
            mActivity.mDialog.stopProgressDialog();
        }
    }
}
