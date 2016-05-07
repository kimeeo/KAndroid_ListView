package com.kimeeo.kAndroid.listViews.recyclerView.stickyRecyclerHeaders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.IViewProvider;
/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
public class DefaultStickyRecyclerViewAdapter extends BaseStickyRecyclerViewAdapter {

    IViewProvider viewProvider;

    public void garbageCollectorCall() {
        super.garbageCollectorCall();
        viewProvider = null;
    }

    protected View getStickyItemView(ViewGroup container)
    {
        return stickyViewProvider.getStickyItemView(container);
    }
    protected BaseItemHolder getStickyItemHolder(View view)
    {
        return stickyViewProvider.getStickyItemHolder(view);
    }
    public long getHeaderId(int position)
    {
        return stickyViewProvider.getHeaderId(position);
    }

    IStickyViewProvider stickyViewProvider;
    public DefaultStickyRecyclerViewAdapter(DataProvider dataProvider, IViewProvider viewProvider, IStickyViewProvider stickyViewProvider) {
        super(dataProvider);
        this.viewProvider = viewProvider;
        this.stickyViewProvider=stickyViewProvider;
    }

    public View getItemView(int viewType, LayoutInflater inflater, ViewGroup container) {
        return viewProvider.getItemView(viewType, inflater, container);
    }

    protected BaseItemHolder getItemHolder(int viewType, View view) {
        return viewProvider.getItemHolder(viewType, view);
    }

    protected int getListItemViewType(int position, Object item) {
        return viewProvider.getListItemViewType(position, item);
    }
}
