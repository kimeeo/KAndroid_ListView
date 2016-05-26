package com.kimeeo.kAndroid.listViews.viewHelper;

import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.kimeeo.kAndroid.listViews.EmptyViewHelper;
import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.MonitorList;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.EndlessRecyclerOnScrollListener;
import com.kimeeo.kAndroid.listViews.recyclerView.GridHelper;

import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
public class RecyclerViewHelper extends BaseHelper implements AdapterView.OnItemClickListener, DataProvider.OnFatchingObserve,MonitorList.OnChangeWatcher
{
    protected EmptyViewHelper mEmptyViewHelper;
    protected BaseRecyclerViewAdapter mAdapter;
    boolean showInternetError = true;
    boolean showInternetRetry = true;
    private RecyclerView.ItemAnimator itemAnimator;
    private int animatorDuration = 200;
    private RecyclerView recyclerView;
    private DataProvider dataProvider;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private OnItemClick onItemClick;
    private GridHelper.IColoumProvider coloumProvider;
    private GridHelper gridHelper;
    public RecyclerViewHelper() {

    }
    public Resources getResources() {
        return recyclerView.getResources();
    }
    public void retry() {
        next();
    }
    public RecyclerViewHelper emptyView(View view) {
        mEmptyViewHelper = new EmptyViewHelper(view.getContext(), view, this, showInternetError, showInternetRetry);
        return this;
    }
    public RecyclerViewHelper showInternetError(boolean showInternetError) {
        this.showInternetError = showInternetError;
        return this;
    }
    public RecyclerViewHelper showInternetRetry(boolean showInternetRetry) {
        this.showInternetRetry = showInternetRetry;
        return this;
    }
    public RecyclerViewHelper emptyView(EmptyViewHelper emptyViewHelper) {
        mEmptyViewHelper = emptyViewHelper;
        return this;
    }
    public RecyclerViewHelper animator(RecyclerView.ItemAnimator itemAnimator) {
        this.itemAnimator=itemAnimator;
        return this;
    }
    public RecyclerViewHelper animatorDuration(int value) {
        animatorDuration=value;
        return this;
    }
    public RecyclerViewHelper with(RecyclerView recyclerView) {
        this.recyclerView =recyclerView;
        return this;
    }
    public RecyclerViewHelper dataManager(DataProvider dataProvider) {
        this.dataProvider =dataProvider;
        this.dataProvider.addFatchingObserve(this);
        this.dataProvider.addDataChangeWatcher(this);
        return this;
    }
    public RecyclerViewHelper next(){
        dataProvider.next();
        return this;
    }
    public RecyclerViewHelper refresh() {
        dataProvider.refresh();
        return this;
    }
    public RecyclerViewHelper swipeRefreshLayout(SwipeRefreshLayout view) {
        mSwipeRefreshLayout =view;
        return this;
    }
    public RecyclerViewHelper layoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }
    public RecyclerViewHelper adapter(BaseRecyclerViewAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }
    public RecyclerViewHelper decoration(RecyclerView.ItemDecoration item) {
        this.itemDecoration = item;
        return this;
    }
    public RecyclerViewHelper setOnItemClick(OnItemClick item) {
        onItemClick=item;
        return this;
    }

    public RecyclerViewHelper setColoumProvider(GridHelper.IColoumProvider item) {
        coloumProvider = item;
        return this;
    }

    public RecyclerViewHelper gridHelper(GridHelper item) {
        gridHelper = item;
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
        recyclerView = null;

        if (mEmptyViewHelper != null)
            mEmptyViewHelper.clean();
        mEmptyViewHelper = null;
        mSwipeRefreshLayout=null;
    }
    public RecyclerViewHelper create() throws Exception{
        if(layoutManager==null)
            throw new Exception("Must have layoutManager");
        else if(mAdapter==null)
            throw new Exception("Must have mAdapter");
        else if(recyclerView==null)
            throw new Exception("Must have recyclerView");
        else if(dataProvider==null)
            throw new Exception("Must have dataManager");


        recyclerView.setLayoutManager(layoutManager);
        if(itemDecoration!=null)
            recyclerView.addItemDecoration(itemDecoration);

        if (gridHelper == null && coloumProvider != null)
            gridHelper = new GridHelper(coloumProvider, recyclerView.getContext());

        if (gridHelper != null)
            gridHelper.configLayoutManager(layoutManager);


        if(dataProvider.getRefreshEnabled())
            configSwipeRefreshLayout(mSwipeRefreshLayout);

        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        setOnScrollListener(recyclerView);
        setItemAnimator(recyclerView);
        configRecyclerView(recyclerView,mAdapter,dataProvider);
        next();
        return this;
    }
    protected void configRecyclerView(RecyclerView recyclerView, BaseRecyclerViewAdapter mAdapter, DataProvider dataProvider) {

    }
    protected void setItemAnimator(RecyclerView mList) {
        if(itemAnimator!=null) {
            itemAnimator.setAddDuration(animatorDuration);
            itemAnimator.setChangeDuration(animatorDuration);
            itemAnimator.setMoveDuration(animatorDuration);
            itemAnimator.setRemoveDuration(animatorDuration);
            mList.setItemAnimator(itemAnimator);
        }
    }
    protected void setOnScrollListener(RecyclerView mList) {
        mList.setOnScrollListener(new EndlessRecyclerOnScrollListener(mList.getLayoutManager()) {
            @Override
            public void onLoadMore() {
                if(dataProvider.getCanLoadNext())
                    next();
            }
            public void onScroll(RecyclerView recyclerView, int dx, int dy)
            {
                onDataScroll(recyclerView, dx, dy);
            }
        });
    }
    protected void onDataScroll(RecyclerView recyclerView, int dx, int dy) {

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
            mSwipeRefreshLayout.setColorSchemeColors(R.array.progressColors);
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

    @Override
    public void onFetchingFinish(boolean isFetchingRefresh)
    {

    }
    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh){
        dataLoaded(dataList,isFetchingRefresh);
    }
    protected void dataLoaded(List<?> dataList, boolean isFetchingRefresh) {
        if (isFetchingRefresh)
            recyclerView.scrollToPosition(0);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object baseObject = dataProvider.get(position);
        if(onItemClick!=null)
            onItemClick.onItemClick(baseObject);
    }
    public interface OnItemClick {
        void onItemClick(Object baseObject);
    }
}
