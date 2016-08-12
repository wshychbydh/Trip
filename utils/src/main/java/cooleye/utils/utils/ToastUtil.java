package cooleye.utils.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

    private static final int TOAST_TOP = 50;
    private static final int TOAST_LEFT = 0;
    /**
     * show toast message after action executed. the toast display time is short
     * default.
     *
     * @param mContext
     * @param message
     */

    private static Toast mToast = null;

    public static void show(@NonNull Context context, @NonNull CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * show toast message after action executed. the toast display time is short
     * default.
     *
     * @param context
     * @param resId
     */
    public static void show(@NonNull Context context, int resId) {
        show(context, context.getString(resId));
    }

    /**
     * show toast message after action executed.
     *
     * @param context
     * @param message
     * @param duration : Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void show(Context context, CharSequence message, int duration) {
        initToast(context, message, duration);
        mToast.show();
    }

    /**
     * show toast message after action executed.
     *
     * @param context
     * @param resId
     * @param duration : Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void show(Context context, int resId, int duration) {
        show(context, context.getString(resId), duration);
    }

    private static void initToast(Context context, CharSequence content, int duration) {
        if (mToast == null) {
            mToast = new Toast(context.getApplicationContext());
            mToast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, TOAST_LEFT, TOAST_TOP);
        }
        mToast.setView(getToastView(context, content));
        mToast.setDuration(duration);
    }

    private static View getToastView(Context context, CharSequence content) {
        TextView toastTv = getTextView(context);
        toastTv.setText(content);
        return toastTv;
    }

    private static TextView getTextView(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        TextView tv = new TextView(context);
        tv.setBackgroundColor(Color.parseColor("#AA000000"));
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (48 * density)));
        int padding = (int) (density * 10);
        tv.setPadding(padding, padding, padding, padding);
        tv.setTextColor(Color.WHITE);
        return tv;
    }
}
