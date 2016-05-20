package com.kimeeo.kAndroid.listViewsDemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.kimeeo.kAndroid.core.fragment.BaseFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Class clazz =HeaderListView.class;
        //Class clazz = ListView.class;
        Fragment view = BaseFragment.newInstance(clazz);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, view).commit();
    }

}
