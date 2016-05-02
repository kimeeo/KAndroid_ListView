package com.kimeeo.kAndroid.listViews.dataProvider;

/**
 * Created by BhavinPadhiyar on 02/05/16.
 */
abstract public class NetworkDataProvider extends DataProvider
{
    public static final int METHOD_POST =1;
    public static final int METHOD_GET =2;
    abstract protected int getMethod();
    abstract protected String getNextURL();
    abstract protected Object getNextParam();
    abstract protected Class<DataModel> getDataModel();
    protected String getRefreshURL() {
        return null;
    }
    protected Object getRefreshParam(){
        return null;
    }
}
