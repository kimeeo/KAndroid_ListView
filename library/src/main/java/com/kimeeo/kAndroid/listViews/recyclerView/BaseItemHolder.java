package com.kimeeo.kAndroid.listViews.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
abstract public class BaseItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    abstract public void updateItemView(Object item,View itemView, int position);
    public void setOnItemHolderClick(OnItemHolderClick onItemHolderClick) {
        this.onItemHolderClick = onItemHolderClick;
        if(onItemHolderClick!=null)
            setItemClickSupport(true);
        else
            setItemClickSupport(false);
    }
    private OnItemHolderClick onItemHolderClick;
    public View getItemView()
    {
        return itemView;
    }
    public void updateItemView(Object item, int position){
        updateItemView(item,itemView,position);
    }
    public void setItemClickSupport(boolean support) {
        if(support)
            getItemView().setOnClickListener(this);
        else
            getItemView().setOnClickListener(null);
    }
    public BaseItemHolder(View itemView,boolean clickSupport) {
        super(itemView);
        if(clickSupport)
            itemView.setOnClickListener(this);
    }

    public BaseItemHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(onItemHolderClick!=null)
            onItemHolderClick.onItemHolderClick(this, getAdapterPosition());
    }
    public static interface OnItemHolderClick{
        void onItemHolderClick(BaseItemHolder itemHolder, int position);
    }
}
