package test.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class ListSeparator extends RecyclerView.ItemDecoration {

    private final int height;
    private final Paint paint;

    public ListSeparator(Context context) {
        height = context.getResources().getDimensionPixelSize(R.dimen.separator_height);
        paint = new Paint();
        paint.setColor(0xfff1f1f1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (pos != parent.getAdapter().getItemCount()) {
            outRect.bottom = height;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();
        for (int i = 0; i < count - 1; i++) {
            View view = parent.getChildAt(i);
            c.drawRect(0, view.getBottom(), c.getWidth(), view.getBottom() + height, paint);
        }
    }
}
