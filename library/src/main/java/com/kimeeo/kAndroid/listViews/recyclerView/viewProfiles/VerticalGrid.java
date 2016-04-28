package com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseProfileRecyclerView;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.DefaultRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.GridHelper;

/**
 * Created by bhavinpadhiyar on 1/12/16.
 */
abstract public class VerticalGrid extends BaseViewProfile implements GridHelper.IColoumProvider
{
    BaseRecyclerViewAdapter mAdaper;
    private GridHelper gridHelper;
    public RecyclerView.LayoutManager createLayoutManager(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getHost().getActivity(), getColumnsCount(), GridLayoutManager.VERTICAL, false);
        return gridLayoutManager;
    }
    public VerticalGrid(String name, BaseProfileRecyclerView host)
    {
        super(name,host);
    }
    public BaseRecyclerViewAdapter getAdapter()
    {
        return mAdaper;
    };
    public void configViewAdapter(BaseRecyclerViewAdapter value)
    {
        mAdaper = value;
    }
    public BaseRecyclerViewAdapter createListViewAdapter(){
        return new DefaultRecyclerViewAdapter(getHost().getDataProvider(), this);
    }
    public int getColumnsCount() {
        if(gridHelper!=null)
            return gridHelper.getColumnsCount();
        return getColumnsPhone();
    }
    public int getColumnsPhone() {return 2;}
    public int getColumnsTablet10() {
        return 5;
    }
    public int getColumnsTablet7() {return 4;}
    public int getSpanSizeForItem(int position,int viewType,Object baseObject)
    {
        return 1;
    }
    public void garbageCollectorCall() {
        super.garbageCollectorCall();
        if(gridHelper!=null)
            gridHelper.garbageCollectorCall();
        gridHelper=null;
    }
    public void configViewParam(){
        super.configViewParam();
        gridHelper = new GridHelper(this,getHost().getApplication());
    }
    public void configLayoutManager(RecyclerView.LayoutManager layoutManager){
        gridHelper.configLayoutManager(layoutManager);
    }
}