package cooleye.utils.cache;

import android.util.Log;

import java.io.File;

/**
 * Created by lenayan on 8/8/14.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static boolean delete(String fileName) {
        return delete(new File(fileName));
    }

    public static boolean delete(File file) {
        if (!file.exists()) {
            Log.i(TAG, "Delete " + file.getPath() + " Fail.");
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(file.getPath());
            } else {
                return deleteDirectory(file.getPath(), true);
            }
        }
    }

    public static boolean deleteChild(String fileName) {
        return deleteChild(new File(fileName));
    }

    public static boolean deleteChild(File file) {
        if (!file.exists()) {
            Log.i(TAG, "Delete Child " + file.getPath() + " Fail.");
            return false;
        } else {
            if (file.isFile()) {
                Log.i(TAG, "Delete Child in" + file.getPath() + " Fail," + file.getPath() + " is not a directory.");
                return false;
            } else {
                return deleteDirectory(file.getPath(), false);
            }
        }
    }

    public static boolean deleteFile(String fileName) {
        return deleteFile(new File(fileName));
    }

    public static boolean deleteFile(File file) {
        if (file.isFile() && file.exists()) {
            return file.delete();
        } else {
            Log.i(TAG, "Delete " + file.getPath() + " Fail");
            return false;
        }
    }

    public static boolean deleteDirectory(String dir, boolean deleteSelf) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        return deleteDirectory(new File(dir), deleteSelf);
    }

    public static boolean deleteDirectory(File dirFile, boolean deleteSelf) {
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            Log.i(TAG, "Derectory" + dirFile.getPath() + " not exist");
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath(), true);
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            Log.i(TAG, "Delete Derectory " + dirFile.getPath() + " Fail");
            return false;
        }
        if (deleteSelf) {
            if (dirFile.delete()) {
                return true;
            } else {
                Log.i(TAG, "Delete Derectory " + dirFile.getPath() + " Fail");
                return false;
            }
        }
        return true;
    }
}
