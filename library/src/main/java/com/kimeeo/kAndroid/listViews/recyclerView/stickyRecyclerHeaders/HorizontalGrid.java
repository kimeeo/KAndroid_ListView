package com.kimeeo.kAndroid.listViews.recyclerView.stickyRecyclerHeaders;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
abstract public class HorizontalGrid extends VerticalGrid {
    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getColumnsCount(), GridLayoutManager.HORIZONTAL, false);
        return gridLayoutManager;
    }
}
