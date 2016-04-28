package com.kimeeo.kAndroid.listViews.recyclerView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.kimeeo.kAndroid.core.utils.DeviceUtilities;


/**
 * Created by bhavinpadhiyar on 2/1/16.
 */
public class GridHelper {

    private IColoumProvider coloumProvider;
    public void garbageCollectorCall() {
        coloumProvider=null;
    }
    public int getColumnsCount() {
        return columnCount;
    }
    private int columnCount;
    public GridHelper(IColoumProvider coloumProvider, Context context) {
        this.coloumProvider =coloumProvider;
        int count;
        if(DeviceUtilities.isTablet(context)){
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            float widthInInches = metrics.widthPixels / metrics.xdpi;
            float heightInInches = metrics.heightPixels / metrics.ydpi;
            double sizeInInches = Math.sqrt(Math.pow(widthInInches, 2) + Math.pow(heightInInches, 2));
            boolean is7inchTablet = sizeInInches >= 6.5 && sizeInInches <= 8;

            if(is7inchTablet)
                count = coloumProvider.getColumnsTablet7();
            else
                count = coloumProvider.getColumnsTablet10();
        }
        else
            count = coloumProvider.getColumnsPhone();
        columnCount =count;
    }
    public void configLayoutManager(RecyclerView.LayoutManager layoutManager){
        if(layoutManager instanceof GridLayoutManager)
        {
            GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
            gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        }
        else if(layoutManager instanceof StaggeredGridLayoutManager)
        {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager)layoutManager;
            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        }
    }
    GridLayoutManager.SpanSizeLookup spanSizeLookup= new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            int viewType = coloumProvider.getAdapter().getItemViewType(position);
            switch(viewType){
                case BaseRecyclerViewAdapter.ViewTypes.VIEW_PROGRESS:
                    return getColumnsCount();
                case BaseRecyclerViewAdapter.ViewTypes.VIEW_HEADER:
                    return getColumnsCount();
                default:
                    return getSpanSizeForItem(position, viewType,coloumProvider.getAdapter().getDataProvider().get(position));
            }
        }
    };
    protected int getSpanSizeForItem(int position,int viewType,Object baseObject){
        if(baseObject instanceof ISpanSizeAware)
        {
            ISpanSizeAware spanSizeAware = (ISpanSizeAware)baseObject;
            int size = spanSizeAware.getSpanSize();
            if(size == ISpanSizeAware.FULL)
                return  getColumnsCount();
            else if(size == ISpanSizeAware.HALF)
                return getColumnsCount()/2;
            else if(size == ISpanSizeAware.QUARTER)
                return getColumnsCount()/4;
            else
                return coloumProvider.getSpanSizeForItem(position,viewType,baseObject);
        }
        return coloumProvider.getSpanSizeForItem(position,viewType,baseObject);
    }
    public  static interface IColoumProvider{
        int getColumnsCount();
        int getColumnsPhone();
        int getColumnsTablet10();
        int getColumnsTablet7();
        int getSpanSizeForItem(int position, int viewType, Object baseObject);
        BaseRecyclerViewAdapter getAdapter();
    }
}
