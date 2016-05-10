package com.kimeeo.kAndroid.listViews.pager.viewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kimeeo.kAndroid.listViews.R;


/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
abstract public class VerticalViewPager extends DefaltViewPager {

    protected View createRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        if(getDataProvider().getRefreshEnabled())
            rootView= inflater.inflate(R.layout._fragment_vertical_page_view_with_swipe_refresh_layout, container, false);
        else
            rootView= inflater.inflate(R.layout._fragment_vertical_page_view, container, false);
        return rootView;
    }
}