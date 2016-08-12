package cooleye.service.download;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * @author cool
 * @cagetory DownloadUtil
 * @description TODO
 */
public class DownloadUtil {

    public static String getFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        return fileName;
    }

    public static void installApk(@NonNull Context context, @NonNull File file) {
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void installApk(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 检测是否有网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connm == null) {
            return false;
        } else {
            NetworkInfo[] nis = connm.getAllNetworkInfo();
            if (nis != null) {
                for (int i = 0; i < nis.length; i++) {
                    if (nis[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWifiAvailable(Context context) {
        if (context == null) {
            return false;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int mWifiState = wifiManager.getWifiState();
        return mWifiState == WifiManager.WIFI_STATE_ENABLED;
    }

    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAPKVersion(Context context, final String path) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path,
                    PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isApkDownload(Context context, String versionName, File apk) {
        if (apk.exists()) {
            String vn = getAPKVersion(context, apk.getAbsolutePath());
            if (versionName.equalsIgnoreCase(vn) && apk.canExecute()) {
                return true;
            } else {
                apk.delete();
            }
        }
        return false;
    }
}
