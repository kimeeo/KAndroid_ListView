package com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseProfileRecyclerView;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.IViewProvider;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
abstract public class BaseViewProfile implements IViewProvider
{
    private String name;
    private BaseProfileRecyclerView host;
    abstract public RecyclerView.LayoutManager createLayoutManager();
    abstract public BaseRecyclerViewAdapter createListViewAdapter();
    abstract public BaseItemHolder getItemHolder(int viewType, View view);
    public void configViewParam(){

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BaseProfileRecyclerView getHost() {
        return host;
    }
    public BaseViewProfile(String name, BaseProfileRecyclerView host){
        this.name =name;
        this.host =host;
        configViewParam();
    }
    public void configViewAdapter(BaseRecyclerViewAdapter value){

    }
    public RecyclerView.ItemDecoration createItemDecoration(Activity context){
        return null;
    }
    public RecyclerView.ItemAnimator createItemAnimator()
    {
        return  null;
    }
    public int getItemAnimatorDuration()
    {
        return -1;
    }
    //Confgi Your RecycleVIew Here
    public void configRecyclerView(RecyclerView mList, BaseRecyclerViewAdapter mAdapter){}
    public void garbageCollectorCall() {
        host=null;
    }
    //Confgi Your Layout manager here
    public void  configLayoutManager(RecyclerView.LayoutManager layoutManager){
    }
}