package com.kimeeo.kAndroid.listViews.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;

/**
 * Created by bhavinpadhiyar on 1/12/16.
 */
public class DefaultRecyclerVerticleViewAdapter extends DefaultRecyclerViewAdapter {
    public DefaultRecyclerVerticleViewAdapter(DataProvider dataManager, IViewProvider viewProvider) {
        super(dataManager, viewProvider);
    }

    protected View getProgressItem(int viewType, LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout._fragment_recycler_horizontal_center_progress_item, container, false);
    }
}