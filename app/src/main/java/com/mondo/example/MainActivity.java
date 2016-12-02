package com.mondo.example;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mondo.circulartabindicator.CircularTabIndicator;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private CircularTabIndicator mCircularTabIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mCircularTabIndicator = (CircularTabIndicator) findViewById(R.id.tab_indicator);
        mCircularTabIndicator.setUpWithViewPager(mViewPager);
    }
}
