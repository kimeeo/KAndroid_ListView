package com.kimeeo.kAndroid.listViewsDemo;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.StaticDataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
import com.kimeeo.kAndroid.listViews.recyclerView.itemDecoration.DefaultDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 02/05/16.
 */
public class ListView extends com.kimeeo.kAndroid.listViews.recyclerView.verticalViews.ListView {

    private int pageCount = 1;
    private int refreshPageCount = 1;

    @Override
    public void onItemClick(Object data) {
        //getDataProvider().remove(data);

        getDataProvider().set(0, new DataObject("UPDATED"));
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
    }

    @Override
    public View getItemView(int i, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.sample_list_view_item, viewGroup, false);

    }

    @Override
    public BaseItemHolder getItemHolder(int  i, View view){
        return new BaseItemHolder1(view);
    }

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

    public class BaseItemHolder1 extends BaseItemHolder
    {
        public BaseItemHolder1(View itemView) {
            super(itemView);
        }
        @Override
        public void updateItemView(Object o, View view, int i) {
            if(o instanceof DataObject) {
                DataObject data = (DataObject) o;
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText(data.name);
            }
        }
    }
}
