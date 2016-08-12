package cooleye.trip.app.component;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cooleye.trip.R;


/**
 * Created by cool on 16-6-27.
 * //FIXME:布局基本没有问题了，但是还不够好，因为需要在xml里面设置高度，以后通过计算高度的方式来达到更好的实现方式。
 */
public class NumberLayout extends RelativeLayout {

    private TextView mNumberTextView;

    private TextView mContentTextView;
    private ImageView mImageView;

    private Drawable mDrawable;
    private int mNumber;
    private String mContent;

    public NumberLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public NumberLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            init(attrs, defStyle);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        Resources.Theme theme = getContext().getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.NumberLayout,
                defStyle, 0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.NumberLayout_drawable) {
                mDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.NumberLayout_content) {
                mContent = typedArray.getString(attr);
            }
        }
        typedArray.recycle();
        if (mDrawable != null) {
            initView();
        }
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
        if (mNumber > 0) {
            mNumberTextView.setVisibility(View.VISIBLE);
            mNumberTextView.setText(String.valueOf(number));
        } else {
            mNumberTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.number_button, this, true);
        mImageView = (ImageView) findViewById(R.id.img_icon);
        mNumberTextView = (TextView) findViewById(R.id.num);
        mContentTextView = (TextView) findViewById(R.id.name);
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        mImageView.setBackgroundDrawable(mDrawable);
        mContentTextView.setText(mContent);
    }
}
