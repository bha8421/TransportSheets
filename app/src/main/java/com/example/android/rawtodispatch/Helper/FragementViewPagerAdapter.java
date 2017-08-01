package com.example.android.rawtodispatch.Helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bharat on 7/19/2017.
 */

public class FragementViewPagerAdapter extends FragmentPagerAdapter {

    private final List<String> fragmentTitles=new ArrayList<>();
    private final List<Fragment> fragments=new ArrayList<>();

    public FragementViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(String tiles,Fragment fragment){
        fragmentTitles.add(tiles);
        fragments.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
