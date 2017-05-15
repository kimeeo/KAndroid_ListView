package com.kimeeo.kAndroid.listViewsDemo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;

/**
 * Created by BhavinPadhiyar on 02/05/16.
 */
public class HeaderListView extends com.kimeeo.kAndroid.listViews.recyclerView.verticalHeaderViews.ResponsiveView {
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
