package test.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.italankin.slidinglayout.SlidingLayout;

public class MySlidingLayout extends SlidingLayout {

    public MySlidingLayout(Context context) {
        this(context, null);
    }

    public MySlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInterceptTouchEvents(false);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        View overlay = getOverlayView();
        int ty = (int) overlay.getTranslationY();
        if (ty > 0 && dy > 0) {
            startDrag();
        }
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getOverlayView().getTranslationY() > 0) {
            if (velocityY > 0) {
                showOverlay();
            } else {
                hideOverlay();
            }
            return true;
        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }
}
