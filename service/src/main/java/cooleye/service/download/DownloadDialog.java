package cooleye.service.download;

import android.app.Activity;
import android.app.Dialog;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cooleye.service.R;

/**
 * Created by cool on 16-6-6.
 */
public class DownloadDialog extends Dialog {

    private Activity mActivity;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    public DownloadDialog(Activity activity) {
        super(activity, R.style.dialog);
        this.mActivity = activity;
        initDialog();
    }

    private void initDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog, null);
        setContentView(view);
        setCancelable(false);
        mTextView = (TextView) view.findViewById(R.id.progress_tv);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    public void updateProgress(final long read, final long total) {
        final float ratio = (float) read / (float) total * 100.0f;
        if (Looper.myLooper() == mActivity.getMainLooper()) {
            updateProgress(ratio);
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateProgress(ratio);
                }
            });
        }
    }

    private void updateProgress(float ratio) {
        mTextView.setText(String.format("已下载 %1$.2f%2$s", ratio, "%"));
        mProgressBar.setProgress((int) ratio);
    }
}
