package com.kimeeo.kAndroid.listViews.recyclerView.adapterLayout.layouts;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.commit451.adapterlayout.AdapterLayoutDelegate;
import com.kimeeo.kAndroid.listViews.recyclerView.adapterLayout.IAdapterLayoutView;

/**
 * Created by bhavinpadhiyar on 2/16/16.
 */
public class AdapterLinearLayout extends android.widget.LinearLayout implements IAdapterLayoutView {
    public AdapterLinearLayout(Context context) {
        super(context);
    }
    public AdapterLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AdapterLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(21)
    public AdapterLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*Copy From Here*/
    private AdapterLayoutDelegate mAdapterLayoutDelegate;
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mAdapterLayoutDelegate == null) {
            mAdapterLayoutDelegate = new AdapterLayoutDelegate(this);
        }
        mAdapterLayoutDelegate.setAdapter(adapter);
    }

    @Nullable
    public RecyclerView.Adapter getAdapter() {
        if (mAdapterLayoutDelegate != null) {
            return mAdapterLayoutDelegate.getAdapter();
        }
        return null;
    }

    @Nullable
    public RecyclerView.ViewHolder getViewHolderAt(int index) {
        return mAdapterLayoutDelegate.getViewHolderAt(index);
    }
    /*Copy To Here*/
}
