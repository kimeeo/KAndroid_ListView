package com.kimeeo.kAndroid.listViews.recyclerView.verticalViews;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseGridView;

/**
 * Created by bhavinpadhiyar on 7/17/15.
 */
abstract public class GridView extends BaseGridView
{
    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getColumnsCount(), GridLayoutManager.VERTICAL, false);
        return gridLayoutManager;
    }
}