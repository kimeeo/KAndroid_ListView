package com.kimeeo.kAndroid.listViewsDemo;

import com.kimeeo.kAndroid.listViews.dataProvider.DataModel;

import java.util.List;

/**
 * Created by bpa001 on 5/2/16.
 */
public class BaseDataModel implements DataModel {
    private String success;
    private List<DataBean> data;

    @Override
    public List getDataProvider() {
        return data;
    }

    @Override
    public void setDataProvider(List list) {
        data = list;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
}
