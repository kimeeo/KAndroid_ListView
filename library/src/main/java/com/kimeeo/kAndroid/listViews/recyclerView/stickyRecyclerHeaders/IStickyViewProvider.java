package com.kimeeo.kAndroid.listViews.recyclerView.stickyRecyclerHeaders;

import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;

/**
 * Created by bhavinpadhiyar on 1/21/16.
 */
public interface IStickyViewProvider {
    View getStickyItemView(ViewGroup container);
    BaseItemHolder getStickyItemHolder(View view);
    long getHeaderId(int position);
}

