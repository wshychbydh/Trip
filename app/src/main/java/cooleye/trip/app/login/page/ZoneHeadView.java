package cooleye.trip.app.login.page;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import cooleye.trip.R;


/**
 * Created by cool on 16-7-7.
 */
public class ZoneHeadView extends View {

    private final static int COLOR_OUTER = Color.WHITE;
    private final static int COLOR_INNER = Color.WHITE;
    /**
     * 以下常量是在密度为1的情况下的值。当密度不为1时，需要在此基础上变化。
     */
    private float mViewWidth = 80f;  //整个View的宽
    private float mViewHeight = 80f;  //整个View的高
    private float mRadiusOuter = 40f;  //外圆大小
    private float mRadiusInner = 35f;   //外圆大小
    private float mBorderWidthOuter = 0.5f;//框大小
    private float mBorderWidthInner = 1f; //内边框大小
    private float mIconSize = 68f; // 圆角图片大小
    private float mIconLeft = 6.5f;  //图片左边开始位置
    private float mIconTop = 6.5f;  //图片顶部开始未知
    private boolean mIsFillet = true; //传递的图片是否时圆角的，默认为true

    private Bitmap mBitmap;

    private Paint mPaint;

    public ZoneHeadView(Context context) {
        this(context, null);
    }

    public ZoneHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSize();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZoneHeadView);
            int count = typedArray.getIndexCount();
            Drawable drawable = null;
            for (int i = 0; i < count; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.ZoneHeadView_src) {
                    drawable = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.ZoneHeadView_isFillet) {
                    mIsFillet = typedArray.getBoolean(0, true);
                }
            }
            if (drawable != null) {
                updateBitmap(drawable);
            }
            typedArray.recycle();
        }
    }

    private void initSize() {
        float density = getResources().getDisplayMetrics().density;
        mViewWidth *= density;
        mViewHeight *= density;
        mRadiusOuter *= density;
        mRadiusInner *= density;
        mBorderWidthOuter *= density;
        mBorderWidthInner *= density;
        mIconSize *= density;
        mIconLeft *= density;
        mIconTop *= density;
    }

    public void setDrawable(@NonNull Drawable drawable, boolean isCircleDrawable) {
        this.mIsFillet = isCircleDrawable;
        updateBitmap(drawable);
    }

    public void setImageResouce(@DrawableRes int res, boolean isCircleDrawable) {
        setDrawable(getResources().getDrawable(res), isCircleDrawable);
    }

    private void updateBitmap(Drawable drawable) {
        mBitmap = createFixedSizeImage(((BitmapDrawable) drawable).getBitmap());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) mViewWidth, (int) mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            mPaint.setStrokeWidth(mBorderWidthOuter);
            mPaint.setColor(COLOR_OUTER);
            canvas.drawCircle(mRadiusOuter, mRadiusOuter, mRadiusOuter, mPaint);
            mPaint.setColor(COLOR_INNER);
            mPaint.setStrokeWidth(mBorderWidthInner);
            canvas.drawCircle(mRadiusOuter, mRadiusOuter, mRadiusInner, mPaint);
            canvas.drawBitmap(mBitmap, mIconLeft, mIconTop, new Paint());
        }
    }

    /**
     * 创建固定大小的bitmap。如果图片不是圆角，则圆角图片
     *
     * @param source
     * @return
     */
    private Bitmap createFixedSizeImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap((int) mIconSize, (int) mIconSize, Bitmap.Config
                .ARGB_8888);
        Canvas canvas = new Canvas(target);
        if (!mIsFillet) {
            float size = mIconSize / 2;
            canvas.drawCircle(size, size, size, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }
        canvas.drawBitmap(scaleBitmap(source), 0, 0, paint);
        return target;
    }

    /**
     * 缩放bitmap
     * @param bitmap
     * @return
     */
    private Bitmap scaleBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        float scale = mIconSize / minSize;
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight
                (), matrix, true);
        return resizeBmp;
    }
}
