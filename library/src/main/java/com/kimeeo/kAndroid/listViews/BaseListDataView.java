package com.kimeeo.kAndroid.listViews;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.kimeeo.kAndroid.core.fragment.BaseFragment;
import com.kimeeo.kAndroid.listViews.dataProvider.DataProvider;
import com.kimeeo.kAndroid.listViews.dataProvider.MonitorList;

/**
 * Created by bhavinpadhiyar on 7/20/15.
 */
abstract public class BaseListDataView extends BaseFragment implements DataProvider.OnFatchingObserve,MonitorList.OnChangeWatcher, IEmptyViewHandler
{


    private DataProvider dataProvider;
    IEmptyViewHandler errorHandler = new DefaultErrorHandlerLocal();
    public void setErrorHandler(IEmptyViewHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
    protected void configViewParam(){

    }
    abstract protected @NonNull DataProvider createDataProvider();
    public void setDataProvider(DataProvider dataProvider) {
        if(this.dataProvider!=null)
        {
            this.dataProvider.removeFatchingObserve(this);
            this.dataProvider.removeDataChangeWatcher(this);
            this.dataProvider = null;
        }
        if(dataProvider!=null) {
            this.dataProvider = dataProvider;
            dataProvider.addFatchingObserve(this);
            dataProvider.addDataChangeWatcher(this);
            configDataManager(dataProvider);
        }
    }
    protected void garbageCollectorCall() {
        if(dataProvider!=null) {
            dataProvider.garbageCollectorCall();
            dataProvider = null;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getDataProvider()==null)
            setDataProvider(createDataProvider());
    }
    protected void configDataManager(DataProvider dataProvider) {}
    public DataProvider getDataProvider()
    {
        return dataProvider;
    }
    protected void next(){
        dataProvider.next();
    }
    protected void refresh()
    {
        dataProvider.refresh();
    }
    public Drawable getEmptyViewDrawable() {
        return errorHandler.getEmptyViewDrawable();
    }
    public String getEmptyViewMessage() {
        return errorHandler.getEmptyViewMessage();
    }
    public Drawable getInternetViewDrawable(){return errorHandler.getInternetViewDrawable();}
    public String getInternetViewMessage() {
        return errorHandler.getInternetViewMessage();
    }
    public void retry() {
        dataProvider.reset();
        //next();
    }
    public boolean showInternetError() {
        return true;
    }
    public boolean showInternetRetryButton() {
        return true;
    }
    public class DefaultErrorHandlerLocal extends DefaultErrorHandler{
        public Resources getResources() {
            return getActivity().getResources();
        }
        public void retry() {
            next();
        }
    }
}
