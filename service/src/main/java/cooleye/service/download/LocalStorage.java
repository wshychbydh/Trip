package cooleye.service.download;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import cooleye.service.R;

/**
 * @author cool
 * @category 文件夹构建类
 */
public class LocalStorage {
    private static String LOCAL_DIR = "appname";

    /**
     * 文件夹根目录
     */

    protected static final String APK = "apk";

    // create apk storage path
    public static String composeAPKPath(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRootFile(context).getAbsolutePath());
        sb.append(File.separator);
        sb.append(APK);
        File folder = new File(sb.toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        sb = new StringBuilder();
        sb.append(folder.getAbsolutePath());
        sb.append(File.separator).append(context.getString(R.string.app_name))
                .append(".apk");
        return sb.toString();
    }

    private static File getRootFile(Context context) {
        File folder;
        /* Checks if external storage is available for read and write */
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            folder = Environment.getExternalStoragePublicDirectory(LOCAL_DIR);
        } else {
            folder = context.getExternalFilesDir(LOCAL_DIR);
        }

        if (!folder.exists()) {
           folder.mkdirs();
        }
        return folder;
    }
}
