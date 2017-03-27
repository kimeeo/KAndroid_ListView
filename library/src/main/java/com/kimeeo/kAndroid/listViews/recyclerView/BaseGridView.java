package com.kimeeo.kAndroid.listViews.recyclerView;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import com.kimeeo.kAndroid.listViews.ProgressItem;

import java.util.List;

/**
 * Created by bhavinpadhiyar on 7/20/15.
 */
abstract public class BaseGridView extends DefaultRecyclerView implements GridHelper.IColoumProvider
{
    private GridHelper gridHelper;
    public int getColumnsCount(){
        if(gridHelper!=null)
            return gridHelper.getColumnsCount();
        return getColumnsPhone();
    }
    public int getColumnsPhone() {
        return 2;
    }
    public int getColumnsTablet10() {
        return 5;
    }

    public int getColumnsTablet7() {
        return 3;
    }
    public int getSpanSizeForItem(int position,int viewType,Object baseObject)
    {
        return 1;
    }
    protected void garbageCollectorCall() {
        super.garbageCollectorCall();
        if(gridHelper!=null)
            gridHelper.garbageCollectorCall();
        gridHelper=null;
    }
    @Override
    protected void configViewParam(){
        super.configViewParam();
        gridHelper = new GridHelper(this,getActivity());
    }
    protected void configLayoutManager(RecyclerView.LayoutManager layoutManager){
        gridHelper.configLayoutManager(layoutManager);
    }

    protected BaseRecyclerViewAdapter createListViewAdapter() {
        return new DefaultRecyclerVerticleViewAdapter(getDataProvider(), this);
    }


    public void itemsRemoved(final int index,final List items){


        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if(recyclerView!=null && recyclerView.getItemAnimator()!=null) {
                    if(items.get(0) instanceof ProgressItem) {
                        recyclerView.getItemAnimator().setRemoveDuration(1);
                        recyclerView.getAdapter().notifyItemRangeChanged(index,100);
                    }
                    else
                        recyclerView.getItemAnimator().setRemoveDuration(getItemAnimatorDuration());
                }
            }
        };
        h.postDelayed(r, 100);

        if (getEmptyViewHelper() != null)
            getEmptyViewHelper().updateView(getDataProvider());
    }
}
