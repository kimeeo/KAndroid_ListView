package com.kimeeo.kAndroid.listViews.dataProvider;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
public class MonitorList<T> extends ArrayList<T> {

    private List<WeakReference<OnChangeWatcher<T>>> onChangeWatcherList = new ArrayList<>();
    public boolean removeDataChangeWatcher(OnChangeWatcher<T> onChangeWatcher) {
        for (WeakReference<OnChangeWatcher<T>> onChangeWatcherWeakReference : onChangeWatcherList) {
            if(onChangeWatcherWeakReference!=null && onChangeWatcherWeakReference.get()!=null && onChangeWatcherWeakReference.get()==onChangeWatcher) {
                return this.onChangeWatcherList.remove(new WeakReference<OnChangeWatcher<T>>(onChangeWatcher));
            }
        }
        return false;

    }
    public boolean addDataChangeWatcher(OnChangeWatcher<T> onChangeWatcher) {
        boolean found=false;
        for (WeakReference<OnChangeWatcher<T>> onChangeWatcherWeakReference : onChangeWatcherList) {
            if(onChangeWatcherWeakReference!=null && onChangeWatcherWeakReference.get()!=null && onChangeWatcherWeakReference.get()==onChangeWatcher)
            {
                found=true;
                break;
            }
        }
        if(!found)
            return this.onChangeWatcherList.add(new WeakReference<OnChangeWatcher<T>>(onChangeWatcher));
        return  false;
    }

    @Override
    public boolean remove(Object item) {
        int position=-1;
        for (int i = 0; i < this.size(); i++) {
            if(this.get(i)==item)
            {
                position=i;
                break;
            }
        }
        boolean value=super.remove(item);
        if(value) {
            ArrayList<T> list = new ArrayList<>();
            list.add((T)item);
            notifyRemove(position,list);
        }
        return value;
    }
    @Override
    public T set(int index,T element)
    {
        T value = super.set(index,element);
        ArrayList<T> list = new ArrayList<>();
        list.add(value);
        notifyChanged(index,list);
        return value;
    }
    @Override
    public T remove(int position) {
        T value=super.remove(position);
        ArrayList<T> list = new ArrayList<>();
        list.add(value);
        notifyRemove(position, list);
        return value;
    }
    @Override
    public boolean add(T value)
    {
        boolean retrunVal= super.add(value);
        if(retrunVal) {
            ArrayList<T> list = new ArrayList<>();
            list.add(value);
            notifyAdd(size(),list);
        }
        return retrunVal;
    }
    @Override
    public void add(int position,T value) {
        ArrayList<T> list = new ArrayList<>();
        list.add(value);
        notifyAdd(position,list);
        super.add(position, value);
    }
    @Override
    public boolean addAll(Collection value) {
        boolean returnVal = super.addAll(value);
        if(returnVal) {
            ArrayList<T> list = new ArrayList<>();
            list.addAll(value);

            notifyAdd(size(), list);
        }
        return returnVal;
    }
    @Override
    public boolean addAll(int index, Collection value) {
        boolean returnVal = super.addAll(index,value);
        if(returnVal) {
            ArrayList<T> list = new ArrayList<>();
            list.addAll(value);
            notifyAdd(index, list);
        }
        return returnVal;
    }
    @Override
    public boolean removeAll(Collection value) {
        ArrayList<T> list = new ArrayList<>();
        list.addAll(value);
        notifyRemove(0,list);
        return super.removeAll(value);
    }
    @Override
    public void clear()
    {
        ArrayList<T> list = new ArrayList<>();
        list.addAll(this);
        notifyRemove(0,list);
        super.clear();
    }

    @Override
    protected void removeRange(int fromIndex,int toIndex)
    {
        ArrayList<T> list= new ArrayList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            list.add(get(i));
        }
        notifyRemove(fromIndex,list);
        super.removeRange(fromIndex,toIndex);
    }




    //NOTIFICATIONS
    protected void notifyAdd(int position,List<T> objects) {
        if(onChangeWatcherList!=null && objects!=null && objects.size()!=0)
        {
            for (WeakReference<OnChangeWatcher<T>> item : onChangeWatcherList) {
                if(item!=null && item.get()!=null)
                    item.get().itemsAdded(position,objects);
            }
        }
    }
    protected void notifyRemove(int position,List<T> objects) {
        if(onChangeWatcherList!=null && objects!=null && objects.size()!=0)
        {
            for (WeakReference<OnChangeWatcher<T>> item : onChangeWatcherList) {
                if(item!=null && item.get()!=null)
                    item.get().itemsRemoved(position,objects);
            }
        }
    }
    protected void notifyChanged(int position,List<T> objects) {
        if(onChangeWatcherList!=null && objects!=null && objects.size()!=0)
        {
            for (WeakReference<OnChangeWatcher<T>> item : onChangeWatcherList) {
                if(item!=null && item.get()!=null)
                    item.get().itemsChanged(position,objects);
            }
        }
    }
    public interface OnChangeWatcher<T>
    {
        void itemsAdded(int index,List<T> items);
        void itemsRemoved(int index,List<T> items);
        void itemsChanged(int index,List<T> items);
    }
    //NOTIFICATIONS
}
