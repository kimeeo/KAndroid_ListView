package com.kimeeo.kAndroid.listViews.dataProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by bhavinpadhiyar on 1/30/16.
 */
public interface IListProvider<T> {
    List<T> getList(Boolean isFetchRefresh);
}
