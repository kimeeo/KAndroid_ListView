package com.kimeeo.kAndroid.listViews.pager.viewPager;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.kimeeo.kAndroid.listViews.pager.BaseItemHolder;

/**
 * Created by bhavinpadhiyar on 1/21/16.
 */
public interface IViewProvider {

    BaseItemHolder getItemHolder(View view, int position, Object data);
    ViewPager getViewPager();
    View getView(int position, Object data);
    void removeView(View view, int position, BaseItemHolder itemHolder);
    String getItemTitle(int position, Object navigationObject);
}
