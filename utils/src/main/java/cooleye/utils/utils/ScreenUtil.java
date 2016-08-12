package cooleye.utils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtil {

    public static int sScreenWidth;
    public static int sScreenHeight;
    public static float sScreenDensity;
    public static float sScreenPPI;
    public static float sScreenInch;

    public static void init(Context context) {
        WindowManager windowManger = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        sScreenWidth = getScreenWidth(windowManger);
        sScreenHeight = getScreenHeight(windowManger);
        sScreenDensity = getScreenDensity(windowManger);
        sScreenPPI = getScreenPPI(windowManger);
        sScreenInch = getScreenInch();
    }

    public static int getStatusHeight(Activity activity) {
        int height = 0;
        final Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static int getActionBarHeight(Activity activity) {
        final int actionBarContainerId = Resources.getSystem().getIdentifier("action_bar_container", "id", "android");
        final View actionBarContainer = activity.findViewById(actionBarContainerId);
        if (null != actionBarContainer) {
            return actionBarContainer.getBottom();
        }
        return 0;
    }

    public static int getPxByDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }

    public static int getPxByDp(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }

    public static int getDpByPx(int px, Context context) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int sp2px(float spValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(float pxValue, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static float pxToSp(float px, Context context) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    private static int getScreenWidth(WindowManager windowManger) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManger.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private static int getScreenHeight(WindowManager windowManger) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManger.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    private static float getScreenDensity(WindowManager windowManger) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManger.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    private static float getScreenPPI(WindowManager windowManger) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManger.getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }

    private static float getScreenInch() {
        return (float) Math.sqrt((Math.pow(sScreenHeight, 2) + Math.pow(sScreenWidth, 2))) / sScreenPPI;

    }

}
