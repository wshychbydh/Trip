package cooleye.trip.scan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cooleye.trip.R;

public class CaptureMainActivity extends Activity{

    private TextView resultTv;
    private ImageView resultIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_main);
        resultTv = (TextView) findViewById(R.id.tv_result);
        resultIv = (ImageView)findViewById(R.id.iv_result);
    }

    public void onClick(View v) {
        startActivityForResult(new Intent(CaptureMainActivity.this, CaptureActivity.class), 0);
    }

    public void encode(View v) {
        startActivity(new Intent(CaptureMainActivity.this, QrEncodeActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            resultTv.setText(data.getStringExtra(CaptureActivity.EXTRA_RESULT));
            resultIv.setImageBitmap((Bitmap)data.getParcelableExtra(CaptureActivity.EXTRA_BITMAP));
        } else {
            resultTv.setText("");
            resultIv.setImageDrawable(null);
        }
    }
}
