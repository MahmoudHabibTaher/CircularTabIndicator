package com.mondo.example;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mahmoud on 8/16/15.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private static final int DUMMY_COUNT = 7;

    private final String TAG = getClass().getSimpleName();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return DUMMY_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return new MyFragment();
    }
}
