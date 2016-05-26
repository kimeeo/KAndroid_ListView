package com.kimeeo.kAndroid.listViews.recyclerView.verticalViews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kimeeo.kAndroid.listViews.recyclerView.DefaultVerticleRecyclerView;

/**
 * Created by bhavinpadhiyar on 7/17/15.
 */
abstract public class ListView extends DefaultVerticleRecyclerView
{
    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        return linearLayoutManager;
    }
}
