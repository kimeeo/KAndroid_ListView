package com.kimeeo.kAndroid.listViews.recyclerView.stickyRecyclerHeaders;

import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
abstract public class BaseStickyRecyclerViewAdapter extends BaseRecyclerViewAdapter implements StickyRecyclerHeadersAdapter<BaseItemHolder> {
    public BaseStickyRecyclerViewAdapter(DataProvider dataProvider)
    {
        super(dataProvider);
        setSupportLoader(false);
    }


    abstract protected View getStickyItemView(ViewGroup container);
    abstract protected BaseItemHolder getStickyItemHolder(View view);


    @Override
    public BaseItemHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = getStickyItemView(parent);
        return getStickyItemHolder(view);
    }


    public void onBindHeaderViewHolder(BaseItemHolder holder, int position)
    {
        Object item = getDataProvider().get(position);
        holder.updateItemView(item, position);
    }
}
