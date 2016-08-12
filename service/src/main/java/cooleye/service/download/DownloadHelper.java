package cooleye.service.download;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import cooleye.service.R;


/**
 * @cagetory DownlaodHelper
 * @author cool
 * @description APK下载帮助类
 */
public class DownloadHelper {

	public static final String DEFAULT_FOLDER = Environment.DIRECTORY_DOWNLOADS;
	public static final String APK_MIMETYPE = "application/vnd.android.package-archive";
	public static final String APK_SUFF = ".apk";

	private Activity mActivity;

	private String mUrl;
	private int mNotificationVisibility = Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;

	private CharSequence mTitle;
	private CharSequence mDescription;
	private String mMimeType;
	private int mAllowedNetworkTypes = Request.NETWORK_WIFI; // 默认仅在wifi下执行
	private boolean mIsVisibleInDownloadsUi = true;
	private boolean isApkFile = true;
	private String mFolder; // 下载文件存放文件夹
	private String mFileName; // 下载文件的文件名（带后缀）

	private DownloadManager mDownloadManager;
	private DownloadReceiver mReceiver;

	/**
	 * 
	 */
	public DownloadHelper(Activity activity, String url) {
		this.mActivity = activity;
		mDownloadManager = (DownloadManager) activity.getSystemService(Activity.DOWNLOAD_SERVICE);
		mUrl = url;
		mTitle = activity.getString(R.string.app_name);

		// 当在manifest中显示注册了DownloadReceiver。就不要调用这个方法。否则的话，根据你的情况进行调用。
		registerReceiver();
	}

	/**
	 * 当在manifest中显示注册了DownloadReceiver。就不要调用这个方法。否则的话，根据你的情况进行调用。
	 * 
	 */
	public void registerReceiver() {
		mReceiver = new DownloadReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
		intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		mActivity.getApplication().registerReceiver(mReceiver, intentFilter);
	}

	/**
	 * @param isApkFile
	 *            是否下载的是APK文件，默认为true
	 */
	public DownloadHelper setApkFile(boolean isApkFile) {
		this.isApkFile = isApkFile;
		return this;
	}

	/**
	 * @param fileName
	 *            //下载文件的文件名（带后缀）
	 */
	public DownloadHelper setFileName(String fileName) {
		this.mFileName = fileName;
		return this;
	}

	/**
	 * @param folder
	 *            下载文件存放文件夹
	 */
	public DownloadHelper setFolder(String folder) {
		this.mFolder = folder;
		return this;
	}

	/**
	 * @param allowedNetworkTypes
	 *            设置网络类型，默认仅在WiFi下下载。
	 */
	public DownloadHelper setAllowedNetworkTypes(int allowedNetworkTypes) {
		this.mAllowedNetworkTypes = allowedNetworkTypes;
		return this;
	}

	/**
	 * @param mimeType
	 *            设置下载文件的类型，默认为apk对应的mimetype
	 */
	public DownloadHelper setMimeType(String mimeType) {
		this.mMimeType = mimeType;
		return this;
	}

	/**
	 * @param notificationVisibility
	 *            the mNotificationVisibility to set
	 */
	public DownloadHelper setNotificationVisibility(int notificationVisibility) {
		this.mNotificationVisibility = notificationVisibility;
		return this;
	}

	/**
	 * @param isVisibleInDownloadsUi
	 *            the mIsVisibleInDownloadsUi to set
	 */
	public DownloadHelper setIsVisibleInDownloadsUi(boolean isVisibleInDownloadsUi) {
		this.mIsVisibleInDownloadsUi = isVisibleInDownloadsUi;
		return this;
	}

	/**
	 * @param title
	 *            the mTitle to set
	 */
	public DownloadHelper setTitle(CharSequence title) {
		this.mTitle = title;
		return this;
	}

	/**
	 * @param description
	 *            the mDescription to set
	 */
	public DownloadHelper setDescription(CharSequence description) {
		this.mDescription = description;
		return this;
	}

	public void start() {
		if (mActivity == null || mUrl == null) {
			return;
		}
		if (isApkFile) {
			File folder = new File(Environment.getExternalStorageDirectory(), getFolder());
			if (!folder.exists()) {
				folder.mkdirs();
			}
			mActivity.getCacheDir();
			File file = new File(folder, getFileName());
			if (file.exists()) {
				if (isDownload(file.getAbsolutePath())) {
					DownloadUtil.installApk(mActivity, Uri.fromFile(file));
					return;
				}
			}
			mDownloadManager.enqueue(getRequest(Uri.parse(mUrl)));
		}
	}

	private boolean isDownload(String fileName) {
		Query query = new Query();
		query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
		Cursor cursor = mDownloadManager.query(query);
		while (cursor.moveToNext()) {
			String localFileName = cursor.getString(cursor
					.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
			if (localFileName.equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	private Request getRequest(Uri uri) {
		Request request = new Request(uri);
		request.setAllowedNetworkTypes(mAllowedNetworkTypes)
				.setNotificationVisibility(mNotificationVisibility)
				.setVisibleInDownloadsUi(mIsVisibleInDownloadsUi)
				.setMimeType(
						mMimeType == null
								? isApkFile ? APK_MIMETYPE : "application/octet-stream"
								: mMimeType).setTitle(mTitle).setDescription(mDescription)
				.setDestinationInExternalPublicDir(getFolder(), getFileName())
				.allowScanningByMediaScanner();
		return request;
	}

	private String getFolder() {
		return mFolder == null ? DEFAULT_FOLDER : mFolder;
	}

	private String getFileName() {
		return mFileName == null ? mTitle + APK_SUFF : mFileName + APK_SUFF;
	}

	public void onDestory() {
		if (mReceiver != null) {
			mActivity.getApplication().unregisterReceiver(mReceiver);
		}
	}
}
