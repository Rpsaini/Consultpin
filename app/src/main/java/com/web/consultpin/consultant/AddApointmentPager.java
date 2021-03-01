package com.web.consultpin.consultant;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.google.android.material.tabs.TabLayout;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.events.AddEventsFragment;
import com.web.consultpin.events.EventRequestActivity;
import com.web.consultpin.events.ListOfEventsFragment;
import com.web.consultpin.interfaces.ResponsInterface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class AddApointmentPager extends FragmentStatePagerAdapter {
    private EventRequestActivity eventRequestActivity;
    int mNumOfTabs;
    public AddApointmentPager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.eventRequestActivity=eventRequestActivity;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AddTimeByConsultantFrg tab1 = new AddTimeByConsultantFrg();

                return tab1;
            case 1:

                EditTimeByConsultantFrg tab2 = new EditTimeByConsultantFrg();

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