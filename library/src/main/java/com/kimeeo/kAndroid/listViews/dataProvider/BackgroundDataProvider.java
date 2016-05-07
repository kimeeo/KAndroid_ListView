package com.kimeeo.kAndroid.listViews.dataProvider;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.List;


/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
abstract public class BackgroundDataProvider extends DataProvider
{
    public BackgroundDataProvider()
    {

    }

    protected boolean loadNext() {
        if(!isFetching && getCanLoadNext() && isFirstCall) {
            isFetching = true;
            isFetchingRefresh = false;
            onFetchingStart(isFetchingRefresh);
            BackgroundTask backgroundTask = new BackgroundTask();
            backgroundTask.execute(false);
            return true;
        } else if(!isFetching && getCanLoadNext() && getNextEnabled()) {
            isFetching = true;
            isFetchingRefresh = false;
            onFetchingStart(isFetchingRefresh);
            BackgroundTask backgroundTask = new BackgroundTask();
            backgroundTask.execute(false);
            return true;
        } else {
            onFetchingEnd(null, false);
            return false;
        }
    }

    protected boolean loadRefresh() {
        if(!isFetching && getCanLoadRefresh() && getRefreshEnabled()) {
            isFetching = true;
            isFetchingRefresh = true;
            onFetchingStart(isFetchingRefresh);
            BackgroundTask backgroundTask = new BackgroundTask();
            backgroundTask.execute(true);
            return true;
        } else {
            onFetchingEnd(null,isFetchingRefresh);
            return false;
        }
    }

    private class BackgroundTask extends AsyncTask<Boolean, Void, Boolean> {

        public BackgroundTask()
        {

        }
        protected void onPreExecute() {

        }
        @Override
        protected Boolean doInBackground(Boolean... params) {
            boolean isRefresh =params[0];
            if(isRefresh)
                invokeLoadRefresh();
            else
                invokeLoadNext();
            return true;
        }
        @Override
        protected void onPostExecute(Boolean data) {

        }
    }

    public void processDataManager(DataModel data) {
        if(data!=null && data.getDataProvider()!=null)
            addDataThreadSafe(data.getDataProvider());
        else
            dataLoadErrorThreadSafe(null);
    }

    public void processDataManagerThreadSafe(final DataModel data) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                processDataManager(data);
            }
        });
    }

    public void addDataThreadSafe(final List list)
    {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                addData(list);
            }
        });

    }
    public void dataLoadErrorThreadSafe(final Object status)
    {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                dataLoadError(status);
            }
        });
    }
}