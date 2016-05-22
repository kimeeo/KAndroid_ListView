package com.kimeeo.kAndroid.listViewsDemo;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.StaticDataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 02/05/16.
 */
public class HeaderListView extends com.kimeeo.kAndroid.listViews.recyclerView.verticalHeaderViews.ResponsiveView {


    private int pageCount=1;
    private int refreshPageCount=1;

    @Override
    public View getNormalItemView(int viewType, LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.sample_list_view_item,container,false);
    }

    @Override
    public BaseItemHolder getNormalItemHolder(int viewType, View view) {
        return new BaseItemHolder1(view);
    }

    @Override
    public View createHeaderView(LayoutInflater inflater,ViewGroup container,Object data)
    {
        View view =inflater.inflate(R.layout.fragment_header_page_view, container,false);
        return view;
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
            DataObject data = (DataObject) o;
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(data.name);
        }
    }
}
