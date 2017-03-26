package com.kimeeo.kAndroid.listViews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by BhavinPadhiyar on 3/26/17.
 */

public class ProgressItem
{
    public Class getView()
    {
        return ProgressbarView.class;
    }

    public static class ProgressbarView extends Fragment {
        protected void garbageCollectorCall() {

        }
        protected void configViewParam(){

        }
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout._fragment_view_pager_progress_item, container, false);
            return rootView;
        }
    }
}
