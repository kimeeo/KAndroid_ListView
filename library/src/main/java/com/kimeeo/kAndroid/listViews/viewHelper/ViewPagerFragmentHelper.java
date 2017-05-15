package com.kimeeo.kAndroid.listViews.viewHelper;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.pager.fragmentPager.BaseFragmentViewPagerAdapter;
/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
public class ViewPagerFragmentHelper extends ViewPagerHelper {
    protected BaseFragmentViewPagerAdapter mFragmentAdapter;
    public ViewPagerFragmentHelper fragmentAdapter(BaseFragmentViewPagerAdapter mFragmentAdapter) {
        this.mFragmentAdapter=mFragmentAdapter;
        return this;
    }
    public void create() throws Exception{
        if(mFragmentAdapter==null)
            throw new Exception("Must have mFragmentAdapter");
        else if(mViewPager==null)
            throw new Exception("Must have BaseViewPager");
        else if(dataProvider==null)
            throw new Exception("Must have dataManager");
        if(dataProvider.getRefreshEnabled())
            configSwipeRefreshLayout(mSwipeRefreshLayout);
        mViewPager.setAdapter(mFragmentAdapter);
        if(mIndicator!=null)
            setUpIndicator(mIndicator, mViewPager);
        mViewPager.addOnPageChangeListener(this);
        next();
        configViewPager(mViewPager, mFragmentAdapter, mIndicator,dataProvider);
    }
    protected void configViewPager(ViewPager mList, BaseFragmentViewPagerAdapter mAdapter, View indicator, DataProvider dataProvider) {

    }
}
