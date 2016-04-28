package com.kimeeo.kAndroid.listViews.recyclerView.verticalViews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kimeeo.kAndroid.listViews.recyclerView.DefaultRecyclerView;

/**
 * Created by bhavinpadhiyar on 7/17/15.
 */
abstract public class ListView extends DefaultRecyclerView
{
    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        return linearLayoutManager;
    }
}
