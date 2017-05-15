package com.kimeeo.kAndroid.listViewsDemo;


import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kimeeo.kAndroid.dataProvider.DataProvider;
import com.kimeeo.kAndroid.dataProvider.StaticDataProvider;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
public class VerticalGrid extends com.kimeeo.kAndroid.listViews.recyclerView.stickyRecyclerHeaders.VerticalGrid {

    private int pageCount = 1;
    private int refreshPageCount = 1;

    @Override
    public View getStickyItemView(ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        return inflater.inflate(R.layout.sample_list_view_item, container, false);
    }

    @Override
    public BaseItemHolder getStickyItemHolder(View view) {
        return new StickyItemHolder1(view);
    }

    @Override
    public long getHeaderId(int position) {
        if (position < 5)
            return 1;
        else if (position < 10)
            return 2;
        else if (position < 20)
            return 3;
        else if (position < 25)
            return 4;
        return 5;
    }

    @Override
    public View getItemView(int i, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.sample_list_view_item, viewGroup, false);
    }

    @Override
    public BaseItemHolder getItemHolder(int i, View view) {
        return new BaseItemHolder1(view);
    }

    @Override
    public int getListItemViewType(int i, Object o) {
        return 0;
    }

    @NonNull
    @Override
    protected DataProvider createDataProvider() {
        return new StaticDataProvider1();
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
        public void updateItemView(Object o, View view, int i) {
            DataObject data = (DataObject) o;
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(i + ". " + data.name);
        }
    }

    public class StickyItemHolder1 extends BaseItemHolder {

        public StickyItemHolder1(View itemView) {
            super(itemView);
        }

        public void updateItemView(Object item, View view, int position) {
            DataObject data = (DataObject) item;
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(position + ". " + data.name);
            title.setTextColor(Color.parseColor("#ffffff"));
            view.setBackgroundResource(R.color.colorPrimary);
        }
    }
}
