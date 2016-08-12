package cooleye.utils.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by luozheng on 14-9-19.
 */
public class DeviceUtil {

    private static InputMethodManager mInputMethodManager;

    private static InputMethodManager getInstanceManager(Context context) {
        if (null == mInputMethodManager) {
            mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        return mInputMethodManager;
    }

    public static void hideSoftInputFromWindow(Context context, View view) {
        getInstanceManager(context).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftInputFromWindow(Context context, View view) {
        getInstanceManager(context).showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void sendSms(Context context, String sendToMobile, String sendToContent){
        Uri smsToUri = Uri.parse("smsto:" + sendToMobile);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", sendToContent);
        context.startActivity(intent);
    }

}
