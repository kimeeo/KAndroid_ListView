package com.kimeeo.kAndroid.listViewsDemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimeeo.kAndroid.core.fragment.BaseFragment;
import com.kimeeo.kAndroid.listViews.BaseListDataView;
import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.dataProvider.StaticDataProvider;
import com.kimeeo.kAndroid.listViews.fragmentStacks.BaseFragmentStacks;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.itemDecoration.DefaultDividerItemDecoration;
import com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles.BaseViewProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 02/05/16.
 */
public class FragmentStack extends BaseFragmentStacks
{

    private int pageCount = 1;
    private int refreshPageCount = 1;
    @Override
    protected BaseListDataView[] configFragments()
    {
        BaseListDataView[] list= new BaseListDataView[4];
        list[0] = (BaseListDataView)BaseFragment.newInstance(GridView.class);
        list[1] = (BaseListDataView)BaseFragment.newInstance(ListView.class);
        list[2] = (BaseListDataView)BaseFragment.newInstance(OldListView.class);
        list[3] = (BaseListDataView)BaseFragment.newInstance(HorizontalViewPager.class);
        return list;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater,container,savedInstanceState);

        setHasOptionsMenu(true);

        getActivity().supportInvalidateOptionsMenu();
        return rootView;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getTitle().equals("Grid"))
            setCurruntView(getFragments()[0]);
        else if(item.getTitle().equals("List"))
            setCurruntView(getFragments()[1]);
        else if(item.getTitle().equals("Old List"))
            setCurruntViewIndex(2);
        else if(item.getTitle().equals("Pager"))
            setCurruntViewIndex(3);
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.add("Grid");
        menu.add("List");
        menu.add("Old List");
        menu.add("Pager");
    }

    @NonNull
    @Override
    protected DataProvider createDataProvider()
    {
        StaticDataProvider1 data=new StaticDataProvider1();
        data.next();
        return data;
    }
    public class StaticDataProvider1 extends StaticDataProvider
    {
        int count = 1;
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List list=new ArrayList();
                for (int i = 0; i < 10; i++) {
                    list.add(new DataObject("Name "+ count));
                    count++;
                }
                addData(list);
            }
        };
        public StaticDataProvider1()
        {
            //setRefreshItemPos(1);
            //setNextItemPos(1);
            setNextEnabled(true);
            setRefreshEnabled(true);

        }

        @Override
        protected void invokeLoadNext() {
            if(pageCount!=10) {
                h.postDelayed(r,100);
                pageCount+=1;
            }
            else {
                setCanLoadNext(false);
                dataLoadError(null);
            }
        }

        @Override
        protected void invokeLoadRefresh() {
            if(refreshPageCount!=3) {
                h.postDelayed(r,2000);
                refreshPageCount+=1;
            }
            else {
                setCanLoadRefresh(false);
                dataLoadError(null);
            }
        }
    }
}
