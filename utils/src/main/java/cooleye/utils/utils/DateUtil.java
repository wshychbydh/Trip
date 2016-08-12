package cooleye.utils.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lenayan on 7/25/14.
 */
public class DateUtil {

    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;
    public static final long YEAR = 12 * MONTH;

    public static String formatDateMM(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return formatter.format(date);
    }

    public static String formatCouponDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm");
        return formatter.format(date);
    }
    public static String formatDateOnlyMM(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        return formatter.format(date);
    }

    public static int compareDate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date date = simpleDateFormat.parse(dateString);
            return date.after(new Date()) ? 1 : -1;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String formatTimeOnlyTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(date);
    }

    public static String formatDateStamp(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(date);
    }

    public static String formatDateYMD(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        return formatter.format(date);
    }

    public static String formatDateYMDHm(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return formatter.format(date);
    }

    public static String formatDateYMDHM(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    public static String formatDate(long millis) {
        if (millis < 0)
            return "";
        Calendar nowcal = Calendar.getInstance();

        long longtime = (nowcal.getTimeInMillis() - millis) / 1000;
        long sec = longtime % 60;
        long min = longtime / 60;
        long hour = longtime / (60 * 60);
        long day = longtime / (60 * 60 * 24);

        if (longtime >= 24 * 60 * 60)
            return day + "天";
        else if (longtime > 60 * 60 && longtime < 24 * 60 * 60)
            return hour + "小时";
        else if (longtime > 60 && longtime < 60 * 60)
            return min + "分钟";
        else if (longtime > 10 && longtime < 60)
            return sec + "秒";
        else
            return "刚刚";
    }

    public static long getFinalLeftTime(long lastModified, long leftTime) {
        long period = System.currentTimeMillis() - lastModified;
        return leftTime - period / 1000;
    }

    public static String getFormatCountTime(long leftSeconds) {
        if (leftSeconds > 0) {
            long minutes = leftSeconds / 60000;
            long hours = minutes / 60;
            long day = hours / 24;
            minutes = minutes % 60;
            hours = hours % 24;
            StringBuilder stringBuilder = new StringBuilder();
            if (day > 0)
                stringBuilder.append(day).append("天");
            if (hours > 0)
                stringBuilder.append(hours).append("小时");
            if (minutes > 0)
                stringBuilder.append(minutes).append("分钟");
            return stringBuilder.toString();
        }
        return null;
    }

    public static String parseString2String(String time, String inStringFormat, String outStringFormat) {
        SimpleDateFormat inFormat = new SimpleDateFormat(inStringFormat);
        SimpleDateFormat outFormat = new SimpleDateFormat(outStringFormat);
        try {
            return outFormat.format(inFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }
}
