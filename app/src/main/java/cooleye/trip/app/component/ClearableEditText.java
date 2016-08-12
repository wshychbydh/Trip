package cooleye.trip.app.component;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

import cooleye.trip.R;

/**
 * Created by lenayan on 14-4-16.
 */
public class ClearableEditText extends EditText {

    private int mTextCount = 0;
    private int mClearIconGravity = Gravity.TOP;
    private int mClearIconTop = 0;
    private int mClearIconLeft = 0;
    private int mClearIconWidth = 0;
    private int mClearIconHeight = 0;
    private int mClearIconPadding = 0;
    private float mLastX = -1;
    private float mLastY = -1;

    private Drawable mClearIconDrawable = null;

    private boolean mNeedInvalidate = true;
    private boolean mIsClearIconClipPadding = false;
    private boolean mIsClearIconDevision = true;
    private boolean mIsConfigChanged = false;

    private int mTextSize;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.ClearableEditText, defStyle, 0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.ClearableEditText_clearIconPadding) {
                setClearIconPadding((int) typedArray.getDimension(attr, 0));
            } else if (attr == R.styleable.ClearableEditText_clearIconDrawable) {
                Drawable drawable = typedArray.getDrawable(attr);
                if (drawable != null) {
                    mClearIconDrawable = drawable;
                }
            } else if (attr == R.styleable.ClearableEditText_clearIconGravity) {
                setClearIconGravity(typedArray.getInt(attr, -1));
            } else if (attr == R.styleable.ClearableEditText_clearIconClipParentPadding) {
                mIsClearIconClipPadding = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.ClearableEditText_clearIconDivision) {
                setClearIconDivision(typedArray.getBoolean(attr, true));
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mIsConfigChanged = true;
    }

    public void setClearIconDivision(boolean isClearIconDivision) {
        mIsClearIconDevision = isClearIconDivision;
    }

    public void setClearIconPadding(int clearIconPadding) {
        mClearIconPadding = clearIconPadding;
    }

    public void setClearIconGravity(int clearIconGravity) {
        if (clearIconGravity == -1)
            return;
        if (clearIconGravity != mClearIconGravity) {
            mNeedInvalidate = true;
            mClearIconGravity = clearIconGravity;
            invalidate();
        }
    }

    public void hideClear() {
        mTextCount = 0;
    }

    public void showClear(int size) {
        mTextCount = size;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mClearIconDrawable != null && isEnabled()) {
            final Bitmap bitmap = ((BitmapDrawable) mClearIconDrawable).getBitmap();
            mClearIconHeight = bitmap.getHeight();
            mClearIconWidth = bitmap.getWidth();
            if (mTextCount > 0) {
                if (mNeedInvalidate || mIsConfigChanged) {
                    mNeedInvalidate = false;
                    setupLeftTopOffset();
                }
                final int left = mClearIconLeft + getScrollX();
                final int top = mClearIconTop + getScrollY();
                final Paint paint = getPaint();
                canvas.drawBitmap(bitmap, left, top, paint);
            }
        }
    }

    private void setupLeftTopOffset() {
        int verticalGravity = mClearIconGravity & Gravity.VERTICAL_GRAVITY_MASK;
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        switch (verticalGravity) {
            case Gravity.TOP:
                topPadding += mClearIconPadding + mClearIconHeight - 15;
                mClearIconTop = (mIsClearIconClipPadding ? 0 : getPaddingTop()) + mClearIconPadding;
                break;
            case Gravity.BOTTOM:
                bottomPadding += mClearIconPadding + mClearIconHeight - 15;
                mClearIconTop = getBottom() - mClearIconHeight - (mIsClearIconClipPadding ? 0 : getPaddingBottom()) - mClearIconPadding - getTop();
                break;
            case Gravity.CENTER_VERTICAL:
                mClearIconTop = (getBottom() - getTop() - mClearIconHeight) / 2;
                break;
            default:
                mClearIconTop = mClearIconPadding;
                break;
        }
        int horizontalGravity = mClearIconGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (horizontalGravity) {
            case Gravity.LEFT:
                if (!mIsConfigChanged)
                    leftPadding += mClearIconPadding + mClearIconWidth;
                mClearIconLeft = (mIsClearIconClipPadding ? 0 : getPaddingLeft()) + mClearIconPadding;
                break;
            case Gravity.RIGHT:
                if (!mIsConfigChanged)
                    rightPadding += mClearIconPadding + mClearIconWidth;
                mClearIconLeft = getRight() - mClearIconWidth - (mIsClearIconClipPadding ? 0 : getPaddingRight()) - mClearIconPadding / 2 - getLeft();
                break;
            case Gravity.CENTER_HORIZONTAL:
                mClearIconLeft = (getRight() - getLeft() - mClearIconWidth) / 2;
                break;
            default:
                mClearIconLeft = mClearIconPadding;
                break;
        }
        if (mIsClearIconDevision)
            setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        mIsConfigChanged = false;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore,
                                 int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        mTextSize = text.length();
        mTextCount = text != null ? mTextSize : 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        final int left = mClearIconLeft - mClearIconPadding;
        final int top = mClearIconTop - mClearIconPadding;
        final int right = mClearIconLeft + mClearIconWidth + mClearIconPadding;
        final int bottom = mClearIconTop + mClearIconHeight + mClearIconPadding;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mTextCount > 0 && mClearIconDrawable != null && x > left && x < right && y < bottom && y > top) {
                    mLastX = event.getX();
                    mLastY = event.getY();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                if (mLastX != -1 && mLastY != -1) {
                    if (x > left && x < right && y < bottom && y > top) {
                        setText("");
                    }
                    mLastX = -1;
                    mLastY = -1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
