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

    @Override
    public boolean refresh()
    {
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(true);
        return true;
    }
    @Override
    public boolean next()
    {
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(false);
        return true;
    }

    @Override
    final protected void invokeLoadNext(){}
    @Override
    final protected void invokeLoadRefresh(){}

    private class BackgroundTask extends AsyncTask<Boolean, Void, DataModel> {

        public BackgroundTask()
        {

        }
        protected void onPreExecute() {

        }
        @Override
        protected DataModel doInBackground(Boolean... params) {
            boolean isRefresh =params[0];
            if(isRefresh)
                return loadDataModelRefresh();
            else
                return loadDataModelNext();
        }
        @Override
        protected void onPostExecute(DataModel data) {
            if(data!=null && data.getDataProvider()!=null)
                addDataThreadSafe(data.getDataProvider());
            else
                dataLoadErrorThreadSafe(null);
        }
    }

    protected DataModel loadDataModelNext() {
        if(isFetching==false && getCanLoadNext() && isFirstCall) {
            isFetching=true;
            isFetchingRefresh=false;
            onFetchingStart(isFetchingRefresh);
            return getDataModelLoadNext();
        }
        else if(isFetching==false && getCanLoadNext() && getNextEnabled()) {
            isFetching=true;
            isFetchingRefresh=false;
            onFetchingStart(isFetchingRefresh);
            return getDataModelLoadNext();
        }
        else {
            onFetchingEnd(null,false);
            return null;
        }
    }

    protected DataModel loadDataModelRefresh() {
        if(isFetching==false && getCanLoadRefresh() && getRefreshEnabled()) {
            isFetching=true;
            isFetchingRefresh=true;
            onFetchingStart(isFetchingRefresh);
            return getDataModelLoadRefresh();
        }
        else {
            onFetchingEnd(null,isFetchingRefresh);
            return null;
        }
    }


    protected DataModel getDataModelLoadNext()
    {
        return null;
    }
    protected DataModel getDataModelLoadRefresh()
    {
        return null;
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

    public void addData(List list) {
        super.addData(list);
    }
    /*
    boolean inLoop;
    @Override
    public void addData(final List list) {
        if(inLoop)
        {
            inLoop=false;
            super.addData(list);
        }
        else
        {
            inLoop=true;
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(list!=null)
                        BackgroundDataProvider.this.addData(list);
                    else
                        BackgroundDataProvider.this.dataLoadError(list);
                }
            });


        }
    }
    */
}