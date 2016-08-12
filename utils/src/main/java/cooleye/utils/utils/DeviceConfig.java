package cooleye.utils.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;

public class DeviceConfig {

    public static final String DEVIDE_OS = "Android";
    public static String sDeviceID;
    public static String sDeviceName;
    public static String sDeviceOSVersion;
    public static String sAppversion;

    public static void init(Context context) {
        sDeviceID = getDeviceID(context);
        sDeviceName = getDeviceName(context);
        sDeviceOSVersion = getOSVersion(context);
        sAppversion = getAppVersion(context);
    }

    private static String getDeviceID(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    private static String getDeviceName(Context context) {
        return Build.MODEL;
    }

    private static String getOSVersion(Context context) {
        return Build.VERSION.RELEASE;
    }

    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
