package cooleye.utils.utils;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.MainThread;

import cooleye.device.R;

/**
 * Created by cool on 16-8-9.
 */
public class DialogUtil {

    private static DialogUtil sInstance;
    private Activity mActivity;  //只允许在activity中显示
    private Dialog mDialog;
    private CustomProgressDialog mProgressDialog;

    private DialogUtil(Activity activity) {
        this.mActivity = activity;
    }


    /**
     * @param activity
     * @return
     */
    @MainThread
    public static DialogUtil get(Activity activity) {
        if (sInstance == null) {
            sInstance = new DialogUtil(activity);
        }
        return sInstance;
    }

    public void show() {
        create();
        mDialog.show();
    }

    public void startProgressDialog() {
        createProgressDialog();
        mProgressDialog.show();
    }

    /**
     * only dismiss the dialog
     */
    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    /**
     * only dismiss the dialog
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * call this if the activity never use this dialog
     */
    public void stop() {
        dismiss();
        mDialog = null;
    }

    public void stopProgressDialog() {
        dismissProgressDialog();
        mProgressDialog = null;
    }

    public Dialog create() {
        if (mDialog == null) {
            mDialog = new Dialog(mActivity, R.style.dialog);
        }
        return mDialog;
    }

    public CustomProgressDialog createProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = CustomProgressDialog.createDialog(mActivity);
        }
        return mProgressDialog;
    }

    /**
     * dismiss all dialog and release context
     */
    public void onDestroy() {
        if (mDialog != null) {
            stop();
        }
        if (mProgressDialog != null) {
            stopProgressDialog();
        }
        sInstance = null;
    }
}
