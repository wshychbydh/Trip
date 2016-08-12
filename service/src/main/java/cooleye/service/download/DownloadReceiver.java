package cooleye.service.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * @cagetory Snippet
 * @2015-12-4
 * @author cooleye
 * @description TODO
 */
public class DownloadReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context.getApplicationContext(), intent.getAction(), Toast.LENGTH_SHORT)
				.show();
		long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
			installApk(context, reference);
		} else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
			toDownloadPager(context);
		}
	}

	private void installApk(Context context, long id) {
		DownloadManager manager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = manager.getUriForDownloadedFile(id);
		DownloadUtil.installApk(context, uri);
	}

	/**
	 * *
	 * <p>
	 * column types are
	 * <ul>
	 * <li>{@link #FIELD_TYPE_NULL}</li>
	 * <li>{@link #FIELD_TYPE_INTEGER}</li>
	 * <li>{@link #FIELD_TYPE_FLOAT}</li>
	 * <li>{@link #FIELD_TYPE_STRING}</li>
	 * <li>{@link #FIELD_TYPE_BLOB}</li>
	 * </ul>
	 * </p>
	 * 
	 * @param cursor
	 * @param type
	 * @return
	 *//*
	public Object getValue(Cursor cursor, int columnIndex) {
		switch (cursor.getType(columnIndex)) {
			case Cursor.FIELD_TYPE_INTEGER :
				return cursor.getInt(columnIndex);
			case Cursor.FIELD_TYPE_FLOAT :
				return cursor.getFloat(columnIndex);
			case Cursor.FIELD_TYPE_STRING :
				return cursor.getString(columnIndex);
			case Cursor.FIELD_TYPE_BLOB :
				return String.valueOf(cursor.getBlob(columnIndex));
			default :
				return "NULL";
		}
	}*/

	/**
	 * 下载管理界面
	 * 
	 * @param context
	 */
	private void toDownloadPager(Context context) {
		Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
