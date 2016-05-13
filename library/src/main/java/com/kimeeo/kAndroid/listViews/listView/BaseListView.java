package com.kimeeo.kAndroid.listViews.listView;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kimeeo.kAndroid.listViews.BaseListDataView;
import com.kimeeo.kAndroid.listViews.EmptyViewHelper;
import com.kimeeo.kAndroid.listViews.R;

import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
abstract public class BaseListView extends BaseListDataView implements AdapterView.OnItemClickListener,BaseListViewAdapter.OnUpdateItem{
    protected ListView mList;
    protected View mRootView;
    protected EmptyViewHelper mEmptyViewHelper;
    protected BaseListViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    abstract protected BaseListViewAdapter createListViewAdapter();
    public View getRootView() {
        return mRootView;
    }
    protected void garbageCollectorCall(){
        super.garbageCollectorCall();
        mList = null;
        if(mAdapter!=null)
            mAdapter.garbageCollectorCall();
        mAdapter =null;

        if (mEmptyViewHelper != null)
            mEmptyViewHelper.clean();
        mEmptyViewHelper = null;
        mSwipeRefreshLayout=null;
    }
    protected ListView getListView()
    {
        return mList;
    }
    public BaseListViewAdapter getAdapter()
    {
        return mAdapter;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        configViewParam();
        mRootView = createRootView(inflater, container, savedInstanceState);
        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mList = createListView(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();

        mAdapter = createListViewAdapter();
        mAdapter.setOnUpdateItem(this);
        mList.setOnItemClickListener(this);
        mList.setAdapter(mAdapter);
        configListView(mList, mAdapter);
        setOnScrollListener(mList);
        next();
        onViewCreated(mRootView);
        return mRootView;
    }
    public void update(BaseItemHolder itemHolder, Object item, int position)
    {

    }

    protected EmptyViewHelper createEmptyViewHelper() {
        return new EmptyViewHelper(getActivity(), createEmptyView(mRootView), this, showInternetError(), showInternetRetryButton());
    }
    public void onViewCreated(View view) {

    }
    //Confgi Your RecycleVIew Here
    protected void configListView(ListView mList,BaseListViewAdapter mAdapter){

    }
    protected void setOnScrollListener(ListView mList) {
        mList.setOnScrollListener(new EndlessListScrollListener() {
            @Override
            public void onLoadMore() {
                if(getDataProvider().getCanLoadNext())
                    next();
            }
            public void onScrolled(AbsListView view, int firstVisibleItem, int visibleItemCount,int totalItemCount)
            {
                //BUG
                onDataScroll((ListView)view, 0, 0);
            }
        });
    }
    protected View createRootView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView;
        if(getDataProvider().getRefreshEnabled())
            rootView = inflater.inflate(getRootRefreshLayoutResID(), container, false);
        else
            rootView = inflater.inflate(getRootLayoutResID(), container, false);
        return rootView;
    }

    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_list_view_with_swipe_refresh_layout;
    }
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_list_view;
    }
    @IdRes
    protected int getListViewResID() {
        return  R.id.listView;
    }
    @IdRes
    protected int getEmptyViewResID() {
        return  R.id.emptyView;
    }
    @IdRes
    protected int getSwipeRefreshLayoutResID() {
        return  R.id.swipeRefreshLayout;
    }


    protected ListView createListView(View rootView) {
        ListView recyclerView = (ListView) rootView.findViewById(getListViewResID());
        return recyclerView;
    }
    protected View createEmptyView(View rootView) {
        View emptyView = rootView.findViewById(getEmptyViewResID());
        return emptyView;
    }
    protected void onDataScroll(ListView listView, int dx, int dy) {

    }
    protected SwipeRefreshLayout getSwipeRefreshLayout()
    {
        return mSwipeRefreshLayout;
    }
    protected void configSwipeRefreshLayout(SwipeRefreshLayout view){
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
            boolean refreshEnabled = getDataProvider().getRefreshEnabled();
            mSwipeRefreshLayout.setEnabled(refreshEnabled);
            mSwipeRefreshLayout.setColorSchemeColors(R.array.progressColors);
        }
    }
    protected SwipeRefreshLayout createSwipeRefreshLayout(View rootView){
        if(rootView.findViewById(getSwipeRefreshLayoutResID())!=null) {
            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(getSwipeRefreshLayoutResID());
            return swipeRefreshLayout;
        }
        return null;
    }
    public void updateSwipeRefreshLayout(boolean isRefreshData){
        if(mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setRefreshing(false);

            if(isRefreshData)
                mSwipeRefreshLayout.setEnabled(getDataProvider().getCanLoadRefresh());
        }
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Object data = getDataProvider().get(position);
        onItemClick(data);
    }
    public void onItemClick(Object data){

    }
    public void onFetchingStart(boolean isFetchingRefresh){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updatesStart();
    };
    public void onFetchingError(Object error){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
        updateSwipeRefreshLayout(false);
    };
    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh){
        dataLoaded(dataList,isFetchingRefresh);
    }
    @Override
    public void onFetchingFinish(boolean isFetchingRefresh)
    {

    }
    public void itemsAdded(int index,List items){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
    };
    public void itemsRemoved(int index,List items){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
    };
    public void itemsChanged(int index,List items){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
    };
    protected void dataLoaded(List<?> dataList, boolean isFetchingRefresh) {
        if (isFetchingRefresh && mList!=null)
            mList.smoothScrollToPosition(0);
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
        updateSwipeRefreshLayout(isFetchingRefresh);
    }
}
