package com.kimeeo.kAndroid.listViewsDemo;

import android.os.Handler;

import com.kimeeo.kAndroid.listViews.dataProvider.StaticDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 23/05/16.
 */
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
    private int pageCount = 1;
    private int refreshPageCount = 1;

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