/*
 * Copyright (C) 2008 ZXing authors
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

package cooleye.trip.scan;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

import java.io.IOException;

import cooleye.logger.Logger;
import cooleye.scan.AmbientLightManager;
import cooleye.scan.BeepManager;
import cooleye.scan.FinishListener;
import cooleye.scan.InactivityTimer;
import cooleye.scan.camera.CameraManager;
import cooleye.trip.R;
import cooleye.trip.app.base.BaseActivity;
import cooleye.utils.utils.ScreenUtil;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public abstract class CaptureActivity extends BaseActivity {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private CameraManager mCameraManager;
    private CaptureActivityHandler mCaptureActivityHandler;
    private Result mSavedResultToShow;
    private ViewfinderView mViewfinderView;
    private InactivityTimer mInactivityTimer;
    private BeepManager mBeepManager;
    private AmbientLightManager mAmbientLightManager;

    private boolean mInitialized;

    public int getOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }


    ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    public Handler getCaptureActivityHandler() {
        return mCaptureActivityHandler;
    }

    CameraManager getCameraManager() {
        return mCameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan);

        mInactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mInactivityTimer.onResume();
        mCaptureActivityHandler = null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !mInitialized) {
            if (mBeepManager == null) mBeepManager = new BeepManager(this);
            if (mAmbientLightManager == null) mAmbientLightManager = new AmbientLightManager(this);

            mCameraManager = new CameraManager(CaptureActivity.this, ScreenUtil.sScreenWidth);
            mCameraManager.setStatusBarHeight(ScreenUtil.getStatusHeight(this));

            mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
            mViewfinderView.setCameraManager(mCameraManager);

            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();

            // surface is already created onWindowFocusChanged;
            initCamera(surfaceHolder);

            mBeepManager.updatePrefs();
            mAmbientLightManager.start(mCameraManager);
            mCaptureActivityHandler = new CaptureActivityHandler(CaptureActivity.this,
                    null, null, null, mCameraManager);
            mInitialized = true;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mCaptureActivityHandler != null) {
            mCaptureActivityHandler.quitSynchronously();
            mCaptureActivityHandler = null;
        }
        mInitialized = false;
        mInactivityTimer.onPause();
        mAmbientLightManager.stop();
        mBeepManager.close();
        mCameraManager.closeDriver();
    }

    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;
            // Use volume up/down to turn on light
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mCameraManager.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                mCameraManager.setTorch(true);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (mCaptureActivityHandler == null) {
            mSavedResultToShow = result;
        } else {
            if (result != null) {
                mSavedResultToShow = result;
            }
            if (mSavedResultToShow != null) {
                Message message = Message.obtain(mCaptureActivityHandler, R.id.decode_succeeded, mSavedResultToShow);
                mCaptureActivityHandler.sendMessage(message);
            }
            mSavedResultToShow = null;
        }
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        mInactivityTimer.onActivity();
        Logger.i("rawResult :" + rawResult);
        ParsedResult parsedResult = ResultParser.parseResult(rawResult);
        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            mBeepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, scaleFactor, rawResult);
        }
        String displayContents = getDisplayContents(parsedResult);
        if (TextUtils.isEmpty(displayContents)) {
            restartPreviewAfterDelay(2000);
            return;
        }
        handleResult(displayContents, rawResult.getBarcodeFormat());
    }

    // Handle result in subclasses
    protected abstract void handleResult(String content, BarcodeFormat barcodeFormat);

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of
     * the barcode.
     *
     * @param barcode     A bitmap of the captured image.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param rawResult   The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.result_points));
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4
                    && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                // Hacky special case -- draw two lines, for the barcode and
                // metadata
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(), scaleFactor * b.getX(),
                    scaleFactor * b.getY(), paint);
        }
    }

    public String getDisplayContents(ParsedResult parsedResult) {
        String contents = parsedResult.getDisplayResult();
        return contents.replace("\r", "");
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (mCameraManager.isOpen()) {
            Logger.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            mCameraManager.openDriver(surfaceHolder, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Logger.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Logger.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.tips));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (mCaptureActivityHandler != null) {
            mCaptureActivityHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }
}
