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

        Fragment view = BaseFragment.newInstance(ListView.class);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, view).commit();
    }

}
