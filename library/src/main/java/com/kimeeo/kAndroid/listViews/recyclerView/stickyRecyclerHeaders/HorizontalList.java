package com.kimeeo.kAndroid.listViews.recyclerView.stickyRecyclerHeaders;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
abstract public class HorizontalList extends DefaultStickyHeaderView {
    protected RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        return linearLayoutManager;
    }
}
