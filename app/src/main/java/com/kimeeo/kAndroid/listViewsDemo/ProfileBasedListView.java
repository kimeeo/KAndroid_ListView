package com.kimeeo.kAndroid.listViewsDemo;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.StaticDataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseProfileRecyclerView;
import com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles.BaseViewProfile;
import com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles.HorizontalList;
import com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles.VerticalGrid;
import com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles.VerticalList;
import com.kimeeo.kAndroid.listViews.recyclerView.viewProfiles.VerticalStaggeredGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/12/16.
 */
public class ProfileBasedListView extends BaseProfileRecyclerView {

    List<BaseViewProfile> viewProfileList = null;

    private int pageCount=1;
    private int refreshPageCount=1;
    @NonNull
    @Override
    protected DataProvider createDataProvider()
    {
        return new StaticDataProvider1();
    }
    public class StaticDataProvider1 extends StaticDataProvider
    {
        int count = 1;
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List list=new ArrayList();
                for (int i = 0; i < 30; i++) {
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
            if(pageCount!=100) {
                h.postDelayed(r,2000);


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

    public void onViewCreated(View view) {
        if (viewProfileList == null) {
            viewProfileList = new ArrayList<>();
            viewProfileList.add(new MyVerticalListViewProfile("List View", this));
            viewProfileList.add(new MyGridViewProfile("Grid View", this));
            viewProfileList.add(new MyVerticalStaggeredGridViewProfile("Staggered View", this));
            viewProfileList.add(new MyHorizontalListViewProfile("H List View", this));
        }
        applyProfile(viewProfileList.get(0));
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
        next();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        for (BaseViewProfile viewProfile : viewProfileList) {
            menu.add(viewProfile.getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        for (BaseViewProfile viewProfile : viewProfileList) {
            if (item.getTitle().equals(viewProfile.getName())) {
                applyProfile(viewProfile);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public class MyVerticalListViewProfile extends VerticalList {
        public MyVerticalListViewProfile(String name, BaseProfileRecyclerView host) {
            super(name, host);
        }

        public View getItemView(int viewType, LayoutInflater inflater, ViewGroup container) {
            return inflater.inflate(R.layout.sample_list_view_item, null);
        }

        @Override
        public BaseItemHolder getItemHolder(int viewType, View view) {
            return new BaseItemHolder1(view);
        }
    }

    public class MyHorizontalListViewProfile extends HorizontalList {
        public MyHorizontalListViewProfile(String name, BaseProfileRecyclerView host) {
            super(name, host);
        }

        public View getItemView(int viewType, LayoutInflater inflater, ViewGroup container) {
            return inflater.inflate(R.layout.sample_list_view_item, null);
        }

        @Override
        public BaseItemHolder getItemHolder(int viewType, View view) {
            return new BaseItemHolder1(view);
        }
    }


    public class MyGridViewProfile extends VerticalGrid {
        public MyGridViewProfile(String name, BaseProfileRecyclerView host) {
            super(name, host);

        }
        public View getItemView(int viewType, LayoutInflater inflater, ViewGroup container) {
            return inflater.inflate(R.layout.sample_list_view_item, null);
        }

        @Override
        public BaseItemHolder getItemHolder(int viewType, View view) {
            return new BaseItemHolder1(view);
        }
    }

    public class MyVerticalStaggeredGridViewProfile extends VerticalStaggeredGrid {
        public MyVerticalStaggeredGridViewProfile(String name, BaseProfileRecyclerView host) {
            super(name, host);
        }


        public View getItemView(int viewType, LayoutInflater inflater, ViewGroup container) {
            return inflater.inflate(R.layout.sample_list_view_item, null);
        }

        // get New BaseItemHolder
        @Override
        public BaseItemHolder getItemHolder(int viewType, View view) {
            return new BaseItemHolder1(view);
        }
    }

    public class BaseItemHolder1 extends BaseItemHolder {
        public BaseItemHolder1(View itemView) {
            super(itemView);
        }

        @Override
        public void updateItemView(Object o, View view, int i) {
            DataObject data = (DataObject) o;
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(i + ". " + data.name);
        }
    }
}
