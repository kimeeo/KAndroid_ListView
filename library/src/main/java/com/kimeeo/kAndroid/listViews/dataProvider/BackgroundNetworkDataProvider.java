package com.kimeeo.kAndroid.listViews.dataProvider;

import java.util.Map;


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
    abstract protected Map<String,Object> getNextParam();
    abstract protected Class<DataModel> getDataModel();
    protected String getRefreshURL() {
        return null;
    }
    protected Map<String,Object> getRefreshParam(){
        return null;
    }
}
