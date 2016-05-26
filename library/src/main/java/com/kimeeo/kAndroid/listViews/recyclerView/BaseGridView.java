package com.kimeeo.kAndroid.listViews.recyclerView;

import android.support.v7.widget.RecyclerView;

/**
 * Created by bhavinpadhiyar on 7/20/15.
 */
abstract public class BaseGridView extends DefaultRecyclerView implements GridHelper.IColoumProvider
{
    private GridHelper gridHelper;
    public int getColumnsCount(){
        if(gridHelper!=null)
            return gridHelper.getColumnsCount();
        return getColumnsPhone();
    }
    public int getColumnsPhone() {
        return 2;
    }
    public int getColumnsTablet10() {
        return 5;
    }

    public int getColumnsTablet7() {
        return 3;
    }
    public int getSpanSizeForItem(int position,int viewType,Object baseObject)
    {
        return 1;
    }
    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        if(gridHelper!=null)
            gridHelper.garbageCollectorCall();
        gridHelper=null;
    }
    @Override
    protected void configViewParam(){
        super.configViewParam();
        gridHelper = new GridHelper(this,getActivity());
    }
    protected void configLayoutManager(RecyclerView.LayoutManager layoutManager){
        gridHelper.configLayoutManager(layoutManager);
    }
}
