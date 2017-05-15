package com.kimeeo.kAndroid.listViews.pager.viewPager;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.dataProvider.MonitorList;
import com.kimeeo.kAndroid.listViews.pager.BaseItemHolder;

import java.lang.ref.WeakReference;
import java.util.List;
import com.kimeeo.kAndroid.listViews.ProgressItem;
/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
abstract public class BaseViewPagerAdapter extends PagerAdapter implements DataProvider.OnFatchingObserve,MonitorList.OnChangeWatcher {
    public boolean supportLoader = true;
    private WeakReference<OnUpdateItem> onUpdateItem;
    private DataProvider dataProvider;

    public BaseViewPagerAdapter(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.dataProvider.addFatchingObserve(this);
        this.dataProvider.addDataChangeWatcher(this);
    }

    public OnUpdateItem getOnUpdateItem() {
        if(onUpdateItem!=null)
            return onUpdateItem.get();
        return
                null;
    }

    public void setOnUpdateItem(OnUpdateItem onUpdateItem) {
        this.onUpdateItem = new WeakReference<OnUpdateItem>(onUpdateItem);
    }

    public void garbageCollectorCall() {
        dataProvider=null;
    }

    protected BaseItemHolder getProgressViewHolder(View view) {
        return  new ProgressViewHolder(view);
    }

    abstract protected View getView(int position,Object data);

    abstract protected BaseItemHolder getItemHolder(View view,int position,Object data);

    abstract protected void removeView(View view,int position,BaseItemHolder itemHolder);

    public int getCount() {
        if(dataProvider!=null)
            return dataProvider.size();
        return 0;
    }
    public Object instantiateItem(ViewGroup container, int position) {
        Object data = dataProvider.get(position);
        View view = getItemView(position, data,container);
        BaseItemHolder itemHolder= getItemHolderAll(view, position, data);
        view.setTag(itemHolder);
        container.addView(view, 0);
        itemHolder.updateItemView(data, position);
        if(onUpdateItem!=null && onUpdateItem.get()!=null)
            onUpdateItem.get().update(itemHolder,data, position);
        return view;
    }
    protected BaseItemHolder getItemHolderAll(View view,int position,Object data) {
        if(data instanceof ProgressItem && supportLoader)
            return getProgressViewHolder(view);
        return getItemHolder(view,position, data);
    }
    protected View getItemView(int position,Object data,ViewGroup container) {
        if(data instanceof ProgressItem && supportLoader)
            return getProgressView(container);
        return getView(position, data);
    }
    public View getProgressView(ViewGroup container) {
        LayoutInflater li = LayoutInflater.from(container.getContext());
        View view = li.inflate(R.layout._fragment_view_pager_progress_item, null, false);
        return view;
    }
    public void destroyItem(ViewGroup container, int position, Object object) {
        try
        {
            View view = (View) object;
            BaseItemHolder itemHolder = (BaseItemHolder)view.getTag();
            itemHolder.cleanView(view, position);
            removeView((View) object, position,itemHolder);
            container.removeView((View) object);
        }catch(Exception e){}
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
    protected DataProvider getDataProvider()
    {
        return dataProvider;
    }

    protected ProgressItem getProgressItem()
    {
        return new ProgressItem();
    }

    @Override
    public void onFetchingStart(boolean isFetchingRefresh){
        if(supportLoader) {
            try
            {
                getDataProvider().add(getProgressItem());
                notifyDataSetChanged();
            }catch (Exception e){}
        }
    }

    public void onFetchingFinish(boolean isFetchingRefresh)
    {
        removeProgressBar();
        notifyDataSetChanged();
    }
    public void onFetchingError(Object error)
    {
        removeProgressBar();
    }
    protected void removeProgressBar() {
        List<Object> list = getDataProvider();
        if(list.size()!=0 && list.get(list.size() - 1) instanceof ProgressItem && supportLoader)
            getDataProvider().remove(getDataProvider().size() - 1);
        notifyDataSetChanged();
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh)
    {
        notifyDataSetChanged();
    }

    public void itemsChanged(int index, List items) {
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


    abstract public String getItemTitle(int position,Object navigationObject);
    @Override
    public CharSequence getPageTitle(int position) {
        Object data = getDataProvider().get(position);
        return getItemTitle(position, data);
    }
    @Override
    public int getItemPosition(Object object) {
        View view = (View) object;
        BaseItemHolder itemHolder = (BaseItemHolder)view.getTag();
        if(itemHolder instanceof ProgressViewHolder)
            return POSITION_NONE;
        return super.getItemPosition(object);
    }


    public interface OnUpdateItem {
        void update(BaseItemHolder itemHolder, Object item, int position);
    }



    // Update View Here
    public class ProgressViewHolder extends BaseItemHolder {
        public ProgressViewHolder(View itemView){
            super(itemView);
        }
        public void cleanView(View itemView,int position){
        }
        public void updateItemView(Object item,View view,int position){

        }
    }
}
