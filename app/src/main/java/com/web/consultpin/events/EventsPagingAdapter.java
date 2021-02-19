package com.web.consultpin.events;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentStatePagerAdapter;


public class EventsPagingAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public EventsPagingAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AddEventsFragment tab1 = new AddEventsFragment();
                return tab1;
            case 1:
                ListOfEventsFragment tab2 = new ListOfEventsFragment();
                return tab2;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}