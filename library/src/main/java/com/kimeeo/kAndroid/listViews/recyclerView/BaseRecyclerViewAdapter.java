package com.kimeeo.kAndroid.listViews.recyclerView;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.dataProvider.MonitorList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.kimeeo.kAndroid.listViews.ProgressItem;
/**
 * Created by bhavinpadhiyar on 7/21/15.
 */
abstract public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseItemHolder> implements MonitorList.OnChangeWatcher, BaseItemHolder.OnItemHolderClick,DataProvider.OnFatchingObserve {

    private static final String TAG = "BaseRecyclerViewAdapter";

    public boolean isSupportLoader() {
        return supportLoader;
    }

    public void setSupportLoader(boolean supportLoader) {
        this.supportLoader = supportLoader;
    }

    private boolean supportLoader = true;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private DataProvider dataProvider;

    private WeakReference<OnUpdateItem> onUpdateItem;

    public BaseRecyclerViewAdapter(DataProvider dataProvider) {
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

    public void garbageCollectorCall()
    {
        if(dataProvider!=null) {
            dataProvider.removeDataChangeWatcher(this);
            dataProvider.removeFatchingObserve(this);
        }
        dataProvider=null;
        mOnItemClickListener=null;
    }

    protected DataProvider getDataProvider()
    {
        return dataProvider;
    }

    abstract protected View getItemView(int viewType,LayoutInflater inflater,ViewGroup container);
    abstract protected BaseItemHolder getItemHolder(int viewType,View view);
    protected BaseItemHolder getProgressViewHolder(View view)
    {
        return new ProgressViewHolder(view);
    }

    public void onItemHolderClick(BaseItemHolder itemHolder,int position)
    {
        if (mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,position, itemHolder.getItemId());
    }

    @Override
    public BaseItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        BaseItemHolder itemHolder;
        View root;
        if(viewType== ViewTypes.VIEW_PROGRESS && supportLoader)
        {
            root = getProgressView(viewType,inflater, container);
            itemHolder = getProgressViewHolder(root);
            if(getItemClickSupport())
                itemHolder.setOnItemHolderClick(this);
        }
        else {
            root= getItemView(viewType,inflater,container);
            itemHolder= getItemHolder(viewType, root);
            if(getItemClickSupport())
                itemHolder.setOnItemHolderClick(this);
        }
        return itemHolder;

    }

    protected boolean getItemClickSupport() {
        return true;
    }

    protected View getProgressView(int viewType,LayoutInflater inflater,ViewGroup container)
    {
        return inflater.inflate(R.layout._fragment_recycler_progress_item, container, false);
    }

    @Override
    public int getItemCount() {
        if (getDataProvider() != null)
            return getDataProvider().size();
        return 0;
    }

    @Override
    public void onBindViewHolder(BaseItemHolder itemHolder, int position) {
        Object item = getDataProvider().get(position);
        if(getItemViewType(position)== ViewTypes.VIEW_PROGRESS && itemHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams && supportLoader)
        {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }
        else if(getItemViewType(position)== ViewTypes.VIEW_HEADER && itemHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
        {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }
        else if (itemHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
        {
            boolean isSnap = getSpanForItem(itemHolder,position,getItemViewType(position));
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(isSnap);
        }
        itemHolder.updateItemView(item, position);
        if(onUpdateItem!=null && onUpdateItem.get()!=null)
            onUpdateItem.get().update(itemHolder,item, position);
    }

    protected boolean getSpanForItem(BaseItemHolder itemHolder, int position,int viewType)
    {
        return false;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (getDataProvider().get(position) instanceof ProgressItem)
            return ViewTypes.VIEW_PROGRESS;
        else {
            int type = getListItemViewType(position, getDataProvider().get(position));
            if (type <= 0)
                type = ViewTypes.VIEW_ITEM;
            return type;
        }

    }

    protected int getListItemViewType(int position, Object item)
    {
        return ViewTypes.VIEW_ITEM;
    }


    protected ProgressItem getProgressItem()
    {
        return new ProgressItem();
    }

    public void onFetchingStart(boolean isFetchingRefresh)
    {
        if (supportLoader) {
            try {
                getDataProvider().add(getProgressItem());
            } catch (Exception e) {

            }

        }
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
        if (list.size() != 0 && list.get(list.size() - 1) instanceof ProgressItem && supportLoader)
        {
            list.remove(getDataProvider().size() - 1);
        }
    }

    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh)
    {

    }



    public void itemsAdded(final int position,final List items)
    {
        if(items!=null && items.size()!=0) {
            if(items.size()==1)
                notifyItemInserted(position);
            else
                notifyItemRangeInserted(position, items.size());

        }
    }
    public void itemsRemoved(int position,List items)
    {

        if(items!=null && items.size()!=0) {
            if(items.size()==1)
                notifyItemRemoved(position);
            else {
                notifyItemRangeRemoved(position, items.size());
            }
        }

    }
    public void itemsChanged(int position,List items)
    {
        if(items!=null && items.size()!=0) {
            if (position == 0)
                notifyDataSetChanged();
            else
                notifyItemRangeChanged(position,items.size());
        }
    }




    public void removeWatcher() {
        if(getDataProvider()!=null) {
            getDataProvider().removeFatchingObserve(this);
            getDataProvider().removeDataChangeWatcher(this);
        }
    }
    public void addWatcher() {
        if(getDataProvider()!=null) {
            getDataProvider().addFatchingObserve(this);
            getDataProvider().addDataChangeWatcher(this);
        }
    }

    public interface OnUpdateItem {
        void update(BaseItemHolder itemHolder, Object item, int position);
    }

    public static class ViewTypes {
        public static final int VIEW_PROGRESS = 0;
        public static final int VIEW_ITEM = 1;
        public static final int VIEW_HEADER = 99999999;
    }


    public class  ProgressViewHolder extends BaseItemHolder {
        public ProgressViewHolder(View itemView)
        {
            super(itemView);
        }
        public void updateItemView(Object item,View view,int position)
        {

        }
    }
}
