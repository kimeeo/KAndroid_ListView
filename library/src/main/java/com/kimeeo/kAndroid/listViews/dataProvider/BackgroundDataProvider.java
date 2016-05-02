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
    List listThreadSafe=null;
    public void addDataThreadSafe(final List list) {
        listThreadSafe =list;
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
            if(listThreadSafe!=null)
                addData(listThreadSafe);
            else
                dataLoadError(listThreadSafe);
            listThreadSafe = null;
        }
    }

}