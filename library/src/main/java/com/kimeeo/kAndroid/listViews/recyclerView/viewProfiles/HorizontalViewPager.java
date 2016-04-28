package com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseProfileRecyclerView;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.DefaultRecyclerViewAdapter;
/**
 * Created by bhavinpadhiyar on 1/12/16.
 */
abstract public class HorizontalViewPager extends BaseViewProfile {
    public HorizontalViewPager(String name, BaseProfileRecyclerView host)
    {
        super(name,host);
    }
    public RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getHost().getActivity(), LinearLayoutManager.HORIZONTAL, false);
        return linearLayoutManager;
    }
    public BaseRecyclerViewAdapter createListViewAdapter(){
        return new DefaultRecyclerViewAdapter(getHost().getDataProvider(),this);
    }
}
