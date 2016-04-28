package com.kimeeo.kAndroid.listViews.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bhavinpadhiyar on 1/21/16.
 */
public interface IViewProvider {
    View getItemView(int viewType, LayoutInflater inflater, ViewGroup container);
    BaseItemHolder getItemHolder(int viewType, View view);
    int getListItemViewType(int position, Object item);
}

