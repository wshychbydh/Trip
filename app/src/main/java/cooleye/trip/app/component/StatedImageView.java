package cooleye.trip.app.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.widget.ImageView;

/**
 * When assigned a normal drawable, this class will add a pressed state automatically.
 * By default, a 50% transparent affect is applied to the pressed state. And you can customized
 * it by adding a PorterDuffColorFilter.
 */
public class StatedImageView extends ImageView {

    private static final PorterDuffColorFilter DEFAULT_COLOR_FILTER =
            new PorterDuffColorFilter(0x80FFFFFF, PorterDuff.Mode.MULTIPLY);

    private PorterDuffColorFilter mPressedColorFilter;

    public StatedImageView(Context context) {
        super(context);
    }

    public StatedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PorterDuffColorFilter getPressedColorFilter() {
        return mPressedColorFilter != null ? mPressedColorFilter : DEFAULT_COLOR_FILTER;
    }

    public void setPressedColorFilter(PorterDuffColorFilter pressedColorFilter) {
        mPressedColorFilter = pressedColorFilter;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (!drawable.isStateful()) {
            ButtonStateListDrawable stateDrawable = new ButtonStateListDrawable();
            stateDrawable.addState(StateSet.WILD_CARD, drawable);
            super.setImageDrawable(stateDrawable);
        } else {
            super.setImageDrawable(drawable);
        }
    }

    @Override
    public void setImageResource(int resId) {
        setImageDrawable(getResources().getDrawable(resId));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private class ButtonStateListDrawable extends StateListDrawable {

        @Override
        protected boolean onStateChange(int[] stateSet) {
            boolean hasPressedState = false;
            for (int state : stateSet) {
                if (state == android.R.attr.state_pressed) {
                    hasPressedState = true;
                    break;
                }
            }
            if (hasPressedState) {
                setColorFilter(getPressedColorFilter());
            } else {
                clearColorFilter();
            }
            return super.onStateChange(stateSet);
        }
    }
}
