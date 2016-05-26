package com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseProfileRecyclerView;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.DefaultRecyclerVerticleViewAdapter;
/**
 * Created by bhavinpadhiyar on 1/12/16.
 */
abstract public class VerticalList extends BaseViewProfile
{
    public VerticalList(String name, BaseProfileRecyclerView host)
    {
        super(name,host);
    }
    public RecyclerView.LayoutManager createLayoutManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getHost().getActivity(), LinearLayoutManager.VERTICAL, false);
        return linearLayoutManager;
    }
    public BaseRecyclerViewAdapter createListViewAdapter(){
        return new DefaultRecyclerVerticleViewAdapter(getHost().getDataProvider(), this);
    }
}
