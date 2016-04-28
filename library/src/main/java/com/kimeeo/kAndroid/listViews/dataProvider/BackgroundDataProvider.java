package com.kimeeo.kAndroid.listViews.dataProvider;

import android.os.AsyncTask;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
abstract public class BackgroundDataProvider extends DataProvider
{
    BackgroundTask backgroundTask;
    public BackgroundDataProvider()
    {
        backgroundTask=new BackgroundTask();
    }

    @Override
    public boolean refresh()
    {
        backgroundTask.execute(true);
        return true;
    }
    @Override
    public boolean next()
    {
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
}
