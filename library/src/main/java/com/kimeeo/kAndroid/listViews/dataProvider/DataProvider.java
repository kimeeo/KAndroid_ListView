package com.kimeeo.kAndroid.listViews.dataProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
abstract public class DataProvider<T> extends MonitorList<T> {
    public boolean isFetching() {
        return isFetching;
    }

    private boolean isFetching=false;
    private boolean isFetchingRefresh=false;
    private boolean canLoadNext=true;
    private boolean canLoadRefresh=true;
    private boolean loadRefreshEnabled=false;
    private boolean loadNextEnabled=false;

    private int refreshItemPos=0;
    private boolean isFirstCall=true;
    private  boolean isConfigurableObject=false;
    private List<WeakReference<OnFatchingObserve>> onFatchingObserve=new ArrayList<>();

    public boolean removeFatchingObserve(OnFatchingObserve onFatchingObserve) {
        return this.onFatchingObserve.remove(onFatchingObserve);
    }
    public boolean addFatchingObserve(OnFatchingObserve onFatchingObserve) {
        return this.onFatchingObserve.add(new WeakReference(onFatchingObserve));
    }

    public int getRefreshItemPos() {
        return refreshItemPos;
    }
    public void setRefreshItemPos(int refreshItemPos) {
        this.refreshItemPos = refreshItemPos;
    }
    public boolean getConfigurableObject() {
        return isConfigurableObject;
    }
    public void setConfigurableObject(boolean configurableObject) {
        isConfigurableObject = configurableObject;
    }
    public boolean getLoadNextEnabled() {
        return loadNextEnabled;
    }
    public void setLoadNextEnabled(boolean loadNextEnabled) {
        this.loadNextEnabled = loadNextEnabled;
    }
    public boolean getLoadRefreshEnabled() {
        return loadRefreshEnabled;
    }
    public void setLoadRefreshEnabled(boolean loadRefreshEnabled) {
        this.loadRefreshEnabled = loadRefreshEnabled;
    }
    public boolean getCanLoadNext() {
        return canLoadNext;
    }
    public void setCanLoadNext(boolean canLoadNext) {
        this.canLoadNext = canLoadNext;
    }
    public boolean getCanLoadRefresh() {
        return canLoadRefresh;
    }
    public void setCanLoadRefresh(boolean canLoadRefresh) {
        this.canLoadRefresh = canLoadRefresh;
    }
    public void reset()
    {
        removeAll(this);
        isFirstCall=true;
        setCanLoadNext(true);
        loadNext();
    }
    public boolean loadNext()
    {
        if(isFetching==false && getCanLoadNext() && isFirstCall) {
            isFetching=true;
            isFetchingRefresh=false;
            if(onFatchingObserve!=null && onFatchingObserve.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserve) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingStart(false);
                }
            }
            invokeLoadNext();
            return true;
        }
        else if(isFetching==false && getCanLoadNext() && getLoadNextEnabled()) {
            isFetching=true;
            isFetchingRefresh=false;
            if(onFatchingObserve!=null && onFatchingObserve.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserve) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingStart(false);
                }
            }
            invokeLoadNext();
            return true;
        }
        else {
            if(onFatchingObserve!=null && onFatchingObserve.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserve) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingEnd(null,false);
                }
            }

            return false;
        }
    }
    protected void dataLoadError(Object status)
    {
        if(onFatchingObserve!=null && onFatchingObserve.size()!=0)
        {
            for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserve) {
                if(fatchingObserve !=null && fatchingObserve.get()!=null)
                    fatchingObserve.get().onFetchingError(status);
            }
        }
    }
    public boolean loadRefresh()
    {
        if(isFetching==false && getCanLoadRefresh() && getLoadRefreshEnabled()) {
            isFetching=true;
            isFetchingRefresh=true;
            if(onFatchingObserve!=null && onFatchingObserve.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserve) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingStart(isFetchingRefresh);
                }
            }

            invokeloadRefresh();
            return true;
        }
        else {
            if(onFatchingObserve!=null && onFatchingObserve.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserve) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingEnd(null,isFetchingRefresh);
                }
            }

            return false;
        }
    }

    abstract protected void invokeLoadNext();
    abstract protected void invokeloadRefresh();

    public void addData(List<T> list)
    {
        if(getConfigurableObject())
        {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof IConfigurableObject)
                    ((IConfigurableObject) list.get(i)).config();
            }
        }

        if(list!=null) {
            if (isFetchingRefresh)
                addAll(getRefreshItemPos(), list);
            else
                addAll(list);
        }

        if(onFatchingObserve!=null && onFatchingObserve.size()!=0)
        {
            for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserve) {
                if(fatchingObserve !=null && fatchingObserve.get()!=null)
                    fatchingObserve.get().onFetchingEnd(list,isFetchingRefresh);
            }
        }

        isFetchingRefresh=false;
        isFetching=false;
        isFirstCall=false;
    }
    public interface OnFatchingObserve
    {
        void onFetchingStart(boolean isFetchingRefresh);
        void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh);
        void onFetchingError(Object error);
    }

}
