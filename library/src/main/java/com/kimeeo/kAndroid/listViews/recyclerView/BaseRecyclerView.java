/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kimeeo.kAndroid.listViews.recyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;

import com.kimeeo.kAndroid.listViews.BaseListDataView;
import com.kimeeo.kAndroid.listViews.EmptyViewHelper;
import com.kimeeo.kAndroid.listViews.ProgressItem;
import com.kimeeo.kAndroid.listViews.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


abstract public class BaseRecyclerView extends BaseListDataView implements AdapterView.OnItemClickListener,BaseRecyclerViewAdapter.OnUpdateItem{
    protected RecyclerView recyclerView;
    protected View mRootView;
    protected BaseRecyclerViewAdapter mAdapter;
    private EmptyViewHelper mEmptyViewHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public EmptyViewHelper getEmptyViewHelper() {
        return mEmptyViewHelper;
    }

    abstract protected RecyclerView.LayoutManager createLayoutManager();
    abstract protected BaseRecyclerViewAdapter createListViewAdapter();

    public View getRootView() {
        return mRootView;
    }
    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        if(mAdapter!=null)
            mAdapter.garbageCollectorCall();
        mAdapter =null;
        recyclerView = null;
        mSwipeRefreshLayout=null;

        if (mEmptyViewHelper != null)
            mEmptyViewHelper.clean();
        mEmptyViewHelper = null;
    }
    protected RecyclerView.ItemDecoration createItemDecoration() {return null;}
    protected RecyclerView getRecyclerView()
    {
        return recyclerView;
    }
    public BaseRecyclerViewAdapter getAdapter()
    {
        return mAdapter;
    }

    public void update(BaseItemHolder itemHolder, Object item, int position)
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        configViewParam();
        mRootView = createRootView(inflater, container, savedInstanceState);
        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mEmptyViewHelper = createEmptyViewHelper();
        recyclerView = createRecyclerView(mRootView);
        mAdapter = createListViewAdapter();
        mAdapter.setOnUpdateItem(this);

        RecyclerView.LayoutManager layoutManager= createLayoutManager();
        configLayoutManager(layoutManager);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration= createItemDecoration();
        if(itemDecoration!=null)
            recyclerView.addItemDecoration(itemDecoration);

        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        configRecyclerView(recyclerView, mAdapter);
        setOnScrollListener(recyclerView);
        setItemAnimator(recyclerView);
        next();
        onViewCreated(mRootView);
        return mRootView;
    }
    protected EmptyViewHelper createEmptyViewHelper() {
        return new EmptyViewHelper(getActivity(), createEmptyView(mRootView), this, showInternetError(), showInternetRetryButton());
    }
    public void onViewCreated(View view) {

    }

    public RecyclerView.ItemAnimator getItemAnimator() {
        return itemAnimator;
    }

    RecyclerView.ItemAnimator itemAnimator;
    protected void setItemAnimator(RecyclerView mList) {
        itemAnimator = createItemAnimator();
        if(itemAnimator!=null) {

            int itemAnimatorDuration = getItemAnimatorDuration();
            itemAnimator.setAddDuration(itemAnimatorDuration);
            itemAnimator.setChangeDuration(itemAnimatorDuration);
            itemAnimator.setMoveDuration(itemAnimatorDuration);
            itemAnimator.setRemoveDuration(itemAnimatorDuration);
            mList.setItemAnimator(itemAnimator);
        }
    }
    protected RecyclerView.ItemAnimator createItemAnimator()
    {
        //return new FadeInAnimator(new OvershootInterpolator(1f));
        //return new SlideInLeftAnimator();
        return null;
    }
    protected int getItemAnimatorDuration()
    {
        return  400;
    }
    //Confgi Your RecycleVIew Here
    protected void configRecyclerView(RecyclerView mList, BaseRecyclerViewAdapter mAdapter) {

    }
    //Confgi Your Layout manager here
    protected void  configLayoutManager(RecyclerView.LayoutManager layoutManager) {

    }
    protected void setOnScrollListener(RecyclerView mList) {
        EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener=new EndlessRecyclerOnScrollListener(mList.getLayoutManager()) {
            @Override
            public void onLoadMore() {
                if (getDataProvider().getCanLoadNext())
                    next();
            }
            @Override
            public void onScroll(RecyclerView recyclerView, int dx, int dy) {
                onDataScroll(recyclerView, dx, dy);
            }
        };
        mList.setOnScrollListener(endlessRecyclerOnScrollListener);
    }
    protected View createRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if(getDataProvider().getRefreshEnabled())
            return inflater.inflate(getRootRefreshLayoutResID(), container, false);
        else
            return inflater.inflate(getRootLayoutResID(), container, false);
    }
    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_recycler_with_swipe_refresh_layout;
    }
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_recycler;
    }
    @IdRes
    protected int getRecyclerViewResID() {
        return  R.id.recyclerView;
    }
    @IdRes
    protected int getEmptyViewResID() {
        return  R.id.emptyView;
    }
    @IdRes
    protected int getSwipeRefreshLayoutResID() {
        return  R.id.swipeRefreshLayout;
    }


    protected RecyclerView createRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(getRecyclerViewResID());
        return recyclerView;
    }
    protected View createEmptyView(View rootView) {
        View emptyView = rootView.findViewById(getEmptyViewResID());
        return emptyView;
    }
    protected void onDataScroll(RecyclerView recyclerView, int dx, int dy) {
    }
    protected SwipeRefreshLayout getSwipeRefreshLayout()
    {
        return mSwipeRefreshLayout;
    }
    protected void configSwipeRefreshLayout(SwipeRefreshLayout view) {
        mSwipeRefreshLayout = view;
        if(mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (getDataProvider().getCanLoadRefresh()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        refresh();
                    }
                    else
                        mSwipeRefreshLayout.setRefreshing(false);

                    mSwipeRefreshLayout.setEnabled(getDataProvider().getCanLoadRefresh());
                }
            });
            boolean refreshEnabled = getDataProvider().getCanLoadRefresh();
            mSwipeRefreshLayout.setEnabled(refreshEnabled);
            //mSwipeRefreshLayout.setColorSchemeColors(R.array.progressColors);
        }
    }
    protected SwipeRefreshLayout createSwipeRefreshLayout(View rootView) {
        if(rootView.findViewById(getSwipeRefreshLayoutResID())!=null) {
            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(getSwipeRefreshLayoutResID());
            return swipeRefreshLayout;
        }
        return null;
    }
    public void updateSwipeRefreshLayout(boolean isFetchingRefresh) {
        if(mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setRefreshing(false);

            if(isFetchingRefresh)
                mSwipeRefreshLayout.setEnabled(getDataProvider().getCanLoadRefresh());
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object data = getDataProvider().get(position);
        if(data!=null)
            onItemClick(data);
    }
    public void onItemClick(Object data) {

    }
    public void onFetchingStart(boolean isFetchingRefresh){
        if (getEmptyViewHelper() != null)
            getEmptyViewHelper().updatesStart();
    }

    public void onFetchingError(Object error){
        if (getEmptyViewHelper() != null)
            getEmptyViewHelper().updateView(getDataProvider());
        updateSwipeRefreshLayout(false);
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh){
        dataLoaded(dataList,isFetchingRefresh);
    }
    @Override
    public void onFetchingFinish(boolean isFetchingRefresh)
    {

    }
    public void itemsAdded(int index,List items){
        if (getEmptyViewHelper() != null)
            getEmptyViewHelper().updateView(getDataProvider());
    }

    public void itemsRemoved(final int index,final List items){

        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (recyclerView != null && recyclerView.getItemAnimator() != null) {
                    if (items.get(0) instanceof ProgressItem) {
                        recyclerView.getItemAnimator().setRemoveDuration(1);
                        recyclerView.getAdapter().notifyItemChanged(index);
                    } else
                        recyclerView.getItemAnimator().setRemoveDuration(getItemAnimatorDuration());
                }
            }
        };
        h.postDelayed(r, 50);

        if (getEmptyViewHelper() != null)
            getEmptyViewHelper().updateView(getDataProvider());
    }

    public void itemsChanged(int index,List items){
        if (getEmptyViewHelper() != null)
            getEmptyViewHelper().updateView(getDataProvider());
    }

    protected void dataLoaded(List<?> dataList, boolean isFetchingRefresh) {
        if (isFetchingRefresh)
            recyclerView.scrollToPosition(0);

        if (getEmptyViewHelper() != null)
            getEmptyViewHelper().updateView(getDataProvider());
        updateSwipeRefreshLayout(isFetchingRefresh);
    }


}
