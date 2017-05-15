package com.kimeeo.kAndroid.listViews.pager.fragmentPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.pager.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.pager.BaseViewPager;
import com.kimeeo.kAndroid.listViews.pager.viewPager.BaseViewPagerAdapter;

/**
 * Created by bhavinpadhiyar on 1/21/16.
 */
abstract  public class BaseFragmentViewPager extends BaseViewPager implements BaseFragmentViewPagerAdapter.OnItemCreated
{
    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        if(mFragmentAdapter!=null)
            mFragmentAdapter.garbageCollectorCall();
        mFragmentAdapter=null;
    }
    public String getPageTitle(int position, Object o)
    {
        return "";
    }
    public void onItemCreated(Fragment page) {

    }
    public void onDestroyItem(Fragment page) {

    }
    final protected BaseViewPagerAdapter createViewPagerAdapter(){return null;}
    final public View getView(int position, Object data){return null;}
    final public BaseItemHolder getItemHolder(View view, int position, Object data){return null;}
    final public BaseViewPagerAdapter getAdapter() {return null;}
    public BaseFragmentViewPagerAdapter getFragmentAdapter() {
        return mFragmentAdapter;
    }
    protected BaseFragmentViewPagerAdapter mFragmentAdapter;
    abstract protected BaseFragmentViewPagerAdapter createViewPagerFragmentAdapter(FragmentManager fragmentManager, DataProvider dataProvider, ViewPager viewPager);
    @Override
    protected void createAdapter(ViewPager mViewPager) {
        mFragmentAdapter = createViewPagerFragmentAdapter(getChildFragmentManager(),getDataProvider(),getViewPager());
        mViewPager.setAdapter(mFragmentAdapter);
    }

}
