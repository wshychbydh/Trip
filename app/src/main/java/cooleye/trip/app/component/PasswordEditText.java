package cooleye.trip.app.component;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cooleye.trip.R;

/**
 * Created by cool on 15-6-16.
 * <p/>
 * 控制密码可见性
 * <p/>
 * FIXME:1、显示/隐藏密码时输入法总是会弹出的bug；2、当密码为中文切可见时，第一次点击隐藏不起作用。
 */
public class PasswordEditText extends EditText implements TextWatcher {

    private final static int DRAWABLE_MAX_WIDTH = 80;
    private final static int DRAWABLE_MAX_HEIGHT = 80;

    private int mGravity = Gravity.CENTER_VERTICAL;
    private int mTop = 0;
    private int mLeft = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mPadding = 0;

    private Bitmap mShowBitmap = null;
    private Bitmap mHideBitmap = null;

    private boolean mIconTouched = false; //显示/隐藏的图片是否正处于点击中

    private boolean mNeedInvalidate = true; //是否需要刷新界面
    private boolean mIsClipPadding = false;
    private boolean mIsDivision = true;  //是否分割文本和图标
    private boolean mIsConfigChanged = false;

    private boolean mPasswordVisible = false;

    private TransformationMethod mShowMethod;
    private TransformationMethod mHideMethod;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPasswordVisible = isVisiblePasswordInputType(getInputType());
        mShowMethod = HideReturnsTransformationMethod.getInstance();
        mHideMethod = PasswordTransformationMethod.getInstance();
        setTransformationMethod(mPasswordVisible ? mShowMethod : mHideMethod);
        addTextChangedListener(this);
        if (attrs != null) {
            initDrawInfo(attrs, defStyle);
        }
    }

    private void initDrawInfo(AttributeSet attrs, int defStyle) {
        Resources.Theme theme = getContext().getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.PasswordEditText,
                defStyle, 0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.PasswordEditText_padding) {
                setPadding((int) typedArray.getDimension(attr, 0));
            } else if (attr == R.styleable.PasswordEditText_showDrawable) {
                Drawable showDrawable = typedArray.getDrawable(attr);
                mShowBitmap = showDrawable == null ? null : ((BitmapDrawable) showDrawable)
                        .getBitmap();
            } else if (attr == R.styleable.PasswordEditText_hideDrawable) {
                Drawable hideDrawable = typedArray.getDrawable(attr);
                mHideBitmap = hideDrawable == null ? null : ((BitmapDrawable) hideDrawable)
                        .getBitmap();
            } else if (attr == R.styleable.PasswordEditText_gravity) {
                setDrawableGravity(typedArray.getInt(attr, -1));
            } else if (attr == R.styleable.PasswordEditText_clipParentPadding) {
                mIsClipPadding = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.PasswordEditText_division) {
                setDivision(typedArray.getBoolean(attr, true));
            }
        }
        typedArray.recycle();
        build();
    }

    private void build() {
        if (bitmapAvailable()) {
            buildBitmap();
            setupLeftTopOffset();
        }
    }

    private void buildBitmap() {
        int maxWidth = mShowBitmap.getWidth() > mHideBitmap.getWidth() ?
                mShowBitmap.getWidth() : mHideBitmap.getWidth();
        int maxHeight = mShowBitmap.getHeight() > mHideBitmap.getHeight() ?
                mShowBitmap.getHeight() : mHideBitmap.getHeight();
        mWidth = maxWidth > DRAWABLE_MAX_WIDTH ?
                DRAWABLE_MAX_WIDTH : maxWidth;
        mHeight = maxHeight > DRAWABLE_MAX_HEIGHT ?
                DRAWABLE_MAX_HEIGHT : maxHeight;
        mShowBitmap = Bitmap.createScaledBitmap(mShowBitmap, mWidth, mHeight, true);
        mHideBitmap = Bitmap.createScaledBitmap(mHideBitmap, mWidth, mHeight, true);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mIsConfigChanged = true;
    }


    public PasswordEditText setDrawable(@NonNull Drawable showDrawable, @NonNull Drawable
            hideDrawable) {
        mShowBitmap = ((BitmapDrawable) showDrawable).getBitmap();
        mHideBitmap = ((BitmapDrawable) hideDrawable).getBitmap();
        buildBitmap();
        return this;
    }

    public PasswordEditText setDivision(boolean isDivision) {
        mIsDivision = isDivision;
        mNeedInvalidate = true;
        return this;
    }

    public PasswordEditText setPadding(int padding) {
        mPadding = padding;
        mNeedInvalidate = true;
        return this;
    }

    public PasswordEditText setDrawableGravity(int gravity) {
        if (gravity != -1) {
            if (gravity != mGravity) {
                mGravity = gravity;
                mNeedInvalidate = true;
            }
        }
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmapAvailable()) {
            if (mNeedInvalidate || mIsConfigChanged) {
                mNeedInvalidate = false;
                setupLeftTopOffset();
            }
            canvas.drawBitmap(mPasswordVisible ? mShowBitmap : mHideBitmap,
                    mLeft + getScrollX(), mTop + getScrollY(), getPaint());
        }
    }

    private boolean bitmapAvailable() {
        return mShowBitmap != null && mHideBitmap != null;
    }

    private void setupLeftTopOffset() {
        int verticalGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        switch (verticalGravity) {
            case Gravity.TOP:
                topPadding += mPadding + mHeight - 15;
                mTop = (mIsClipPadding ? 0 : getPaddingTop()) + mPadding;
                break;
            case Gravity.BOTTOM:
                bottomPadding += mPadding + mHeight - 15;
                mTop = getBottom() - mHeight - (mIsClipPadding ? 0 : getPaddingBottom()) -
                        mPadding - getTop();
                break;
            case Gravity.CENTER_VERTICAL:
                mTop = (getBottom() - getTop() - mHeight) / 2;
                break;
            default:
                mTop = mPadding;
                break;
        }
        int horizontalGravity = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (horizontalGravity) {
            case Gravity.LEFT:
                if (!mIsConfigChanged)
                    leftPadding += mPadding + mWidth;
                mLeft = (mIsClipPadding ? 0 : getPaddingLeft()) + mPadding;
                break;
            case Gravity.RIGHT:
                if (!mIsConfigChanged)
                    rightPadding += mPadding + mWidth;
                mLeft = getRight() - mWidth - (mIsClipPadding ? 0 : getPaddingRight()) - mPadding
                        / 2 - getLeft();
                break;
            case Gravity.CENTER_HORIZONTAL:
                mLeft = (getRight() - getLeft() - mWidth) / 2;
                break;
            default:
                mLeft = mPadding;
                break;
        }
        if (mIsDivision)
            setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        mIsConfigChanged = false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence ss, int start, int before, int count) {
        String editable = getText().toString();
        String str = passwordFilter(editable);
        if (!editable.equals(str)) {
            setText(str);
            //设置新的光标所在位置
            setSelection(str.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (bitmapAvailable()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (isPointInRect(event.getX(), event.getY())) {
                        mIconTouched = true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mIconTouched = false;
                    break;
                case MotionEvent.ACTION_UP:
                    if (mIconTouched && isPointInRect(event.getX(), event.getY())) {
                        mPasswordVisible = !mPasswordVisible;
                        setTransformationMethod(mPasswordVisible ? mShowMethod : mHideMethod);
                    }
                    mIconTouched = false;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean isPointInRect(float x, float y) {
        return x > mLeft && x < mLeft + mWidth && y > mTop && y < mTop + mHeight;
    }

    private boolean isVisiblePasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    private String passwordFilter(@NonNull String password) {
        // 只允许字母、数字和汉字
        String regEx = "[^a-zA-Z0-9._]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(password);
        return m.replaceAll("").trim();
    }
}
