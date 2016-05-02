/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kimeeo.kAndroid.listViews.recyclerView.adapterLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.DefaultRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.IViewProvider;

abstract public class DefaultAdapterLayoutView extends BaseAdapterLayoutView implements IViewProvider
{
    abstract public View getItemView(int viewType,LayoutInflater inflater,ViewGroup container);
    abstract public BaseItemHolder getItemHolder(int viewType, View view);

    public int getListItemViewType(int position,Object item)
    {
        return BaseRecyclerViewAdapter.ViewTypes.VIEW_ITEM;
    }
    protected BaseRecyclerViewAdapter createListViewAdapter()
    {
        return new DefaultRecyclerViewAdapter(getDataProvider(),this);
    }
}
