package cooleye.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cool on 16-6-3.
 * Above android 6.0, Permissions are not granted all the time,so you should check the permissions and request them if needed.
 */
public class PermissionHelper {

    private static final int REQUEST_CODE = 101;

    private Activity mActivity;
    private IPermissionCallback mPermissionCallback;
    private String[] mPermissions;

    public PermissionHelper(@NonNull Activity activity, @NonNull String[] permissions, IPermissionCallback callback) {
        this.mActivity = activity;
        this.mPermissions = permissions;
        this.mPermissionCallback = callback;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean result = checkPermissions();
            if (result) {
                if (mPermissionCallback != null) {
                    mPermissionCallback.onAllGranted();
                }
            } else {
                requestPermission();
            }
        }
    }

    private boolean checkPermissions() {
        boolean result = true;
        for (String permission : mPermissions) {
            result &= hasPermission(permission);
        }
        return result;
    }

    private void requestPermission() {
        String[] permissions = getRequetPermissions();
        if (permissions != null && permissions.length > 0) {
            //请求权限
            ActivityCompat.requestPermissions(mActivity, permissions,
                    REQUEST_CODE);
        }
    }

    private String[] getRequetPermissions() {
        List<String> requestPermission = new ArrayList<>();
        for (String permission : mPermissions) {
            if (!shouldShowRequestPermissionRationale(permission)) {
                requestPermission.add(permission);
            }
        }
        int size = requestPermission.size();
        if (size > 0) {
            String[] permissions = new String[size];
            for (int i = 0; i < size; i++) {
                permissions[i] = requestPermission.get(i);
            }
            return permissions;
        } else {
            return null;
        }
    }

    private boolean shouldShowRequestPermissionRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    //请求权限回调
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE && mPermissionCallback != null) {
            boolean allGranted = true;
            int len = permissions.length;
            for (int i = 0; i < len; i++) {
                boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                mPermissionCallback.onPermissionGranted(permissions[i], granted);
                allGranted &= granted;
            }
            if (allGranted) {
                mPermissionCallback.onAllGranted();
            }
        }
    }

    public interface IPermissionCallback {

        /**
         * @param permission 权限
         * @param isGranted  是否授权
         */
        void onPermissionGranted(String permission, boolean isGranted);

        /**
         * 当请求的权限用户都允许时
         */
        void onAllGranted();

    }
}
