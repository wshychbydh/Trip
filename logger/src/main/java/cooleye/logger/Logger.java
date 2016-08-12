package cooleye.logger;

import android.util.Log;

/**
 * A powerful logger which help print more useful information in DEBUG mode.
 * With this logger you can also print well formatted json Strings.
 * It's build based on default Android log.
 */
public class Logger {
    private static final String INDENT = "  ";

    private static boolean DEBUG = true;

    private static String sTag = "Trip";

    public static void setIsDebug(boolean isDebug) {
        DEBUG = isDebug;
    }

    public static void setTag(String tag) {
        sTag = tag;
    }

    /**
     * Log message at INFO level.
     */
    public static void i(String msg) {
        if (DEBUG) {
            Log.i(sTag, enchantMessage(msg));
        }
    }

    /**
     * Log message at INFO level.
     *
     * @param msg msg.toString() will be used as the message
     */
    public static void i(Object msg) {
        if (DEBUG) {
            Log.i(sTag, enchantMessage(msg.toString()));
        }
    }

    /**
     * Log message at INFO level with string format.
     */
    public static void i(String format, Object... args) {
        if (DEBUG) {
            Log.i(sTag, enchantMessage(String.format(format, args)));
        }
    }

    /**
     * Log message at DEBUG level.
     */
    public static void d(String msg) {
        if (DEBUG) {
            Log.d(sTag, enchantMessage(msg));
        }
    }

    /**
     * Log message at DEBUG level.
     *
     * @param msg msg.toString() will be used as the message
     */
    public static void d(Object msg) {
        if (DEBUG) {
            Log.d(sTag, enchantMessage(msg.toString()));
        }
    }

    /**
     * Log message at DEBUG level with string format.
     */
    public static void d(String format, Object... args) {
        if (DEBUG) {
            Log.d(sTag, enchantMessage(String.format(format, args)));
        }
    }


    /**
     * Log message at WARN level.
     */
    public static void w(String msg) {
        if (DEBUG) {
            Log.w(sTag, enchantMessage(msg));
        } else {
            Log.w(sTag, msg);
        }
    }

    /**
     * Log message at WARN level.
     *
     * @param msg msg.toString() will be used as the message
     */
    public static void w(Object msg) {
        if (DEBUG) {
            Log.w(sTag, enchantMessage(msg.toString()));
        } else {
            Log.w(sTag, msg.toString());
        }
    }

    /**
     * Log message at WARN level with string format.
     */
    public static void w(String format, Object... args) {
        if (DEBUG) {
            Log.w(sTag, enchantMessage(String.format(format, args)));
        } else {
            Log.w(sTag, String.format(format, args));
        }
    }

    /**
     * Log throwable at WARN level.
     *
     * @param msg       short description for the throwable
     * @param throwable
     */
    public static void w(String msg, Throwable throwable) {
        if (DEBUG) {
            Log.w(sTag, enchantMessage(msg), throwable);
        } else {
            Log.w(sTag, msg, throwable);
        }
    }

    /**
     * Log message at ERROR level.
     */
    public static void e(String msg) {
        if (DEBUG) {
            Log.e(sTag, enchantMessage(msg));
        } else {
            Log.e(sTag, msg);
        }
    }

    /**
     * Log message at ERROR level.
     *
     * @param msg msg.toString() will be used as the message
     */
    public static void e(Object msg) {
        if (DEBUG) {
            Log.e(sTag, enchantMessage(msg.toString()));
        } else {
            Log.e(sTag, msg.toString());
        }
    }

    /**
     * Log message at ERROR level with string format.
     */
    public static void e(String format, Object... args) {
        if (DEBUG) {
            Log.e(sTag, enchantMessage(String.format(format, args)));
        } else {
            Log.e(sTag, String.format(format, args));
        }
    }

    /**
     * Log throwable at ERROR level.
     *
     * @param msg       short description for the throwable
     * @param throwable
     */
    public static void e(String msg, Throwable throwable) {
        if (DEBUG) {
            Log.e(sTag, enchantMessage(msg), throwable);
        } else {
            Log.e(sTag, msg, throwable);
        }
    }

    /**
     * Log INFO level json String in a pretty format.
     *
     * @param name       short description for the json String.
     * @param jsonString
     */
    public static void j(String name, String jsonString) {
        if (DEBUG) {
            Log.i(sTag, enchantMessage(name) + "\n" + prettyJson(jsonString, '\n'));
        }
    }

    /**
     * Log INFO level json String in a pretty format. Which can be easily copied when monitor the log in Android Studio.
     *
     * @param name       short description for the json String.
     * @param jsonString
     */
    public static void jc(String name, String jsonString) {
        if (DEBUG) {
            Log.i(sTag, enchantMessage(name) + "\n" + prettyJson('\r' + jsonString, '\r'));
        }
    }

    private static String enchantMessage(String message) {
        StackTraceElement trace = Thread.currentThread().getStackTrace()[4];
        if (message == null || message.length() == 0) {
            return "(" + trace.getFileName() + ":" + trace.getLineNumber() + ")";
        } else {
            return "(" + trace.getFileName() + ":" + trace.getLineNumber() + ")" + INDENT + message;
        }
    }

    //TODO: when there's special characters (like: [{) in the JSON value, the format doesn't work correctly.
    private static String prettyJson(String jsonStr, char type) {
        String strType;
        if (type == '\r') {
            strType = "\n" + type;
        } else {
            strType = String.valueOf(type);
        }
        int level = 0;
        StringBuilder jsonForMatStr = new StringBuilder();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && type == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + strType);
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + strType);
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append(strType);
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuilder levelStr = new StringBuilder();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append(INDENT);
        }
        return levelStr.toString();
    }

}
