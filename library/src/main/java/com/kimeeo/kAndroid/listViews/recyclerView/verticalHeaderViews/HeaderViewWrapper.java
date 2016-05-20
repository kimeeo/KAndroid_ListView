package com.kimeeo.kAndroid.listViews.recyclerView.verticalHeaderViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by bhavinpadhiyar on 2/2/16.
 */
public class HeaderViewWrapper extends FrameLayout {
    private int mOffset;


    public HeaderViewWrapper(Context context) {
        super(context);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
        super.dispatchDraw(canvas);
    }

    public void setClipY(int offset) {
        mOffset = offset;
        invalidate();
    }

}
