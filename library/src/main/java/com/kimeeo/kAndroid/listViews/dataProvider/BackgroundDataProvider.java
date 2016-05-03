package com.kimeeo.kAndroid.listViews.dataProvider;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
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
                loadRefresh();
            else
                loadNext();
            return true;
        }
        @Override
        protected void onPostExecute(Boolean data) {

        }
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
}