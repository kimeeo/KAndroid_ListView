package com.kimeeo.kAndroid.listViews;

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
public class ListView extends com.kimeeo.kAndroid.listViews.recyclerView.verticalViews.ListView {
    private int pageCount=1;

    @Override
    public View getItemView(int i, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.sample_list_view_item,viewGroup,false);
    }
    @Override
    public BaseItemHolder getItemHolder(int i, View view){
        return new BaseItemHolder1(view);
    }

    @NonNull
    @Override
    protected DataProvider createDataProvider()
    {
        StaticDataProvider1 data = new StaticDataProvider1();
        return data;
    }
    public class StaticDataProvider1 extends StaticDataProvider
    {
        public StaticDataProvider1()
        {
            add(new DataObject("Name 1"));
            add(new DataObject("Name 2"));
            add(new DataObject("Name 3"));
            add(new DataObject("Name 4"));
            add(new DataObject("Name 5"));
            add(new DataObject("Name 6"));
            add(new DataObject("Name 7"));
            add(new DataObject("Name 8"));
            add(new DataObject("Name 9"));
            add(new DataObject("Name 10"));
        }

        @Override
        protected void invokeLoadNext() {
            if(pageCount!=5) {
                List list=new ArrayList();
                for (int i = 0; i < 10; i++) {
                    list.add(new DataObject("Name "+pageCount+i));
                }
                addData(list);
                pageCount+=1;
            }
            else
                dataLoadError(null);
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
            title.setText(i +". "+data.name);
        }
    }
}
