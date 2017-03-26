package com.kimeeo.kAndroid.listViews.fragmentStacks;
import android.os.Bundle;

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

abstract public class BaseFragmentStacks extends BaseListDataView
{
    private FragmentManager fragmentManager;
    private BaseListDataView[] fragments;
    private int curruntViewIndex=0;
    private BaseListDataView curruntView;





    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout._fragment_stack_holder, container, false);
        fragments = configFragments();
        if(fragments!=null)
        {
            fragmentManager=getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (BaseListDataView fragment : fragments) {
                int id = IDUtil.generateViewId();
                FrameLayout frameLayout=new FrameLayout(getActivity());
                FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
                frameLayout.setLayoutParams(layoutParams);
                frameLayout.setId(id);
                rootView.addView(frameLayout);
                fragment.setDataProvider(getDataProvider());
                transaction.replace(id, fragment);
                transaction.hide(fragment);
            }
            transaction.commit();
            setCurruntViewIndex(getCurruntViewIndex());
        }
        return rootView;
    }

    protected abstract BaseListDataView[] configFragments();

    public BaseListDataView getCurruntView() {
        return curruntView;
    }

    public void setCurruntView(BaseListDataView value) {
        if(value!=curruntView) {
            for (int i = 0; i < fragments.length; i++) {
                if (fragments[i] == value) {
                    setCurruntViewIndex(i);
                    break;
                }
            }
        }
    }

    public int getCurruntViewIndex() {
        return curruntViewIndex;
    }

    public void setCurruntViewIndex(int value) {
        BaseListDataView[] fragments = getFragments();
        if(fragments!= null && value>=0 && value<fragments.length) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (curruntViewIndex >= 0 && curruntViewIndex < fragments.length) {
                BaseListDataView fragment = fragments[curruntViewIndex];
                transaction.hide(fragment);
            }
            curruntViewIndex = value;

            if (curruntViewIndex >= 0 && curruntViewIndex < fragments.length) {
                BaseListDataView fragment = fragments[curruntViewIndex];
                fragment.setDataProvider(getDataProvider());
                transaction.show(fragment);
                curruntView = fragment;
            }
            transaction.commit();
        }
    }

    @Override
    public void itemsAdded(int index, List items) {
        /*
        BaseListDataView[] fragments=getFragments();
        if(fragments!=null) {
            for (BaseListDataView fragment : fragments) {
                fragment.itemsAdded(index,items);
            }
        }*/
    }

    @Override
    public void itemsRemoved(int index, List items) {
        /*
        BaseListDataView[] fragments=getFragments();
        if(fragments!=null) {
            for (BaseListDataView fragment : fragments) {
                fragment.itemsRemoved(index,items);
            }
        }*/
    }

    @Override
    public void itemsChanged(int index, List items) {
        /*
        BaseListDataView[] fragments=getFragments();
        if(fragments!=null) {
            for (BaseListDataView fragment : fragments) {
                fragment.itemsChanged(index,items);
            }
        }*/
    }

    @Override
    public void onFetchingStart(boolean isFetchingRefresh) {
        /*
        BaseListDataView[] fragments=getFragments();
        if(fragments!=null) {
            for (BaseListDataView fragment : fragments) {
                fragment.onFetchingStart(isFetchingRefresh);
            }
        }*/
    }

    @Override
    public void onFetchingFinish(boolean isFetchingRefresh) {
        /*
        BaseListDataView[] fragments=getFragments();
        if(fragments!=null) {
            for (BaseListDataView fragment : fragments) {
                fragment.onFetchingFinish(isFetchingRefresh);
            }
        }*/
    }

    @Override
    public void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh) {
        /*
        BaseListDataView[] fragments=getFragments();
        if(fragments!=null) {
            for (BaseListDataView fragment : fragments) {
                fragment.onFetchingEnd(dataList,isFetchingRefresh);
            }
        }*/
    }

    @Override
    public void onFetchingError(Object error) {
        /*
        BaseListDataView[] fragments=getFragments();
        if(fragments!=null) {
            for (BaseListDataView fragment : fragments) {
                fragment.onFetchingError(error);
            }
        }*/
    }

    public BaseListDataView[] getFragments() {
        return fragments;
    }
}
