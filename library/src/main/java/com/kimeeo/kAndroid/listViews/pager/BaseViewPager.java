package com.kimeeo.kAndroid.listViews.pager;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.BaseListDataView;
import com.kimeeo.kAndroid.listViews.EmptyViewHelper;
import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.listViews.pager.viewPager.BaseViewPagerAdapter;

import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
abstract public class BaseViewPager extends BaseListDataView implements ViewPager.OnPageChangeListener,BaseViewPagerAdapter.OnUpdateItem {
    protected View mRootView;
    protected BaseViewPagerAdapter mAdapter;
    protected ViewPager mViewPager;
    protected View mIndicator;
    protected EmptyViewHelper mEmptyViewHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isIndicatorSetFirstTime = true;
    private int currentItem;
    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        mRootView = null;
        if (mAdapter != null)
            mAdapter.garbageCollectorCall();
        mAdapter = null;
        mViewPager = null;
        mSwipeRefreshLayout = null;
        mIndicator = null;
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.clean();
        mEmptyViewHelper = null;
    }
    public View getRootView() {
        return mRootView;
    }
    public BaseViewPagerAdapter getAdapter() {
        return mAdapter;
    }
    public ViewPager getViewPager() {
        return mViewPager;
    }
    public SwipeRefreshLayout getSwipeRefreshLayout()
    {
        return mSwipeRefreshLayout;
    }
    public String getItemTitle(int position,Object navigationObject)
    {
        return "";
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        configViewParam();
        mRootView = createRootView(inflater, container, savedInstanceState);
        if(getDataProvider().getRefreshEnabled())
            configSwipeRefreshLayout(createSwipeRefreshLayout(mRootView));

        mViewPager = createViewPager(mRootView);
        mEmptyViewHelper = createEmptyViewHelper();
        createAdapter(mViewPager);

        mIndicator = createIndicator(mRootView);
        updateIndicator(mIndicator, mViewPager);

        mViewPager.addOnPageChangeListener(this);
        next();
        configViewPager(mViewPager, mAdapter, mIndicator);
        onViewCreated(mRootView);
        return mRootView;
    }
    protected EmptyViewHelper createEmptyViewHelper() {
        return new EmptyViewHelper(getActivity(), createEmptyView(mRootView), this, showInternetError(), showInternetRetryButton());
    }
    protected void createAdapter(ViewPager mViewPager) {
        mAdapter = createViewPagerAdapter();
        mAdapter.setOnUpdateItem(this);
        mViewPager.setAdapter(mAdapter);
    }
    public void update(BaseItemHolder itemHolder, Object item, int position)
    {

    }
    //Confgi Your your viewpager here
    protected void configViewPager(ViewPager mList, BaseViewPagerAdapter mAdapter, View indicator) {

    }
    public void onViewCreated(View view) {

    }

    @LayoutRes
    protected int getRootRefreshLayoutResID() {
        return R.layout._fragment_page_view_with_swipe_refresh_layout;
    }
    @LayoutRes
    protected int getRootLayoutResID() {
        return R.layout._fragment_page_view;
    }
    @IdRes
    protected int getViewPagerResID() {
        return  R.id.viewPager;
    }
    @IdRes
    protected int getEmptyViewResID() {
        return  R.id.emptyView;
    }
    @IdRes
    protected int getSwipeRefreshLayoutResID() {
        return  R.id.swipeRefreshLayout;
    }

    @IdRes
    protected int getIndicatorResID() {
        return  R.id.indicator;
    }


    protected View createEmptyView(View rootView) {
        View emptyView = rootView.findViewById(getEmptyViewResID());
        return emptyView;
    }
    protected void configSwipeRefreshLayout(SwipeRefreshLayout view) {
        mSwipeRefreshLayout = view;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (getDataProvider().getCanLoadRefresh()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        refresh();
                    }
                    else
                        mSwipeRefreshLayout.setRefreshing(false);

                    mSwipeRefreshLayout.setEnabled(getDataProvider().getRefreshEnabled());
                }
            });
            boolean refreshEnabled = getDataProvider().getRefreshEnabled();
            mSwipeRefreshLayout.setEnabled(refreshEnabled);
            mSwipeRefreshLayout.setColorSchemeColors(R.array.progressColors);
        }
    }
    protected SwipeRefreshLayout createSwipeRefreshLayout(View rootView) {
        if (rootView.findViewById(getSwipeRefreshLayoutResID()) != null) {
            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(getSwipeRefreshLayoutResID());
            return swipeRefreshLayout;
        }
        return null;
    }
    protected View createRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        if(getDataProvider().getRefreshEnabled())
            rootView = inflater.inflate(getRootRefreshLayoutResID(), container, false);
        else
            rootView = inflater.inflate(getRootLayoutResID(), container, false);
        return rootView;
    }
    abstract protected BaseViewPagerAdapter createViewPagerAdapter();
    public String getPageTitle(int position, Object o)
    {
        return "";
    }
    protected ViewPager createViewPager(View rootView) {
        ViewPager viewPager = (ViewPager) rootView.findViewById(getViewPagerResID());
        return viewPager;
    }
    protected void updateIndicator(View indicator, ViewPager viewPager){
        if(isIndicatorSetFirstTime){
            setUpIndicator(indicator,viewPager,isIndicatorSetFirstTime);
            isIndicatorSetFirstTime = false;
        }
        else
            setUpIndicator(indicator,viewPager,isIndicatorSetFirstTime);
    }

    protected void setUpIndicator(View indicator, ViewPager viewPager,boolean isFirstTime) {
        if(indicator!=null) {
            if(isFirstTime && indicator instanceof TabLayout && getDataProvider().size()!=0) {
                TabLayout tabLayout = (TabLayout) indicator;
                tabLayout.setupWithViewPager(viewPager);
                configTabLayout(tabLayout,viewPager);
            }
            else if (indicator instanceof TabLayout && getDataProvider().size()!=0) {
                final TabLayout tabLayout = (TabLayout) indicator;
                final int selected = tabLayout.getSelectedTabPosition();
                tabLayout.setupWithViewPager(viewPager);
                if (selected > 0) {
                    tabLayout.setVisibility(View.INVISIBLE);
                    tabLayout.getTabAt(selected).select();
                    tabLayout.setScrollPosition(selected, Float.parseFloat("0.3"), true);
                    tabLayout.setVisibility(View.VISIBLE);

                    final Handler handler = new Handler();
                    final Runnable runnablelocal = new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.setScrollPosition(selected, Float.parseFloat("0.3"), true);
                            tabLayout.setVisibility(View.VISIBLE);
                        }
                    };
                    handler.postDelayed(runnablelocal, 100);
                }
                configTabLayout(tabLayout, viewPager);
            }
        }
    }
    protected void configTabLayout(TabLayout tabLayout, ViewPager viewPager) {
    }
    protected View createIndicator(View rootView) {
        return rootView.findViewById(getIndicatorResID());
    }
    protected void onPageChange(Object itemPosition, int position) {}
    public void onPageSelected(int position) {
        if (mViewPager != null) {
            Object iBaseObject = getDataProvider().get(position);
            onPageChange(iBaseObject, position);
            setCurrentItem(position);
            if (getDataProvider().getCanLoadNext() && position == getDataProvider().size() - 1)
                next();
            updateRefreshEnabled(position);
        }
    }
    private void updateRefreshEnabled(int position) {
        if (getDataProvider().getCanLoadRefresh() && getDataProvider().getCanLoadRefresh() && position == 0) {
            if (mSwipeRefreshLayout != null)
                mSwipeRefreshLayout.setEnabled(true);
            else
                refresh();
        }
        else if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setEnabled(false);
    }
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }
    public void onPageScrollStateChanged(int arg0) {
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
    protected int getCurrentItem() {
        return currentItem;
    }
    protected void setCurrentItem(int value) {
        currentItem = value;
    }
    public void onDataReceived(String url, Object value, Object status) {
    }

    public void updateSwipeRefreshLayout(boolean isRefreshData) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (isRefreshData)
                mSwipeRefreshLayout.setEnabled(getDataProvider().getRefreshEnabled());
        }
    }
    public void onItemClick(View view, int position) {
        Object baseObject = getDataProvider().get(position);
        onItemClick(baseObject);
    }
    public void onItemClick(Object baseObject) {

    }


    //Watch Data Updates
    public void onFetchingStart(boolean isFetchingRefresh){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updatesStart();

        updateIndicator(mIndicator, mViewPager);
    }

    public void onFetchingError(Object error){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
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
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
    }

    public void itemsRemoved(int index,List items){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
    }

    public void itemsChanged(int index,List items){
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
    }

    private void dataLoaded(List<?> dataList,final boolean isFetchingRefresh) {
        if(dataList!=null && dataList.size()!=0 && getViewPager()!=null)
        {
            /*
                if (isFetchingRefresh) {
                    final Handler handler = new Handler();
                    final Runnable runnablelocal = new Runnable() {
                        @Override
                        public void run() {
                            if (isFetchingRefresh) {
                                if (getViewPager() instanceof DirectionalViewPager == false)
                                {
                                    getViewPager().setAdapter(getAdapter());
                                    gotoItem(0, true);
                                    setUpIndicator(mIndicator, mViewPager);
                                }
                            }
                        }
                    };
                    handler.postDelayed(runnablelocal, 1000);
                } else
                    setUpIndicator(mIndicator, mViewPager);
            */
            updateIndicator(mIndicator, mViewPager);

        }
        if (mEmptyViewHelper != null)
            mEmptyViewHelper.updateView(getDataProvider());
        updateSwipeRefreshLayout(isFetchingRefresh);
    }
}
