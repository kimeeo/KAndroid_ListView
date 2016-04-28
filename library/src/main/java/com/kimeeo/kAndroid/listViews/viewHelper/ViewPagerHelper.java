package com.kimeeo.kAndroid.listViews.viewHelper;

import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.kimeeo.kAndroid.listViews.EmptyViewHelper;
import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.MonitorList;
import com.kimeeo.kAndroid.listViews.pager.viewPager.BaseViewPagerAdapter;

import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
public class ViewPagerHelper extends BaseHelper implements DataProvider.OnFatchingObserve,MonitorList.OnChangeWatcher,ViewPager.OnPageChangeListener {
    protected EmptyViewHelper mEmptyViewHelper;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected DataProvider dataProvider;
    protected BaseViewPagerAdapter mAdapter;
    protected ViewPager mViewPager;
    protected View mIndicator;
    boolean showInternetError = true;
    boolean showInternetRetry = true;
    private boolean isIndicatorSet = false;
    private int currentItem;

    public Resources getResources() {
        return mViewPager.getResources();
    }
    public void retry() {
        next();
    }
    public ViewPagerHelper emptyView(View view) {
        mEmptyViewHelper = new EmptyViewHelper(view.getContext(), view, this, showInternetError, showInternetRetry);
        return this;
    }
    public ViewPagerHelper showInternetError(boolean showInternetError) {
        this.showInternetError = showInternetError;
        return this;
    }
    public ViewPagerHelper showInternetRetry(boolean showInternetRetry) {
        this.showInternetRetry = showInternetRetry;
        return this;
    }
    public ViewPagerHelper
    emptyView(EmptyViewHelper emptyViewHelper) {
        mEmptyViewHelper = emptyViewHelper;
        return this;
    }
    protected void clear() {
        if (dataProvider != null) {
            dataProvider.garbageCollectorCall();
            dataProvider = null;
        }
        if (mAdapter != null)
            mAdapter.garbageCollectorCall();

        mAdapter = null;
        mViewPager = null;
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.clean();
        mEmptyViewHelper = null;
        mSwipeRefreshLayout = null;
    }
    public ViewPagerHelper swipeRefreshLayout(SwipeRefreshLayout view) {
        mSwipeRefreshLayout = view;
        return this;
    }
    public ViewPagerHelper dataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.dataProvider.addFatchingObserve(this);
        this.dataProvider.addDataChangeWatcher(this);
        return this;
    }
    public ViewPagerHelper adapter(BaseViewPagerAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }
    public ViewPagerHelper with(ViewPager view) {
        this.mViewPager =view;
        return this;
    }
    public ViewPagerHelper indicator(View view) {
        mIndicator = view;
        return this;
    }
    public void create() throws Exception{

        if(mAdapter==null)
            throw new Exception("Must have Adapter");
        else if(mViewPager==null)
            throw new Exception("Must have BaseViewPager");
        else if(dataProvider==null)
            throw new Exception("Must have dataProvider");

        if(dataProvider.getRefreshEnabled())
            configSwipeRefreshLayout(mSwipeRefreshLayout);


        mViewPager.setAdapter(mAdapter);

        if(mIndicator!=null)
            setUpIndicator(mIndicator, mViewPager);

        mViewPager.addOnPageChangeListener(this);

        next();
        configViewPager(mViewPager, mAdapter, mIndicator,dataProvider);

    }

    protected void configSwipeRefreshLayout(SwipeRefreshLayout view) {
        mSwipeRefreshLayout = view;
        if (mSwipeRefreshLayout != null) {
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

    public ViewPagerHelper next(){
        dataProvider.next();
        return this;
    }

    public ViewPagerHelper refresh()
    {
        dataProvider.refresh();
        return this;
    }

    protected void configViewPager(ViewPager mList, BaseViewPagerAdapter mAdapter, View indicator, DataProvider dataProvider) {

    }
    protected void setUpIndicator(View indicator, ViewPager viewPager) {
        if(indicator!=null) {
            if(isIndicatorSet==false) {
                if (indicator instanceof TabLayout && dataProvider.size()!=0) {
                    TabLayout tabLayout = (TabLayout) indicator;
                    tabLayout.setupWithViewPager(viewPager);
                    configTabLayout(tabLayout,viewPager);
                }
                isIndicatorSet = true;
            }
            else if (indicator instanceof TabLayout && dataProvider.size()!=0) {
                TabLayout tabLayout = (TabLayout) indicator;
                tabLayout.setupWithViewPager(viewPager);
                configTabLayout(tabLayout, viewPager);
            }

        }
    }
    protected void configTabLayout(TabLayout tabLayout, ViewPager viewPager) {

    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    public void onPageScrollStateChanged(int arg0) {
    }
    protected void onPageChange(Object itemPosition, int position) {

    }
    protected int getCurrentItem() {
        return currentItem;
    }
    protected void setCurrentItem(int value) {
        currentItem = value;
    }
    public void onPageSelected(int position) {
        if (mViewPager != null) {
            Object iBaseObject = dataProvider.get(position);
            onPageChange(iBaseObject, position);
            setCurrentItem(position);
            if (dataProvider.getCanLoadNext() && position == dataProvider.size() - 1)
                next();
            updateRefreshEnabled(position);
        }
    }
    private void updateRefreshEnabled(int position) {
        if (dataProvider.getCanLoadRefresh() && dataProvider.getCanLoadRefresh() && position == 0) {
            if (mSwipeRefreshLayout != null)
                mSwipeRefreshLayout.setEnabled(true);
            else
                refresh();
        }
        else if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setEnabled(false);
    }
    public void gotoItem(int value, Boolean scroll) {
        if (mViewPager != null)
            mViewPager.setCurrentItem(value, scroll);
        setCurrentItem(value);
    }
    public void goBack() {
        int index = -1;
        int currentIndex = getCurrentItem();
        if (currentIndex != 0)
            index = currentIndex - 1;
        else {
            if (mViewPager != null)
                index = mViewPager.getAdapter().getCount() - 1;

        }
        gotoItem(index, true);
    }
    public void goNext() {
        int index = -1;
        int currentIndex = getCurrentItem();
        int total = 0;
        if (mViewPager != null)
            total = mViewPager.getAdapter().getCount() - 1;
        if (currentIndex != total)
            index = currentIndex + 1;
        else
            index = 0;
        gotoItem(index, true);
    }
    public void itemsAdded(int index,List items){
        dataLoaded(items,false);
    };
    public void itemsRemoved(int index,List items){
        dataLoaded(items,false);
    };
    public void itemsChanged(int index,List items){
        dataLoaded(items,false);
    };
    public void onFetchingStart(boolean isFetchingRefresh){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updatesStart();
    };
    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh){
        dataLoaded(dataList,isFetchingRefresh);
    }
    protected void dataLoaded(List<?> dataList, boolean isFetchingRefresh) {
        if(dataList!=null && dataList.size()!=0 && mViewPager!=null)
        {
            final Handler handler = new Handler();
            final Runnable runnablelocal = new Runnable() {
                @Override
                public void run() {
                    setUpIndicator(mIndicator, mViewPager);
                }
            };
            handler.postDelayed(runnablelocal, 1000);
        }
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(dataProvider);

        updateSwipeRefreshLayout(isFetchingRefresh);
    }
    public void onFetchingError(Object error){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(dataProvider);
        updateSwipeRefreshLayout(false);
    };
    public void updateSwipeRefreshLayout(boolean isRefreshData) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (isRefreshData)
                mSwipeRefreshLayout.setEnabled(dataProvider.getCanLoadRefresh());
        }
    }
}
