package com.kimeeo.kAndroid.listViews.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.MonitorList;

import java.util.List;

/**
 * Created by bhavinpadhiyar on 7/21/15.
 */
abstract public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseItemHolder> implements MonitorList.OnChangeWatcher, BaseItemHolder.OnItemHolderClick,DataProvider.OnFatchingObserve {

    private static final String TAG = "BaseRecyclerViewAdapter";
    public boolean supportLoader = true;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private DataProvider dataProvider;

    public BaseRecyclerViewAdapter(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.dataProvider.addFatchingObserve(this);
        this.dataProvider.addDataChangeWatcher(this);
    }
    public void garbageCollectorCall()
    {
        dataProvider=null;
        mOnItemClickListener=null;
    }

    protected DataProvider getDataProvider()
    {
        return dataProvider;
    }

    abstract protected View getItemView(int viewType,LayoutInflater inflater,ViewGroup container);
    abstract protected BaseItemHolder getItemHolder(int viewType,View view);

    public void itemsChanged(int position,List items)
    {
        if(items!=null && items.size()!=0) {
            if (position == 0)
                notifyDataSetChanged();
            else
                notifyItemRangeChanged(position,items.size());
        }
    }

    public void itemsAdded(int position,List items)
    {
        if(items!=null && items.size()!=0) {
            if (position == 0)
                notifyDataSetChanged();
            else
                notifyItemRangeInserted(position, items.size());
        }
    }
    public void itemsRemoved(int position,List items)
    {
        if(items!=null && items.size()!=0) {
            if (position == 0)
                notifyDataSetChanged();
            else
                notifyItemRangeRemoved(position, items.size());
        }
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
            root = getProgressItem(viewType,inflater, container);
            itemHolder = new ProgressViewHolder(root);
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

    private boolean getItemClickSupport() {
        return true;
    }

    protected View getProgressItem(int viewType,LayoutInflater inflater,ViewGroup container)
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
        return getDataProvider().get(position) instanceof ProgressItem ? ViewTypes.VIEW_PROGRESS : getListItemViewType(position, getDataProvider().get(position));
    }

    protected int getListItemViewType(int position, Object item)
    {
        return ViewTypes.VIEW_ITEM;
    }




    public void onFetchingStart(boolean isFetchingRefresh)
    {
        if (supportLoader) {
            try {
                getDataProvider().add(new ProgressItem());
                notifyItemInserted(getDataProvider().size());
            } catch (Exception e) {

            }

        }
    }
    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh)
    {
        List<Object> list = getDataProvider();
        if (list.size() != 0 && list.get(list.size() - 1) instanceof ProgressItem && supportLoader) {
            getDataProvider().remove(getDataProvider().size() - 1);
            notifyItemRemoved(getDataProvider().size());
        }
    }
    public void onFetchingError(Object error)
    {
        List<Object> list = getDataProvider();
        if (list.size() != 0 && list.get(list.size() - 1) instanceof ProgressItem && supportLoader) {
            getDataProvider().remove(getDataProvider().size() - 1);
            notifyItemRemoved(getDataProvider().size());
        }
    }



    final public void onCallEnd(List<?> dataList, boolean isRefreshPage)
    {
        if (dataList != null && dataList.size() != 0) {
            if (isRefreshPage) {
                notifyItemRangeInserted(getDataProvider().getRefreshItemPos(), dataList.size());
            } else
                notifyItemRangeInserted(getDataProvider().size() - dataList.size(), dataList.size());
        }
    }


    public static class ViewTypes {
        public static final int VIEW_PROGRESS = 0;
        public static final int VIEW_ITEM = 1;
        public static final int VIEW_HEADER = -1;
    }

    public static class ProgressItem
    {

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
