package com.kimeeo.kAndroid.listViews.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
/**
 * Created by bhavinpadhiyar on 1/12/16.
 */
public class DefaultRecyclerViewAdapter extends BaseRecyclerViewAdapter
{
    IViewProvider viewProvider;
    public void garbageCollectorCall(){
        super.garbageCollectorCall();
        viewProvider=null;
    }
    public DefaultRecyclerViewAdapter(DataProvider dataManager,IViewProvider viewProvider) {
        super(dataManager);
        this.viewProvider = viewProvider;
    }
    public View getItemView(int viewType,LayoutInflater inflater,ViewGroup container){
        return viewProvider.getItemView(viewType, inflater, container);
    }
    protected BaseItemHolder getItemHolder(int viewType,View view){
        return  viewProvider.getItemHolder(viewType, view);
    }
    protected int getListItemViewType(int position,Object item){
        return viewProvider.getListItemViewType(position, item);
    }
    @Override
    protected boolean getSpanForItem(BaseItemHolder itemHolder, int position,int viewType)
    {
        return false;
    }
}