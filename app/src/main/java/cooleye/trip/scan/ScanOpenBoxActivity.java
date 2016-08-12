package cooleye.trip.scan;

import com.google.zxing.BarcodeFormat;

import cooleye.scan.DecodeFormatManager;
import cooleye.utils.utils.ToastUtil;


/**
 * Created by Edward Gu on 16/3/17.
 */
public class ScanOpenBoxActivity extends CaptureActivity {

    public final static String FIRST_SCAN = "first_scan";

    private static final String QRCODE = "qrcode";

    @Override
    protected void handleResult(String content, BarcodeFormat barcodeFormat) {
        if (!DecodeFormatManager.QR_CODE_FORMATS.contains(barcodeFormat)) {
            //扫出来是条形码
            ToastUtil.show(this, "扫码失败");
            restartPreviewAfterDelay(2000);
            return;
        }
        if (!content.contains(QRCODE)) {
            //不符合速递易规则的二维码
            ToastUtil.show(this,  "无法识别的二维码");
            restartPreviewAfterDelay(2000);
            return;
        }
    }
}
