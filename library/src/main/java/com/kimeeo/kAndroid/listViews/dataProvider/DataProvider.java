package com.kimeeo.kAndroid.listViews.dataProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
abstract public class DataProvider extends MonitorList {
    protected int curruntRefreshPage=0;
    protected int curruntPage=0;
    protected boolean isFetching = false;
    protected boolean isFetchingRefresh = false;
    protected boolean canLoadNext = true;
    protected boolean canLoadRefresh = true;
    protected boolean refreshEnabled = false;
    protected boolean nextEnabled = false;
    protected int refreshItemPos = 0;
    protected boolean enterReverse = false;
    protected int nextItemPos = 0;
    protected boolean isFirstCall = true;
    protected boolean isConfigurableObject = false;
    private List<WeakReference<OnFatchingObserve>> onFatchingObserveList = new ArrayList<>();

    public int getCurruntRefreshPage() {
        return curruntRefreshPage;
    }

    public int getCurruntPage() {
        return curruntPage;
    }

    abstract protected void invokeLoadNext();

    abstract protected void invokeLoadRefresh();

    public boolean isFetching() {
        return isFetching;
    }

    public boolean getEnterReverse() {
        return enterReverse;
    }

    public void setEnterReverse(boolean enterReverse) {
        this.enterReverse = enterReverse;
    }

    public int getNextItemPos() {
        return nextItemPos;
    }

    public void setNextItemPos(int nextItemPos) {
        this.nextItemPos = nextItemPos;
    }

    public boolean isFirstCall() {
        return isFirstCall;
    }

    public void garbageCollectorCall() {
        curruntPage=0;
        curruntRefreshPage=0;
        if(onFatchingObserveList!=null) {
            for (int i = 0; i < onFatchingObserveList.size(); i++) {
                onFatchingObserveList.set(i, null);
            }
            onFatchingObserveList.removeAll(onFatchingObserveList);
            onFatchingObserveList =new ArrayList<>();
        }
    }

    public boolean removeFatchingObserve(OnFatchingObserve onFatchingObserve) {
        for (WeakReference<OnFatchingObserve> onFatchingObserveWeakReference : this.onFatchingObserveList) {
            if(onFatchingObserveWeakReference!=null && onFatchingObserveWeakReference.get()!=null && onFatchingObserveWeakReference.get()==onFatchingObserve)
                return this.onFatchingObserveList.remove(onFatchingObserveWeakReference);
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
        if(refreshItemPos<0)
            refreshItemPos=0;
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
        curruntPage=0;
        curruntRefreshPage=0;
        resetData();
        removeAll(this);
        isFirstCall=true;
        setCanLoadNext(true);
        next();
    }

    protected void resetData() {

    }

    protected boolean loadNext() {
        if(isFetching==false && getCanLoadNext() && isFirstCall) {
            isFetching=true;
            isFetchingRefresh=false;
            onFetchingStart(isFetchingRefresh);
            invokeLoadNext();
            return true;
        }
        else if(isFetching==false && getCanLoadNext() && getNextEnabled()) {
            isFetching=true;
            isFetchingRefresh=false;
            onFetchingStart(isFetchingRefresh);
            invokeLoadNext();
            return true;
        }
        else {
            onFetchingEnd(null,false);
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
            onFetchingStart(isFetchingRefresh);
            invokeLoadRefresh();
            return true;
        }
        else {
            onFetchingEnd(null,isFetchingRefresh);
            return false;
        }
    }

    public boolean refresh()
    {
        return loadRefresh();
    }

    protected void dataLoadError(Object status) {
        onFetchingError(status);
        onFetchingFinish(isFetchingRefresh);
        isFetchingRefresh=false;
        isFetching=false;
        isFirstCall=false;
    }

    protected void onFetchingEnd(List list, boolean isFetchingRefresh) {
        if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
        {
            for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                if(fatchingObserve !=null && fatchingObserve.get()!=null)
                    fatchingObserve.get().onFetchingEnd(list,isFetchingRefresh);
            }
        }
    }

    protected void onFetchingFinish(boolean isFetchingRefresh) {
        if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
        {
            for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                if(fatchingObserve !=null && fatchingObserve.get()!=null)
                    fatchingObserve.get().onFetchingFinish(isFetchingRefresh);
            }
        }
    }

    protected void onFetchingError(Object status) {
        if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
        {
            for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                if(fatchingObserve !=null && fatchingObserve.get()!=null)
                    fatchingObserve.get().onFetchingError(status);
            }
        }
    }

    protected void onFetchingStart(boolean isFetchingRefresh) {
        if(onFatchingObserveList!=null && onFatchingObserveList.size()!=0)
        {
            for (WeakReference<OnFatchingObserve> fatchingObserve : onFatchingObserveList) {
                if(fatchingObserve !=null && fatchingObserve.get()!=null)
                    fatchingObserve.get().onFetchingStart(isFetchingRefresh);
            }
        }
    }

    public void addData(List list) {
        onFetchingFinish(isFetchingRefresh);

        if(isFetchingRefresh)
            curruntRefreshPage +=1;
        else
            curruntPage +=1;

        if(getConfigurableObject())
        {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof IConfigurableObject)
                    ((IConfigurableObject) list.get(i)).config();
            }
        }
        if(list!=null) {
            if (getEnterReverse()) {
                if (isFetchingRefresh) {
                    if (getNextItemPos() <= 0)
                        addAll(list);
                    else {
                        if (size() != 0) {
                            int pos = size() - getNextItemPos();
                            if (pos > 0)
                                addAll(pos, list);
                            else
                                addAll(list);
                        } else
                            addAll(list);
                    }
                } else {
                    addAll(getRefreshItemPos(), list);
                }
            }
            else
            {
                if (isFetchingRefresh)
                    addAll(getRefreshItemPos(), list);
                else
                {
                    if (getNextItemPos() <= 0)
                        addAll(list);
                    else {
                        if (size() != 0) {
                            int pos = size() - getNextItemPos();
                            if (pos > 0)
                                addAll(pos, list);
                            else
                                addAll(list);
                        } else
                            addAll(list);
                    }
                }
            }
        }
        onFetchingEnd(list,isFetchingRefresh);
        isFetchingRefresh=false;
        isFetching=false;
        isFirstCall=false;
    }

    public interface OnFatchingObserve {
        void onFetchingStart(boolean isFetchingRefresh);
        void onFetchingFinish(boolean isFetchingRefresh);
        //void onFetchingEnd(boolean isFetchingRefresh);
        void onFetchingEnd(List<?> dataList, boolean isFetchingRefresh);
        void onFetchingError(Object error);
    }
}
