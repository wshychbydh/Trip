package cooleye.utils.cache;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

import cooleye.utils.utils.DateUtil;

/**
 * Created by lenayan-x230s on 6/26/14.
 */
public class FileCache {

    private static final String LOG_TAG = "FileCache";

    public static final String IMAGE_CACHE_FLODER_SUFFIX = "/ImageCache";
    public static final String FILE_CACHE_FLODER_SUFFIX = "/FileCache";
    public static final String FILE_DOWNLOAD_FLODER_SUFFIX = "/files";

    private static final String FILE_CACHE_VERSION_CODE_KEY = "FILE_CACHE_VERSION_CODE_KEY";
    private static final String FILE_CACHE_VERSION_CODE = "2014-6-26";

    private static final byte[] LRU_LOCK = new byte[0];

    public static int REMOVE_CACHE_FILE_NOT_EXISTS = -1;
    public static int REMOVE_CACHE_SUCCESS = 1;
    public static int REMOVE_CACHE_FAILURE = 0;

    public static String sRoot;
    public static String sCacheRoot;
    public static String sImageCacheRootPath;
    public static String sFileCacheRootPath;
    public static String sDownLoadFileRootPath;
    private static FileCache sInstance = new FileCache();
    private LinkedList<String> mFileLRU = new LinkedList<String>();

    private FileCache() {
        //TODO  remove expired caches .
    }

