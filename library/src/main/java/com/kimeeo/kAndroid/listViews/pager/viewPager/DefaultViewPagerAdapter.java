package com.kimeeo.kAndroid.listViews.pager.viewPager;

import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.pager.BaseItemHolder;
/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
public class DefaultViewPagerAdapter extends BaseViewPagerAdapter
{
    public String getItemTitle(int position,Object data) {
        return viewProvider.getItemTitle(position,data);
    }
    public void garbageCollectorCall() {
        super.garbageCollectorCall();
        viewProvider=null;
    }
    private IViewProvider viewProvider;
    public DefaultViewPagerAdapter(DataProvider dataProvider, IViewProvider pageView) {
        super(dataProvider);
        this.viewProvider=pageView;
    }
    protected BaseItemHolder getItemHolder(View view, int position, Object data) {
        return viewProvider.getItemHolder(view,position, data);
    }
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        return obj;
    }
    protected View getView(int position,Object data) {
        return viewProvider.getView(position, data);
    }
    protected void removeView(View view,int position,BaseItemHolder itemHolder) {
        viewProvider.removeView((View) view,position,itemHolder);
    }
}
