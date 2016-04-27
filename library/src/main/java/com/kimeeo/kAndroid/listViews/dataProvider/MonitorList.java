package com.kimeeo.kAndroid.listViews.dataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by BhavinPadhiyar on 27/04/16.
 */
public class MonitorList<T> extends ArrayList<T> {

    private OnChangeWatcher<T> onChangeWatcher;
    public OnChangeWatcher<T> getDataChangeWatcher() {
        return onChangeWatcher;
    }
    public void setDataChangeWatcher(OnChangeWatcher<T> onChangeWatcher) {
        this.onChangeWatcher = onChangeWatcher;
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
        notifyRemove(position,list);
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
    public void  add(int position,T value) {
        ArrayList<T> list = new ArrayList<>();
        list.add(value);
        notifyAdd(position,list);
        super.add(position, value);
    }
    @Override
    public boolean addAll(Collection value) {
        ArrayList<T> list = new ArrayList<>();
        list.addAll(value);

        notifyAdd(size(),list);
        return super.addAll(value);
    }
    @Override
    public boolean addAll(int index, Collection value) {
        ArrayList<T> list = new ArrayList<>();
        list.addAll(value);
        notifyAdd(index,list);
        return super.addAll(index,value);
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
        if(onChangeWatcher!=null && objects!=null && objects.size()!=0)
            onChangeWatcher.itemsAdded(position,objects);
    }
    protected void notifyRemove(int position,List<T> objects) {
        if(onChangeWatcher!=null && objects!=null && objects.size()!=0)
            onChangeWatcher.itemsRemoved(position,objects);
    }
    protected void notifyChanged(int position,List<T> objects) {
        if(onChangeWatcher!=null && objects!=null && objects.size()!=0)
            onChangeWatcher.itemsChanged(position,objects);
    }
    public interface OnChangeWatcher<T>
    {
        void itemsAdded(int index,List<T> items);
        void itemsRemoved(int index,List<T> items);
        void itemsChanged(int index,List<T> items);
    }
    //NOTIFICATIONS
}
