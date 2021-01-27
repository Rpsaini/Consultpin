package com.web.consultpin.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;

import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.adapter.CustomTimingGrid;

import java.util.ArrayList;

public class Appointment extends Fragment {

private View view;

    public Appointment() {
        // Required empty public constructor
    }


    public static Appointment newInstance(String param1, String param2) {
        Appointment fragment = new Appointment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_appointment, container, false);
        viewTimingGrid();
        return view;
    }


    private void viewTimingGrid()
    {
        GridView grid_timing=view.findViewById(R.id.grid_timing);

        final ArrayList<Integer> keysAr=new ArrayList<>();
        for(int x=1;x<=29;x++)
        {
            keysAr.add(x);
        }
//        keysAr.add(0);


        grid_timing.setAdapter(new CustomTimingGrid((MainActivity)getActivity(), keysAr));
    }
}