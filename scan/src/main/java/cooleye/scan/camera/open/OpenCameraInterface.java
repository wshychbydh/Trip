/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cooleye.scan.camera.open;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;

public final class OpenCameraInterface {

    private static final String TAG = OpenCameraInterface.class.getName();

    public static Camera open(Context context) {
        Camera.CameraInfo cameraInfo = null;
        Camera camera;
        /**
         * 获取手机摄像头的总数
         */
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            Log.w(TAG, "No cameras!");
            return null;
        }
        /**
         * 获取后置摄像头的id(index)，一般情况下0为后置，1为前置
         */
        int index = 0;
        while (index < numCameras) {
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(index, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//判断是否为后置摄像头
                break;
            }
            index++;
        }

        if (index < numCameras) {
            Log.i(TAG, "Opening camera #" + index);
            camera = Camera.open(index);
        } else {
            Log.i(TAG, "No camera facing back; returning camera #0");
            camera = Camera.open(0);
        }
        setCameraDisplayOrientation((Activity) context, cameraInfo, camera);
        return camera;
    }

    public static void setCameraDisplayOrientation(Activity activity, Camera.CameraInfo info, Camera camera) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result = (info.orientation - degrees + 360) % 360;
        camera.setDisplayOrientation(result);
    }
}
