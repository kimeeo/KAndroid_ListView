package com.kimeeo.kAndroid.listViews.fragmentStacks;

import com.kimeeo.kAndroid.listViews.BaseListDataView;
import com.kimeeo.kAndroid.listViews.listView.BaseListView;
import com.kimeeo.kAndroid.listViews.pager.BaseViewPager;
import com.kimeeo.kAndroid.listViews.recyclerView.BaseRecyclerView;

/**
 * Created by BhavinPadhiyar on 3/26/17.
 */

abstract public class FragmentViewStack extends BaseFragmentStacks
{
    final protected int getAddMethod() {
        return ADDING_METHOD_ADD_STACK;
    }
    private int selectedIndex=0;

    public int getSelectedIndex() {
        return selectedIndex;
    }
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        if(getFragments()!=null && isViewCreated())
            showOnlyFragments(getFragments()[selectedIndex]);
    }

    public BaseListDataView getSelectedItem() {
        return getFragments()[selectedIndex];
    }
    public void setSelectedItem(BaseListDataView selectedItem) {
        if(getFragments()!=null)
        {
            for (int i = 0; i < getFragments().length; i++) {
                if(getFragments()[i]==selectedItem) {
                    setSelectedIndex(i);
                    return;
                }
            }
        }
    }
    @Override
    protected  void configFragements(BaseListDataView[] fragments){
        super.configFragements(fragments);

        for (int i = 0; i < fragments.length; i++) {
            BaseListDataView fragment = getFragments()[i];
            if (fragment instanceof BaseRecyclerView)
                ((BaseRecyclerView) fragment).setSupportLoader(false);
            else if (fragment instanceof BaseViewPager)
                ((BaseViewPager) fragment).setSupportLoader(false);
            else if (fragment instanceof BaseListView)
                ((BaseListView) fragment).setSupportLoader(false);
        }
    }


    @Override
    protected void onViewComplete() {
        super.onViewComplete();
        setSelectedIndex(selectedIndex);
    }
}
