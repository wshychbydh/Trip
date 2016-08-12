package cooleye.service.download;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import cooleye.service.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AppUpdate {

    private Activity mActivity;
    private IAppUpdateCallback callback;
    private DownloadHelper mDownloadHelper;

    public AppUpdate(Activity context, IAppUpdateCallback callback) {
        this.mActivity = context;
        this.callback = callback;
    }

    public void checkUpdate() {
        Retrofit retrofit = HttpUtil.getRetrofit();
        ApkService service = retrofit.create(ApkService.class);
        service.checkUpdate().enqueue(new Callback<DownloadBean>() {
            @Override
            public void onResponse(Call<DownloadBean> call, retrofit2.Response<DownloadBean> response) {
                DownloadBean downloadBean = response.body();
                String currentVersion = DownloadUtil.getVersionName(mActivity);
                if (downloadBean.getVersion() != null && !downloadBean.getVersion().equals(currentVersion)) {
                    showAlertDialog(downloadBean, currentVersion);
                    return;
                }
            }

            @Override
            public void onFailure(Call<DownloadBean> call, Throwable t) {
                Log.e("AppUpdate", t.getMessage());
            }
        });
    }

    /**
     * 发现新版本，强制更新
     *
     * @param downloadBean
     */
    public void showAlertDialog(final DownloadBean downloadBean, final String currentVersionName) {
        showAlertDialog(
                String.format(mActivity.getString(R.string.find_update), currentVersionName, downloadBean.getVersion()), downloadBean.getMsg(),
                mActivity.getString(R.string.update), new IAppUpdateCallback() {
                    @Override
                    public void callback(boolean update) {
                        if (update) {
                            File file = new File(LocalStorage.composeAPKPath(mActivity));
                            if (DownloadUtil.isApkDownload(mActivity, downloadBean.getVersion(), file)) {
                                DownloadUtil.installApk(mActivity, file);
                            } else {
                                if (downloadBean.isForce()) {
                                    forceDownload(downloadBean.getSoftwareUrl());
                                    return;
                                } else {
                                    downloadApk(downloadBean.getSoftwareUrl());
                                    Toast.makeText(mActivity, R.string.update_background, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        // 存在更新,但是未更新
                        if (callback != null) {
                            callback.callback(true);
                        }
                    }
                }, !downloadBean.isForce());
    }

    private void downloadApk(String url) {
        mDownloadHelper = new DownloadHelper(mActivity, url);
        mDownloadHelper.start();
    }

    public void forceDownload(String url) {
        new FileDownload(mActivity, new DownloadDialog(mActivity)).download(url);
    }

    public void onDestory() {
        if (mDownloadHelper != null) {
            mDownloadHelper.onDestory();
        }
    }

    public void showAlertDialog(final String title, final String msg, final String btn,
                                final IAppUpdateCallback backInterface, final boolean showCancle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(
                        btn == null ? mActivity.getString(R.string.sure) : btn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (backInterface != null) {
                                    backInterface.callback(true);
                                }
                            }
                        });
        if (showCancle) {
            builder.setPositiveButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (backInterface != null) {
                                backInterface.callback(false);
                            }
                        }
                    });
        }
        builder.show();
    }
}
