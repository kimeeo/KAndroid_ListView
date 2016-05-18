package com.kimeeo.kAndroid.listViews;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.StaticDataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;
/**
 * Created by BhavinPadhiyar on 02/05/16.
 */
public class ListView extends com.kimeeo.kAndroid.listViews.recyclerView.verticalViews.ListView {
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

    }

    public class BaseItemHolder1 extends BaseItemHolder
    {
        public BaseItemHolder1(View itemView) {
            super(itemView);
        }
        @Override
        public void updateItemView(Object o, View view, int i) {
           // DataBean data = (DataBean) o;
            //TextView title = (TextView) view.findViewById(R.id.title);
            //title.setText(i +". "+data.getName());
        }
    }
}
