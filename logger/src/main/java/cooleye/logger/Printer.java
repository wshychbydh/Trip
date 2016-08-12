package cooleye.logger;

/**
 * Created by cool on 16-8-9.
 */
public class Printer {

    public static void printI(CharSequence cs) {
        System.out.println(cs);
    }

    public static void printE(CharSequence cs) {
        System.err.println(cs);
    }

    public static void formatI(String format, Object... args) {
        System.out.format(format, args);
    }

}
