package com.kimeeo.kAndroid.listViews.dataProvider;

import android.os.AsyncTask;

import java.util.List;
import java.util.Map;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
abstract public class BackgroundDataProvider<T> extends DataProvider<T>
{
    BackgroundTask backgroundTask;
    public BackgroundDataProvider(IListProvider<T> listProvider)
    {
        backgroundTask=new BackgroundTask(listProvider);
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
    private class BackgroundTask extends AsyncTask<Boolean, Void, List<T>> {

        IListProvider listProvider;
        public BackgroundTask(IListProvider listProvider)
        {
            this.listProvider = listProvider;
        }
        protected void onPreExecute() {

        }
        @Override
        protected List<T> doInBackground(Boolean... params) {
            List<T> data = listProvider.getList(params[0]);
            return data;
        }
        @Override
        protected void onPostExecute(List<T> data) {
            addData(data);
        }
    }
}
