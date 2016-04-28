package com.kimeeo.kAndroid.listViews;

import android.graphics.drawable.Drawable;

/**
 * Created by BhavinPadhiyar on 28/04/16.
 */
public interface IEmptyViewHandler {
    Drawable getEmptyViewDrawable();
    String getEmptyViewMessage();
    Drawable getInternetViewDrawable();
    String getInternetViewMessage();
    void retry();
}
