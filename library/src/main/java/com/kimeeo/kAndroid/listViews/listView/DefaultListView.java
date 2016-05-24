package com.kimeeo.kAndroid.listViews.listView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by bhavinpadhiyar on 1/20/16.
 */

abstract public class DefaultListView extends BaseListView implements IViewProvider
{
    @Override
    public int getTotalViewTypeCount() {
        return 1;
    }

    @Override
    public int getListItemViewType(int viewType, Object data) {
        return 1;
    }

    abstract public View getItemView(int viewType,LayoutInflater inflater,ViewGroup container);
    abstract public BaseItemHolder getItemHolder(int viewType,View view);
    protected BaseListViewAdapter createListViewAdapter(){
        return new DefaultListViewAdapter(getDataProvider(),this);
    }
}