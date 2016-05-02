package com.kimeeo.kAndroid.listViews.dataProvider;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
abstract public class BackgroundNetworkDataProvider extends BackgroundDataProvider
{
    public BackgroundNetworkDataProvider()
    {

    }
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
