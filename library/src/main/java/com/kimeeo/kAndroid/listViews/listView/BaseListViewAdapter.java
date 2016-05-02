package com.kimeeo.kAndroid.listViews.listView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.MonitorList;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bhavinpadhiyar on 1/20/16.
 */
abstract public class BaseListViewAdapter extends BaseAdapter implements DataProvider.OnFatchingObserve,MonitorList.OnChangeWatcher
{
    public static class ViewTypes {
        public static final int VIEW_PROGRESS = 0;
        public static final int VIEW_ITEM = 1;
        public static final int VIEW_HEADER = -1;
    }
    private static final String TAG = "BaseRecyclerViewAdapter";
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private DataProvider dataProvider;

    private WeakReference<OnUpdateItem> onUpdateItem;
    public OnUpdateItem getOnUpdateItem() {
        if(onUpdateItem!=null)
            return onUpdateItem.get();
        return
                null;
    }
    public void setOnUpdateItem(OnUpdateItem onUpdateItem) {
        this.onUpdateItem = new WeakReference<OnUpdateItem>(onUpdateItem);
    }


    public boolean supportLoader = true;
    public void garbageCollectorCall() {
        dataProvider=null;
    }
    protected DataProvider getDataProvider()
    {
        return dataProvider;
    }
    abstract protected View getItemView(int viewType,LayoutInflater inflater,ViewGroup container);
    abstract protected BaseItemHolder getItemHolder(int viewType,View view);
    @Override
    public int getCount() {
        if(getDataProvider()!=null)
            return getDataProvider().size();
        return 0;
    }
    public BaseListViewAdapter(DataProvider dataProvider){
        this.dataProvider= dataProvider;
        this.dataProvider.addFatchingObserve(this);
        this.dataProvider.addDataChangeWatcher(this);
    }
    public void itemsChanged(int index,List items){
        notifyDataSetChanged();
    };
    public void itemsAdded(int position,List items)
    {
        notifyDataSetChanged();
    }
    public void itemsRemoved(int position,List items)
    {
        notifyDataSetChanged();
    }
    public BaseItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        BaseItemHolder itemHolder;
        View root;
        if(viewType== ViewTypes.VIEW_PROGRESS && supportLoader)
        {
            root = getProgressItem(viewType,inflater, container);
            itemHolder = new ProgressViewHolder(root);
        }
        else {

            root= getItemView(viewType,inflater,container);
            itemHolder= getItemHolder(viewType,root);
        }
        return itemHolder;

    }
    protected View getProgressItem(int viewType,LayoutInflater inflater,ViewGroup container) {
        return inflater.inflate(R.layout._fragment_recycler_progress_item, container, false);
    }
    public int getItemCount() {
        return getDataProvider().size();
    }
    @Override
    public Object getItem(int position) {
        return getDataProvider().get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    public int getViewTypeCount()
    {
        return getTotalViewTypeCount()+1;
    }
    abstract protected int getTotalViewTypeCount();
    private Map<Integer,Integer> viewTypes=new HashMap<>();
    private int counter=1;
    protected int itemViewType(int viewType) {
        /*
        Integer value=viewTypes.get(viewType);
        if(value==null) {
            viewTypes.put(viewType, counter);
            value=viewTypes.get(viewType);
            counter++;
        }
        */
        return viewType;
    }
    protected int getListItemViewType(int position, Object item)
    {
        return ViewTypes.VIEW_ITEM;
    }
    public void onBindViewHolder(BaseItemHolder itemHolder, int position) {
        Object item = getDataProvider().get(position);
        itemHolder.updateItemView(item, position);
        if(onUpdateItem!=null && onUpdateItem.get()!=null)
            onUpdateItem.get().update(itemHolder,item, position);
    }
    @Override
    public int getItemViewType(int position){
        if(getDataProvider().get(position) instanceof ProgressItem)
            return ViewTypes.VIEW_PROGRESS;
        else
            return itemViewType(getListItemViewType(position,getDataProvider().get(position)));
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseItemHolder holder = null;
        if (convertView == null) {
            int viewType = getItemViewType(position);
            holder = onCreateViewHolder(parent,viewType);
            convertView  = holder.getItemView();
            convertView.setTag(holder);
        } else {
            holder = (BaseItemHolder)convertView.getTag();
        }

        holder.position = position;
        onBindViewHolder(holder,position);

        return convertView;
    }
    public static class ProgressItem{

    }
    public void onFetchingStart(boolean isFetchingRefresh){
        if(supportLoader) {
            try
            {
                getDataProvider().add(new ProgressItem());
                notifyDataSetChanged();
            }catch (Exception e){}
        }
    };
    @Override
    public void onFetchingFinish(boolean isFetchingRefresh)
    {
        List<Object> list = getDataProvider();
        if(list.size()!=0 && list.get(list.size() - 1) instanceof ProgressItem && supportLoader)
            getDataProvider().remove(getDataProvider().size() - 1);

        notifyDataSetChanged();
    }
    @Override
    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh){
        List<Object> list = getDataProvider();
        if(list.size()!=0 && list.get(list.size() - 1) instanceof ProgressItem && supportLoader)
            getDataProvider().remove(getDataProvider().size() - 1);

        notifyDataSetChanged();
    };
    public void onFetchingError(Object error){
    };
    // Update View Here
    public static class ProgressViewHolder extends BaseItemHolder {
        public ProgressViewHolder(View itemView){
            super(itemView);
        }
        public void updateItemView(Object item,View view,int position){

        }
    }
    public interface OnUpdateItem
    {
        void update(BaseItemHolder itemHolder, Object item, int position);
    }
}

