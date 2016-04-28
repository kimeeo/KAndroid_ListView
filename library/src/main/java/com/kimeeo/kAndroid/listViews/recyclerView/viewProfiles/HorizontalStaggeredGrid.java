package com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseProfileRecyclerView;


/**
 * Created by bhavinpadhiyar on 1/12/16.
 */
abstract public class HorizontalStaggeredGrid extends VerticalGrid {
    public HorizontalStaggeredGrid(String name, BaseProfileRecyclerView host){
        super(name,host);
    }
    public RecyclerView.LayoutManager createLayoutManager() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getColumnsCount(), StaggeredGridLayoutManager.HORIZONTAL);
        return layoutManager;
    }
}
