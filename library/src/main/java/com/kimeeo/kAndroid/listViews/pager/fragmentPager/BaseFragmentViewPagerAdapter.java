package com.kimeeo.kAndroid.listViews.pager.fragmentPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.core.fragment.BaseFragment;
import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.MonitorList;

import java.util.List;
import com.kimeeo.kAndroid.listViews.ProgressItem;

/**
 * Created by bhavinpadhiyar on 1/21/16.
 */
abstract public class BaseFragmentViewPagerAdapter extends FragmentStatePagerAdapter implements DataProvider.OnFatchingObserve,MonitorList.OnChangeWatcher {
    private OnItemCreated onItemCreated;
    protected void garbageCollectorCall() {
        onItemCreated=null;
        dataProvider=null;
        mViewPager=null;
    }
    abstract public Fragment getItemFragment(int position, Object navigationObject);
    abstract public String getItemTitle(int position,Object navigationObject);
    private DataProvider dataProvider;
    private ViewPager mViewPager;
    public BaseFragmentViewPagerAdapter(FragmentManager fragmentManager, DataProvider dataProvider, ViewPager viewPager, OnItemCreated onItemCreated) {
        super(fragmentManager);
        this.dataProvider=dataProvider;
        //must have to be disable
        this.dataProvider.setRefreshEnabled(false);
        this.dataProvider.addFatchingObserve(this);
        this.dataProvider.addDataChangeWatcher(this);
        this.mViewPager = viewPager;
        this.onItemCreated =onItemCreated;
    }
    protected DataProvider getDataProvider()
    {
        return dataProvider;
    }
    public boolean supportLoader = true;
    public void itemsChanged(int index,List items){
        notifyDataSetChanged();
    }

    public void itemsAdded(int position,List items)
    {
        notifyDataSetChanged();
    }
    public void itemsRemoved(int position,List items)
    {
        notifyDataSetChanged();
    }
    public void onFetchingStart(boolean isFetchingRefresh){
        if(supportLoader) {
            getDataProvider().add(getProgressItem());
            notifyDataSetChanged();
        }
    }

    protected ProgressItem getProgressItem()
    {
        return new ProgressItem();
    }


    public void onFetchingFinish(boolean isFetchingRefresh)
    {
        removeProgressBar();
    }
    public void onFetchingError(Object error)
    {
        removeProgressBar();
    }
    protected void removeProgressBar() {
        List<Object> list = getDataProvider();
        if(list.size()!=0 && list.get(list.size() - 1) instanceof ProgressItem && supportLoader)
        {
            getDataProvider().remove(getDataProvider().size() - 1);
            if(firstTime) {
                mViewPager.setAdapter(this);
                firstTime=false;
            }
        }
        notifyDataSetChanged();
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh)
    {
        if(dataList!=null && dataList.size()!=0)
            notifyDataSetChanged();
    }

    boolean firstTime = true;
    // Returns total number of pages
    @Override
    public int getCount() {
        if(getDataProvider()!=null)
            return getDataProvider().size();
        return 0;
    }
    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        Object navigationObject = getDataProvider().get(position);
        Fragment fragment;
        if(navigationObject instanceof ProgressItem)
            fragment = getProgressView(position, navigationObject);
        else
            fragment = getItemFragment(position, navigationObject);
        if (onItemCreated != null)
            onItemCreated.onItemCreated(fragment);
        return fragment;
    }
    protected Fragment getProgressView(int position, Object navigationObject) {
        return BaseFragment.newInstance(ProgressItem.ProgressbarView.class);
    }
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container,position,object);
        Fragment fragment = (Fragment)object;
        if(onItemCreated!=null)
            onItemCreated.onDestroyItem(fragment);
    }
    public CharSequence getPageTitle(int position) {
        Object navigationObject = getDataProvider().get(position);
        return getItemTitle(position,navigationObject);
    }

    public interface OnItemCreated {
        void onItemCreated(Fragment page);
        void onDestroyItem(Fragment page);
    }
}