    public static void install(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // SD-card available
            sRoot = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Android/data/" + context.getPackageName();
            sCacheRoot = sRoot + "/cache";
            File file = new File(sCacheRoot);
            if (!file.exists()) {
                if (!file.mkdirs())
                    sCacheRoot = context.getCacheDir().getAbsolutePath();
            }
        } else {
            sCacheRoot = context.getCacheDir().getAbsolutePath();
        }
        initImageCacheFile();
        initFileCacheFile();
        initDownLoadFilesPath();
    }

    private static void initImageCacheFile() {
        sImageCacheRootPath = FileCache.sCacheRoot + IMAGE_CACHE_FLODER_SUFFIX;
        File file = new File(sImageCacheRootPath);
        if (!file.exists()) {
            if (!file.mkdirs())
                sImageCacheRootPath = FileCache.sCacheRoot;
        }
    }

    private static void initFileCacheFile() {
        sFileCacheRootPath = FileCache.sCacheRoot + FILE_CACHE_FLODER_SUFFIX;
        File file = new File(sFileCacheRootPath);
        if (!file.exists()) {
            if (!file.mkdirs())
                sFileCacheRootPath = FileCache.sCacheRoot;
        }
    }

    private static void initDownLoadFilesPath() {
        sDownLoadFileRootPath = FileCache.sRoot + FILE_DOWNLOAD_FLODER_SUFFIX;
        File file = new File(sDownLoadFileRootPath);
        if (!file.exists()) {
            if (!file.mkdirs())
                sDownLoadFileRootPath = FileCache.sRoot;
        }
    }

    public static FileCache getsInstance() {
        return sInstance;
    }

    public void cacheCheck() {
        String versionCode = sInstance.get(FILE_CACHE_VERSION_CODE_KEY);
        if (versionCode == null || !versionCode.equals(FILE_CACHE_VERSION_CODE)) {
            cleanCacheUpdateVersion();
        }
    }

    public void cleanCacheUpdateVersion() {
        File file = new File(sFileCacheRootPath);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            put(FILE_CACHE_VERSION_CODE_KEY, FILE_CACHE_VERSION_CODE);
            return;
        }
        File tmpFile = null;
        for (int i = 0; i < files.length; i++) {
            tmpFile = new File(files[i].getAbsolutePath());
            FileUtil.delete(tmpFile);
            synchronized (LRU_LOCK) {
                mFileLRU.remove(tmpFile.getAbsolutePath());
            }
        }
        put(FILE_CACHE_VERSION_CODE_KEY, FILE_CACHE_VERSION_CODE);
    }

    public void clear() {
        clearFileCache();
        clearImageCache();
    }

    public void clearFileCache() {
        FileUtil.deleteChild(sFileCacheRootPath);
        synchronized (LRU_LOCK) {
            mFileLRU.clear();
        }
    }

    public void clearImageCache() {
        FileUtil.deleteChild(sImageCacheRootPath);
    }

    public synchronized <T> T get(String key) {
        File file = getCacheFile(key);
        if (file.exists()) {
            FileInputStream stream = null;
            ObjectInputStream objectStream = null;
            try {
                stream = new FileInputStream(file);
                objectStream = new ObjectInputStream(stream);
                @SuppressWarnings("unchecked")
                T value = (T) objectStream.readObject();

                // Update position within LRU.
                String filePath = file.getAbsolutePath();
                synchronized (mFileLRU) {
                    mFileLRU.remove(filePath);
                    mFileLRU.addLast(filePath);
                }

                return value;
            } catch (FileNotFoundException ex) {
                // Will not happen.
                Log.i(LOG_TAG, "File " + key + " not found");
            } catch (StreamCorruptedException ex) {
                remove(key);
                Log.i(LOG_TAG, "Get File " + key + " cast StreamCorruptedException");
            } catch (IOException ex) {
                remove(key);
                Log.i(LOG_TAG, "Get File " + key + " cast IOException");
            } catch (ClassNotFoundException ex) {
                // Will not happen.
                Log.i(LOG_TAG, "Get File " + key + " cast ClassNotFoundException");
            } finally {
                if (objectStream != null) {
                    try {
                        objectStream.close();
                    } catch (IOException ex) {
                        // Do nothing.
                        Log.i(LOG_TAG, "Get File " + key + " cast IOException");
                    }
                }
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ex) {
                        // Do nothing.
                        Log.i(LOG_TAG, "Get File " + key + " cast IOException");
                    }
                }
            }
        }
        return null;
    }

    public synchronized <T> T get(String key, T defaultValue) {
        T item = get(key);
        if (item == null) {
            return defaultValue;
        } else {
            return item;
        }
    }

    public synchronized <T> void put(String key, T value) {
        File file = getCacheFile(key);
        boolean fileExists = true;
        try {
            fileExists = !file.createNewFile();
        } catch (IOException ex) {
            // Will not happen.
        }

        FileOutputStream stream = null;
        ObjectOutputStream objectStream = null;
        try {
            stream = new FileOutputStream(file);
            objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(value);

            if (!fileExists) {
                mFileLRU.addLast(file.getAbsolutePath());
            }
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.i(LOG_TAG, ex.getMessage());
            }
            // In case this happens, just ignore it.
        } finally {
            if (objectStream != null) {
                try {
                    objectStream.close();
                } catch (IOException ex) {
                    Log.i(LOG_TAG, "Put File " + key + " cast IOException");
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Log.i(LOG_TAG, "Put File " + key + " cast IOException");
                }
            }
        }
    }

    public <T> void sysput(final String key, final T value) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    put(key, value);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "getLocalCurrencyCountryList UnknownException" + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };
        asyncTask.execute();
    }

    public int remove(String key) {
        if (!exists(key)) {
            return REMOVE_CACHE_FILE_NOT_EXISTS;
        }
        File file = getCacheFile(key);
        if (!FileUtil.delete(file)) {
            file.deleteOnExit();
        }
        synchronized (LRU_LOCK) {
            mFileLRU.remove(file.getAbsolutePath());
        }
        if (exists(key)) {
            return REMOVE_CACHE_FAILURE;
        }
        return REMOVE_CACHE_SUCCESS;
    }

    public boolean exists(String key) {
        return getCacheFile(key).exists();
    }

    public boolean isDownLoadFileexists(String key) {
        return new File(getValidFloderPath(sDownLoadFileRootPath), escapeFileName(key)).exists();
    }

    public String getDownLoadFileFullPath(String key) {
        return sDownLoadFileRootPath + "/" + key;
    }

    public File getCacheFile(String key) {
        return new File(getValidFloderPath(sFileCacheRootPath), escapeFileName(key));
    }

    public File getCacheFile(String key, String roortPath) {
        return new File(getValidFloderPath(roortPath), escapeFileName(key));
    }

    public File getCacheImageFile(String key) {
        return new File(getValidFloderPath(sImageCacheRootPath), escapeFileName(key));
    }

    public File getCacheImageFile(String key, String roortPath) {
        return new File(getValidFloderPath(roortPath), escapeFileName(key));
    }

    public String getValidFloderPath(String florderPath) {
        File file = new File(florderPath);
        if (!file.exists() && !file.mkdirs()) {
            Log.e(LOG_TAG, "Create floder falure.Key = " + florderPath);
        }
        return florderPath;
    }

    private String escapeFileName(String key) {
//        Encode file path if needed.
//        try {
//            return java.net.URLEncoder.encode(key, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return key.replace('/', '_');
    }

    public boolean isFileCacheExpired(String key, long cacheTime) {
        long lastModified = getFileLastModifiedTime(key);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        long period = calendar.getTime().getTime() - lastModified;
        return period > cacheTime;
    }

    public long getFileLastModifiedTime(String key) {
        File file = new File(sFileCacheRootPath, escapeFileName(key));
        if (file.exists()) {
            return file.lastModified();
        }
        return 0;
    }

    public void saveImageFile(final String fileName, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream out = null;
                try {
                    File imageFile = getCacheFile(fileName + ".jpg", sImageCacheRootPath);
                    out = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Save Image cast Exception : " + e.getMessage());
                } finally {
                    try {
                        if (out != null)
                            out.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Save Image cast IOException : " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    public String getImageFileCachePath(Context context, String fileName) {
        String path;
        if (isIntentAvailable(context, MediaStore.ACTION_IMAGE_CAPTURE)) {
            path = sImageCacheRootPath + "/" + fileName + DateUtil.formatDateStamp(System.currentTimeMillis())
                    + ".jpg";
        } else {
            path = sImageCacheRootPath + "/temp" + DateUtil.formatDateStamp(System.currentTimeMillis()) + ".jpg";
        }
        return "file:///" + path;
    }

    public String genImageFilePath() {
        return sImageCacheRootPath + "/" + DateUtil.formatDateStamp(System.currentTimeMillis()) + ".jpg";
    }

    private boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private static class CacheOutputStream extends FileOutputStream {
        private int length;

        private FileChannel channel;

        public CacheOutputStream(File file, int length) throws FileNotFoundException {
            super(file);
            this.length = length;
            this.channel = getChannel();
        }

        @Override
        public void write(byte[] buffer) throws IOException {
            super.write(buffer);
            if (channel.size() >= length) {
                close();
            }
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            super.write(buffer, offset, count);
            if (channel.size() >= length) {
                close();
            }
        }

        @Override
        public void write(int oneByte) throws IOException {
            super.write(oneByte);
            if (channel.size() >= length) {
                close();
            }
        }
    }

}
