package com.kimeeo.kAndroid.listViews.fragmentStacks;
import android.os.Bundle;

import com.kimeeo.kAndroid.core.utils.IDUtil;
import com.kimeeo.kAndroid.listViews.BaseListDataView;
import com.kimeeo.kAndroid.listViews.R;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

/**
 * Created by BhavinPadhiyar on 3/26/17.
 */

import android.os.Bundle;


import com.kimeeo.kAndroid.listViews.BaseListDataView;
import com.kimeeo.kAndroid.listViews.R;
import com.kimeeo.kAndroid.listViews.listView.BaseListView;
import com.kimeeo.kAndroid.listViews.pager.BaseViewPager;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerView;

import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by BhavinPadhiyar on 3/26/17.
 */

abstract public class BaseFragmentStacks extends BaseListDataView
{
    public static final int ADDING_METHOD_ADD_STACK = 1;
    public static final int ADDING_METHOD_ADD_NEW = 2;
    private Map<BaseListDataView,Integer> fragmentsMap=new HashMap<>();
    private BaseListDataView[] fragments;
    private Runnable addingObject=new Runnable() {
        @Override
        public void run() {
            addingFragments();
        }
    };
    private Runnable resizeObject=new Runnable() {
        @Override
        public void run() {
            resizingFragments();
        }
    };
    protected void resizingFragments() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        for (BaseListDataView fragment : fragments) {
            if(fragment!=null)
                fragment.getView().setLayoutParams(createFragmentHolderLayoutFor(fragment));
        }
        transaction.commit();
        configFragements(fragments);
        viewCreated=true;
        onViewComplete();
    }
    protected void onViewComplete() {

    }
    public boolean isViewCreated() {
        return viewCreated;
    }
    private boolean viewCreated=false;
    protected void addingFragments() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        for (BaseListDataView fragment : fragments) {
            if(fragment!=null) {
                fragment.setDataProvider(getDataProvider());
                if (getAddMethod() == ADDING_METHOD_ADD_NEW) {
                    Integer id = fragmentsMap.get(fragment);
                    transaction.replace(id, fragment);
                } else {
                    if(isDefaultVisible(fragment))
                        transaction.show(fragment);
                    else
                        transaction.hide(fragment);
                    transaction.add(R.id.stackHolder, fragment);
                }
            }
        }
        transaction.commit();
        configFragements(fragments);
        Handler handler=new Handler();
        handler.postDelayed(resizeObject,50);
    }

    protected boolean isDefaultVisible(BaseListDataView fragment) {
        return false;
    }

    protected  void configFragements(BaseListDataView[] fragments){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout rootView = createRootView(inflater,container,savedInstanceState);
        fragments = configFragments();
        if(fragments!=null)
        {
            for (BaseListDataView fragment : fragments) {
                if(fragment!=null) {
                    if (getAddMethod() == ADDING_METHOD_ADD_NEW) {
                        int id = generateRandomId(333333333,999999999);
                        RelativeLayout fragmentHolder = createFragmentHolderFor(fragment,inflater,savedInstanceState);
                        fragmentHolder.setId(id);
                        rootView.addView(fragmentHolder);
                        fragmentsMap.put(fragment,id);
                    }
                }
            }
            Handler handler=new Handler();
            handler.postDelayed(addingObject,300);
        }
        return rootView;
    }
    protected FrameLayout createRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (FrameLayout)inflater.inflate(getRootViewLayoutRes(), container, false);
    }
    @LayoutRes
    protected int getRootViewLayoutRes() {
        return R.layout._fragment_stack_holder;
    }
    protected int getAddMethod() {
        return ADDING_METHOD_ADD_NEW;
    }
    protected RelativeLayout createFragmentHolderFor(BaseListDataView fragment,LayoutInflater inflater,Bundle savedInstanceState) {
        RelativeLayout fragmentHolder=new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        fragmentHolder.setLayoutParams(layoutParams);
        return fragmentHolder;
    }
    protected ViewGroup.LayoutParams createFragmentHolderLayoutFor(BaseListDataView fragment) {
        ViewGroup.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        if(fragment.getView().getLayoutParams()!=null && fragment.getView().getLayoutParams() instanceof RelativeLayout.LayoutParams)
            layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        else if(fragment.getView().getLayoutParams()!=null && fragment.getView().getLayoutParams() instanceof FrameLayout.LayoutParams)
            layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        else if(fragment.getView().getLayoutParams()!=null && fragment.getView().getLayoutParams() instanceof LinearLayout.LayoutParams)
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        return layoutParams;
    }
    public void showOnlyFragments(BaseListDataView fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < getFragments().length; i++) {
            transaction.hide(getFragments()[i]);
        }
        transaction.show(fragment);
        transaction.commit();
    }
    public void showFragments(BaseListDataView fragment) {
        if(fragment!= null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        }
    }
    public void hideFragments(BaseListDataView fragment) {
        if(fragment!= null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.hide(fragment);
            transaction.commit();
        }
    }
    public void toggleFragments(BaseListDataView fragment) {
        if(fragment!= null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if(fragment.isVisible())
                transaction.hide(fragment);
            else
                transaction.show(fragment);
            transaction.commit();
        }
    }
    public void syncViews(int position) {
        BaseListDataView[] fragments= getFragments();
        for (int i = 0; i < fragments.length; i++) {
            if(fragments[i] instanceof BaseRecyclerView)
                ((BaseRecyclerView)fragments[i]).getRecyclerView().scrollToPosition(position);
            else if(fragments[i] instanceof BaseViewPager)
                ((BaseViewPager)fragments[i]).gotoItem(position,false);
            else if(fragments[i] instanceof BaseListView)
                ((BaseListView)fragments[i]).getListView().smoothScrollToPosition(position);
        }
    }
    protected abstract BaseListDataView[] configFragments();
    public BaseListDataView[] getFragments() {return fragments;}
    @Override
    public void itemsAdded(int index, List items) {}
    @Override
    public void itemsRemoved(int index, List items) {}
    @Override
    public void itemsChanged(int index, List items) {}
    @Override
    public void onFetchingStart(boolean isFetchingRefresh) {}
    @Override
    public void onFetchingFinish(boolean isFetchingRefresh) {}
    @Override
    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh) {}
    @Override
    public void onFetchingError(Object error) {}

    static Random r = new Random();
    public static int generateRandomId(int min,int max) {
        return r.nextInt(max - min + 1) + min;
    }
}

