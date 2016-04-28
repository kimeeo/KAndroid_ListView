package com.kimeeo.kAndroid.listViews.pager.fragmentPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
/**
 * Created by bhavinpadhiyar on 1/21/16.
 */
public class DefaultViewFragmentPagerAdapter extends BaseFragmentViewPagerAdapter {
    private final IFragmentProvider fragmentProvider;
    public DefaultViewFragmentPagerAdapter(FragmentManager fragmentManager, DataProvider dataProvider, ViewPager viewPager, IFragmentProvider fragmentProvider,OnItemCreated onItemCreated) {
        super(fragmentManager,dataProvider,viewPager,onItemCreated);
        this.fragmentProvider = fragmentProvider;
    }
    public Fragment getItemFragment(int position, Object navigationObject) {
        return fragmentProvider.getItemFragment( position, navigationObject);
    }
    public String getItemTitle(int position,Object navigationObject) {
        return fragmentProvider.getItemTitle( position, navigationObject);
    }
}
