package cooleye.trip.app.login.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static MessageListener mMessageListener;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                //短信内容
                String content = smsMessage.getDisplayMessageBody();
                if (TextUtils.isEmpty(content)) return;
                mMessageListener.onReceived(getCode(content));
                abortBroadcast();
            }
        }

    }

    //回调接口
    public interface MessageListener {
        void onReceived(String message);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        mMessageListener = messageListener;
    }

    public static String getCode(String body) {
        String expression = "(?<!\\d)(\\d{6})(?!\\d)";
        Pattern p = Pattern.compile(expression);

        Matcher m = p.matcher(body);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }
}
