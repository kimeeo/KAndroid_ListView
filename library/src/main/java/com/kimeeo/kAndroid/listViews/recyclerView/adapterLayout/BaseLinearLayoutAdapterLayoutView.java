package com.kimeeo.kAndroid.listViews.recyclerView.adapterLayout;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.R;

/**
 * Created by bhavinpadhiyar on 2/16/16.
 */
abstract public class BaseLinearLayoutAdapterLayoutView extends DefaultAdapterLayoutView
{
    @Override
    protected View createRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(getDataProvider().getRefreshEnabled())
            return inflater.inflate(getRootRefreshLayoutResID(), container, false);
        else
            return inflater.inflate(getRootLayoutResID(), container, false);
    }

    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_vertical_linear_layout_adapter_view_with_swipe_refresh_layout;
    }
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_vertical_linear_layout_adapter_view;
    }
    @IdRes
    protected int getViewGroupResID() {
        return  R.id.viewGroup;
    }

    protected ViewGroup createViewGroup(View rootView)
    {
        ViewGroup view = (ViewGroup) rootView.findViewById(getViewGroupResID());
        return view;
    }
}
