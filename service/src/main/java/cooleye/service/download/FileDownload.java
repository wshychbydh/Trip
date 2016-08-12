package cooleye.service.download;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author cool
 * @category apk下載
 */
public class FileDownload {

    private final static String TAG = "FileDownload";
    private final static int TIMEOUT = 0; //0表示永不超时
    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private Activity mActivity;
    private DownloadDialog mDownloadDialog;
    private File mFile;

    public FileDownload(@NonNull Activity activity, DownloadDialog dialog) {
        this.mActivity = activity;
        this.mDownloadDialog = dialog;
        initDownload();
    }

    private void initDownload() {
        mFile = new File(LocalStorage.composeAPKPath(mActivity));
        if (mFile.exists()) {
            mFile.delete();
        }
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(String url) {
        if (mDownloadDialog != null) {
            mDownloadDialog.show();
        }
        String baseUrl, path;
        baseUrl = url.substring(0, url.lastIndexOf(File.separator) + 1);
        path = url.replace(baseUrl, "");
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TIME_UNIT)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response response = chain.proceed(chain.request());
                        return response.newBuilder()
                                .body(new ProgressResponseBody(response.body(), new ProgressListener() {
                                    @Override
                                    public void onProgress(long progress, long total, boolean done) {
                                        if (mDownloadDialog != null) {
                                            mDownloadDialog.updateProgress(progress, total);
                                        }
                                    }
                                }))
                                .build();
                    }
                })
                .build();
        DownLoadApi api = builder.client(client)
                .build().create(DownLoadApi.class);

        Call<ResponseBody> call = api.getFile(path);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    byte[] bytes = getBytesFromStream(is);
                    boolean result = saveBytesToFile(bytes);
                    if (result) {
                        DownloadUtil.installApk(mActivity, mFile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mDownloadDialog != null) {
                    mDownloadDialog.dismiss();
                }
                Log.e(TAG, "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mDownloadDialog != null) {
                    mDownloadDialog.dismiss();
                }
                Log.e(TAG, "" + t.getMessage());
            }
        });
    }

    private byte[] getBytesFromStream(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while ((len = is.read(buf, 0, size)) != -1) {
            bos.write(buf, 0, len);
        }
        buf = bos.toByteArray();
        return buf;
    }

    private boolean saveBytesToFile(byte[] bytes) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mFile);
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
