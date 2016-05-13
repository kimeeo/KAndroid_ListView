package com.kimeeo.kAndroid.listViews.pager.viewPager;

import android.support.annotation.LayoutRes;

import com.kimeeo.kAndroid.listViews.R;


/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
abstract public class VerticalViewPager extends DefaltViewPager {

    @Override
    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_vertical_page_view_with_swipe_refresh_layout;
    }
    @Override
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_vertical_page_view;
    }

}