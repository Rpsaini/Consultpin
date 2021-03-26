package com.web.consultpin.appointmenthistory;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.web.consultpin.events.AddEventsFragment;
import com.web.consultpin.events.EventRequestActivity;



public class AppointmentPagingAdapter extends FragmentStatePagerAdapter {
    private EventRequestActivity eventRequestActivity;
    int mNumOfTabs;
    public AppointmentPagingAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.eventRequestActivity=eventRequestActivity;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UpcomingAppointment tab1 = new UpcomingAppointment();

                return tab1;
            case 1:



                AppointmentHistory tab2 = new AppointmentHistory();

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