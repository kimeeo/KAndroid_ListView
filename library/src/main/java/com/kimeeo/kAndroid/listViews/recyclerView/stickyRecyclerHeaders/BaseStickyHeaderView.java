package com.kimeeo.kAndroid.listViews.recyclerView.stickyRecyclerHeaders;

import android.support.v7.widget.RecyclerView;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerView;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
abstract public class BaseStickyHeaderView extends BaseRecyclerView
{
    final public boolean isSupportLoader() {
        return false;
    }

    final public void setSupportLoader(boolean supportLoader) {}

    protected void configRecyclerView(RecyclerView recyclerView, BaseRecyclerViewAdapter mAdapter )
    {
        super.configRecyclerView(recyclerView, mAdapter);
        if(mAdapter instanceof StickyRecyclerHeadersAdapter)
        {
            StickyRecyclerHeadersAdapter adapter = (StickyRecyclerHeadersAdapter) mAdapter;
            mAdapter.setSupportLoader(isSupportLoader());
            final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
            recyclerView.addItemDecoration(headersDecor);
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headersDecor.invalidateHeaders();
                }
            });
        }
    }

}
