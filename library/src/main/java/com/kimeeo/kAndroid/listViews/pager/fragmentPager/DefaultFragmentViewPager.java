package com.kimeeo.kAndroid.listViews.pager.fragmentPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;

/**
 * Created by bhavinpadhiyar on 1/21/16.
 */
abstract public class DefaultFragmentViewPager extends BaseFragmentViewPager implements IFragmentProvider{
    abstract public Fragment getItemFragment(int position, Object navigationObject);
    protected BaseFragmentViewPagerAdapter createViewPagerFragmentAdapter(FragmentManager fragmentManager, DataProvider dataProvider, ViewPager viewPager) {
        return new DefaultViewFragmentPagerAdapter(fragmentManager,dataProvider,viewPager,this,this);
    }
}
