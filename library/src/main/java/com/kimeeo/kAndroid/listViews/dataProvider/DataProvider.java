package com.kimeeo.kAndroid.listViews.dataProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
abstract public class DataProvider extends MonitorList {
    abstract protected void invokeLoadNext();
    abstract protected void invokeloadRefresh();
    public boolean isFetching() {
        return isFetching;
    }
    private boolean isFetching=false;
    private boolean isFetchingRefresh=false;
    private boolean canLoadNext=true;
    private boolean canLoadRefresh=true;
    private boolean refreshEnabled=false;
    private boolean nextEnabled=false;
    private int refreshItemPos=0;
    private boolean isFirstCall=true;
    private  boolean isConfigurableObject=false;
    private List<WeakReference<OnFatchingObserve>> onFatchingObserveList=new ArrayList<>();
    public boolean removeFatchingObserve(OnFatchingObserve onFatchingObserve) {
        for (WeakReference<OnFatchingObserve> onFatchingObserveWeakReference : this.onFatchingObserveList) {
            if(onFatchingObserveWeakReference!=null && onFatchingObserveWeakReference.get()!=null && onFatchingObserveWeakReference.get()==onFatchingObserve)
                return this.onFatchingObserveList.remove(onFatchingObserve);
        }
        return false;
    }
    public boolean addFatchingObserve(OnFatchingObserve onFatchingObserve) {
        boolean found=false;
        for (WeakReference<OnFatchingObserve> onFatchingObserveWeakReference : this.onFatchingObserveList) {
            if(onFatchingObserveWeakReference!=null && onFatchingObserveWeakReference.get()!=null && onFatchingObserveWeakReference.get()==onFatchingObserve) {
                found = true;
                break;
            }
        }
        if(!found)
            return this.onFatchingObserveList.add(new WeakReference(onFatchingObserve));
        return false;
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
    public boolean getNextEnabled() {
        return nextEnabled;
    }
    public void setNextEnabled(boolean nextEnabled) {
        this.nextEnabled = nextEnabled;
    }
    public boolean getRefreshEnabled() {
        return refreshEnabled;
    }
    public void setRefreshEnabled(boolean refreshEnabled) {
        this.refreshEnabled = refreshEnabled;
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
    public void reset() {
        removeAll(this);
        isFirstCall=true;
        setCanLoadNext(true);
        next();
    }
    protected boolean loadNext() {
        if(isFetching==false && getCanLoadNext() && isFirstCall) {
            isFetching=true;
            isFetchingRefresh=false;
            if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingStart(false);
                }
            }
            invokeLoadNext();
            return true;
        }
        else if(isFetching==false && getCanLoadNext() && getNextEnabled()) {
            isFetching=true;
            isFetchingRefresh=false;
            if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingStart(false);
                }
            }
            invokeLoadNext();
            return true;
        }
        else {
            if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingEnd(null,false);
                }
            }

            return false;
        }
    }
    public boolean next()
    {
        return loadNext();
    }
    protected boolean loadRefresh() {
        if(isFetching==false && getCanLoadRefresh() && getRefreshEnabled()) {
            isFetching=true;
            isFetchingRefresh=true;
            if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingStart(isFetchingRefresh);
                }
            }

            invokeloadRefresh();
            return true;
        }
        else {
            if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
            {
                for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                    if(fatchingObserve !=null && fatchingObserve.get()!=null)
                        fatchingObserve.get().onFetchingEnd(null,isFetchingRefresh);
                }
            }

            return false;
        }
    }
    public boolean refresh()
    {
        return loadRefresh();
    }
    protected void dataLoadError(Object status) {
        if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
        {
            for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                if(fatchingObserve !=null && fatchingObserve.get()!=null)
                    fatchingObserve.get().onFetchingError(status);
            }
        }
    }
    public void addData(List list) {
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
        if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
        {
            for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                if(fatchingObserve !=null && fatchingObserve.get()!=null)
                    fatchingObserve.get().onFetchingEnd(list,isFetchingRefresh);
            }
        }
        isFetchingRefresh=false;
        isFetching=false;
        isFirstCall=false;
    }
    public void garbageCollectorCall() {

    }
    public interface OnFatchingObserve {
        void onFetchingStart(boolean isFetchingRefresh);
        void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh);
        void onFetchingError(Object error);
    }
}
