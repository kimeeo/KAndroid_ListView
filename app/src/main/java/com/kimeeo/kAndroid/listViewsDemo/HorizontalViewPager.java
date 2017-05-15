package com.kimeeo.kAndroid.listViewsDemo;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.dataProvider.StaticDataProvider;
import com.kimeeo.kAndroid.listViews.pager.BaseItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 22/05/16.
 */
public class HorizontalViewPager extends com.kimeeo.kAndroid.listViews.pager.viewPager.HorizontalViewPager {


    private int pageCount = 1;
    private int refreshPageCount = 1;

    public String getItemTitle(int position, Object o) {
        if (o instanceof DataObject) {
            DataObject data = (DataObject) o;
            return data.name;
        }
        return "";
    }

    @Override
    public View getView(int i, Object o) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sample_list_view_item, null);
        return view;
    }

    @NonNull
    @Override
    protected DataProvider createDataProvider() {
        return new StaticDataProvider1();
    }

    @Override
    public BaseItemHolder getItemHolder(View view, int i, Object o) {
        return new BaseItemHolder1(view);
    }

    public class StaticDataProvider1 extends StaticDataProvider {
        int count = 1;
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List list = new ArrayList();
                for (int i = 0; i < 10; i++) {
                    list.add(new DataObject("Name " + count));
                    count++;
                }
                addData(list);
            }
        };

        public StaticDataProvider1() {
            //setRefreshItemPos(1);
            //setNextItemPos(1);
            setNextEnabled(true);
            setRefreshEnabled(true);

        }

        @Override
        protected void invokeLoadNext() {
            if (pageCount != 10) {
                h.postDelayed(r, 2000);


                pageCount += 1;
            } else {
                setCanLoadNext(false);
                dataLoadError(null);
            }
        }

        @Override
        protected void invokeLoadRefresh() {
            if (refreshPageCount != 3) {
                h.postDelayed(r, 2000);
                refreshPageCount += 1;
            } else {
                setCanLoadRefresh(false);
                dataLoadError(null);
            }
        }
    }

    public class BaseItemHolder1 extends BaseItemHolder {
        public BaseItemHolder1(View itemView) {
            super(itemView);
        }
        @Override
        public void cleanView(View view, int i) {

        }
        @Override
        public void updateItemView(Object o, View view, int i) {
            if(o instanceof DataObject) {
                DataObject data = (DataObject) o;
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText(i + ". " + data.name);
            }
        }
    }
}
