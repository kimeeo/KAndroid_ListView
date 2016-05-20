package com.kimeeo.kAndroid.listViews.recyclerView.verticalHeaderViews;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by bhavinpadhiyar on 7/17/15.
 */
abstract public class ResponsiveView extends BaseHeaderGridView
{
    protected boolean getVariableHeight()
    {
        return true;
    }
    boolean canBeParallex;
    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        RecyclerView.LayoutManager layoutManager;
        int columns = getColumnsCount();

        if(columns==1)
        {
            canBeParallex=true;
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        else
        {
            if(getVariableHeight()) {
                canBeParallex = false;
                layoutManager = new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);
            }
            else {
                canBeParallex=true;
                layoutManager = new GridLayoutManager(getActivity(), columns, GridLayoutManager.VERTICAL, false);
            }
        }
        return layoutManager;
    }

    @Override
    final public boolean getSupportParallex()
    {
        if(!canBeParallex)
            return false;
        else
            return supportParallex();
    }

    public boolean supportParallex()
    {
        return false;
    }
}