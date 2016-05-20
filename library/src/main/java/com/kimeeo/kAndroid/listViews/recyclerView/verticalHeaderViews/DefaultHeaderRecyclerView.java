/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kimeeo.kAndroid.listViews.recyclerView.verticalHeaderViews;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerViewAdapter;
import com.kimeeo.kAndroid.listViews.recyclerView.IViewProvider;

abstract public class DefaultHeaderRecyclerView extends BaseHeaderRecyclerView implements IViewProvider
{
    private final float DEFAULT_SCROLL_MULTIPLIER = 0.5f;
    HeaderObject headerObject = new HeaderObject();
    private float mScrollMultiplier = DEFAULT_SCROLL_MULTIPLIER;
    private OnParallaxScroll mParallaxScroll;
    private View headerView=null;

    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        mParallaxScroll = parallaxScroll;
        mParallaxScroll.onParallaxScroll(0, 0, headerView);
    }

    abstract public View getNormalItemView(int viewType,LayoutInflater inflater,ViewGroup container);

    abstract public BaseItemHolder getNormalItemHolder(int viewType, View view);

    public int getNormalItemViewType(int position,Object item){
        return BaseRecyclerViewAdapter.ViewTypes.VIEW_ITEM;
    }

    public View getItemView(int viewType,LayoutInflater inflater,ViewGroup container) {
        if(viewType==BaseRecyclerViewAdapter.ViewTypes.VIEW_HEADER)
        {
            View hView = createHeaderView(inflater,container,getHeaderObject());
            if(hView!=null) {
                if (getSupportParallex()) {
                    if (hView instanceof HeaderViewWrapper) {
                        headerView = hView;
                    } else{

                        HeaderViewWrapper headerViewWrapper= new HeaderViewWrapper(getActivity());
                        headerViewWrapper.addView(hView);

                        headerView= headerViewWrapper;
                    }
                } else
                    headerView = hView;
            }
            else {
                headerView = new View(container.getContext());
                headerView.setVisibility(View.GONE);
            }

            return headerView;
        }
        return getNormalItemView(viewType,inflater,container);
    }

    public BaseItemHolder getHeaderItemHolder(int viewType,View view) {
        return new HeaderItemHolder(view);
    }

    public boolean getSupportParallex() {
        return false;
    }

    public View createHeaderView(LayoutInflater inflater,ViewGroup container,Object data)
    {
        return null;
    }

    public View getHeaderView()
    {
        return headerView;
    }

    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        headerView=null;
    }

    protected void configRecyclerView(RecyclerView mList, BaseRecyclerViewAdapter mAdapter) {
        if(getSupportParallex()) {
            mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (headerView != null) {
                        RecyclerView.ViewHolder holder = getRecyclerView().findViewHolderForAdapterPosition(0);
                        if (holder != null)
                            translateHeader(-holder.itemView.getTop());
                    }
                }
            });
        }
    }

    public void translateHeader(float headerTop) {
        float translationAmount = headerTop * mScrollMultiplier;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            headerView.setTranslationY(translationAmount);
        } else {
            TranslateAnimation anim = new TranslateAnimation(0, 0, translationAmount, translationAmount);
            anim.setFillAfter(true);
            anim.setDuration(0);
            headerView.startAnimation(anim);
        }
        if(headerView instanceof HeaderViewWrapper)
            ((HeaderViewWrapper)headerView).setClipY(Math.round(translationAmount));
        if (mParallaxScroll != null) {
            float left = Math.min(1, ((translationAmount) / (headerView.getHeight() * mScrollMultiplier)));
            mParallaxScroll.onParallaxScroll(left, headerTop, headerView);
        }
    }

    @Override
    protected void configDataManager(DataProvider dataManager) {
        if(getHeaderObject()!=null && dataManager!=null) {
            dataManager.add(getHeaderObject());
            dataManager.setRefreshItemPos(1);
        }
    }

    public Object getHeaderObject()
    {
        return headerObject;
    }

    public BaseItemHolder getItemHolder(int viewType,View view) {
        if(viewType== BaseRecyclerViewAdapter.ViewTypes.VIEW_HEADER)
            return getHeaderItemHolder(viewType,view);
        else
            return getNormalItemHolder(viewType, view);
    }
    final public int getListItemViewType(int position,Object item) {
        if(position==0 && getHeaderObject()!=null)
            return BaseRecyclerViewAdapter.ViewTypes.VIEW_HEADER;
        else
            return getNormalItemViewType(position, item);
    }
    @Override
    protected BaseRecyclerViewAdapter createListViewAdapter(){
        return new DefaultHeaderRecyclerViewAdapter(getDataProvider(),this);
    }


    public interface OnParallaxScroll {
        void onParallaxScroll(float percentage, float offset, View parallax);
    }

    public static class HeaderObject{}

    // Update View Here
    public class HeaderItemHolder extends BaseItemHolder {

        public HeaderItemHolder(View itemView)
        {
            super(itemView);
        }

        public void updateItemView(Object item,View view,int position){

        }
    }
}
