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
    public void addDataInThread(final List list) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                addData(list);
            }});
    }
}