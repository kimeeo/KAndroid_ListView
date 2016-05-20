package com.kimeeo.kAndroid.listViews.recyclerView.verticalHeaderViews;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.DefaultRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.IViewProvider;

/**
 * Created by BhavinPadhiyar on 20/05/16.
 */
public class DefaultHeaderRecyclerViewAdapter extends DefaultRecyclerViewAdapter {
    public DefaultHeaderRecyclerViewAdapter(DataProvider dataManager, IViewProvider viewProvider) {
        super(dataManager,viewProvider);
    }

    @Override
    protected boolean getSpanForItem(BaseItemHolder itemHolder, int position, int viewType)
    {
        return false;
    }

}
