package com.mkg.orderfood.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Manikiran Goud on 21-02-2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();


    public void addFragments(Fragment fragment, String title) {
        this.fragments.add(fragment);
        this.titles.add(title);
    }

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }


    public void replaceFragment(int position, Fragment fragment, String title){

        fragments.remove(position);
        fragments.add(position, fragment);
        titles.add(position, title);
       // fragments.remove(position);
       // titles.remove(position);
    }


}
