package com.kimeeo.kAndroid.listViews.viewHelper;

import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kimeeo.kAndroid.listViews.EmptyViewHelper;
import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.dataProvider.MonitorList;
import com.kimeeo.kAndroid.listViews.listView.BaseListViewAdapter;
import com.kimeeo.kAndroid.listViews.listView.EndlessListScrollListener;

import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
public class ListViewHelper extends BaseHelper implements AdapterView.OnItemClickListener, DataProvider.OnFatchingObserve,MonitorList.OnChangeWatcher
{
    protected EmptyViewHelper mEmptyViewHelper;
    protected ListView mList;
    protected BaseListViewAdapter mAdapter;
    boolean showInternetError = true;
    boolean showInternetRetry = true;
    private DataProvider dataProvider;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private OnItemClick onItemClick;
    public ListViewHelper(){

    }
    public Resources getResources()
    {
        return mList.getResources();
    }
    public void retry()
    {
        next();
    }
    public ListViewHelper emptyView(View view) {
        mEmptyViewHelper = new EmptyViewHelper(view.getContext(), view, this, showInternetError, showInternetRetry);
        return this;
    }
    public ListViewHelper showInternetError(boolean showInternetError) {
        this.showInternetError = showInternetError;
        return this;
    }
    public ListViewHelper showInternetRetry(boolean showInternetRetry) {
        this.showInternetRetry = showInternetRetry;
        return this;
    }
    public ListViewHelper emptyView(EmptyViewHelper emptyViewHelper) {
        mEmptyViewHelper = emptyViewHelper;
        return this;
    }
    public ListViewHelper with(ListView list) {
        this.mList = list;
        return this;
    }
    public ListViewHelper dataManager(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        dataProvider.addFatchingObserve(this);
        dataProvider.addDataChangeWatcher(this);
        return this;
    }
    public ListViewHelper next() {dataProvider.next();
        return this;
    }
    public ListViewHelper refresh() {
        dataProvider.refresh();
        return this;
    }
    public ListViewHelper swipeRefreshLayout(SwipeRefreshLayout view) {
        mSwipeRefreshLayout = view;
        return this;
    }
    public ListViewHelper adapter(BaseListViewAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }
    public ListViewHelper setOnItemClick(OnItemClick item) {
        onItemClick=item;
        return this;
    }
    protected void clear() {
        if(dataProvider!=null) {
            dataProvider.garbageCollectorCall();
            dataProvider = null;
        }
        if(mAdapter!=null)
            mAdapter.garbageCollectorCall();

        mAdapter =null;
        mList = null;
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.clean();
        mEmptyViewHelper = null;
        mSwipeRefreshLayout=null;
    }
    public ListViewHelper create() throws Exception{

        if(mAdapter==null)
            throw new Exception("Must have mAdapter");
        else if(mList==null)
            throw new Exception("Must have recyclerView");
        else if(dataProvider==null)
            throw new Exception("Must have dataManager");

        if(dataProvider.getRefreshEnabled())
            configSwipeRefreshLayout(mSwipeRefreshLayout);

        mList.setOnItemClickListener(this);
        mList.setAdapter(mAdapter);
        setOnScrollListener(mList);
        configListView(mList,mAdapter,dataProvider);
        next();
        return this;
    }
    protected void configListView(ListView mList,BaseListViewAdapter mAdapter,DataProvider dataProvider) {

    }
    protected void setOnScrollListener(ListView mList) {
        mList.setOnScrollListener(new EndlessListScrollListener() {
            @Override
            public void onLoadMore() {
                if(dataProvider.getCanLoadNext())
                    next();
            }
            public void onScrolled(AbsListView view, int firstVisibleItem, int visibleItemCount,int totalItemCount)
            {
                //BUG
                onDataScroll((ListView)view, 0, 0);
            }
        });
    }
    protected void onDataScroll(ListView recyclerView, int dx, int dy) {

    }
    protected void configSwipeRefreshLayout(SwipeRefreshLayout view) {
        mSwipeRefreshLayout = view;
        if(mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (dataProvider.getCanLoadRefresh())
                        refresh();
                    else
                        mSwipeRefreshLayout.setRefreshing(false);

                    mSwipeRefreshLayout.setEnabled(dataProvider.getCanLoadRefresh());
                }
            });
            boolean refreshEnabled = dataProvider.getRefreshEnabled();
            mSwipeRefreshLayout.setEnabled(refreshEnabled);
            //mSwipeRefreshLayout.setColorSchemeColors(R.array.progressColors);
        }
    }
    public void updateSwipeRefreshLayout(boolean isRefreshData) {
        if(mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setRefreshing(false);

            if(isRefreshData)
                mSwipeRefreshLayout.setEnabled(dataProvider.getCanLoadRefresh());
        }
    }


    public void itemsAdded(int index,List items){
        dataLoaded(items,false);
    }

    public void itemsRemoved(int index,List items){
        dataLoaded(items,false);
    }

    public void itemsChanged(int index,List items){
        dataLoaded(items,false);
    }

    public void onFetchingStart(boolean isFetchingRefresh){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updatesStart();
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh){
        dataLoaded(dataList,isFetchingRefresh);
    }
    @Override
    public void onFetchingFinish(boolean isFetchingRefresh)
    {

    }
    protected void dataLoaded(List<?> dataList, boolean isFetchingRefresh) {
        if (isFetchingRefresh && mList!=null)
            mList.smoothScrollToPosition(0);
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(dataProvider);
        updateSwipeRefreshLayout(isFetchingRefresh);
    }
    public void onFetchingError(Object error){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(dataProvider);
        updateSwipeRefreshLayout(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Object baseObject = dataProvider.get(position);
        if(onItemClick!=null)
            onItemClick.onItemClick(baseObject);
    }
    public interface OnItemClick {
        void onItemClick(Object baseObject);
    }
}
