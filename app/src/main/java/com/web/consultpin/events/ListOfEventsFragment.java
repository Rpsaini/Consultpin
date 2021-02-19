package com.web.consultpin.events;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.web.consultpin.R;
import com.web.consultpin.adapter.EventHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListOfEventsFragment extends Fragment {
private View view;
private EventRequestActivity eventRequestActivity;

    public ListOfEventsFragment() {
        // Required empty public constructor
    }

    public static ListOfEventsFragment newInstance(String param1, String param2) {
        ListOfEventsFragment fragment = new ListOfEventsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_list_of_events, container, false);
        eventRequestActivity= (EventRequestActivity) getActivity();
        ArrayList<JSONObject> list=new ArrayList<>();
        for(int x=0;x<10;x++)
        {
            list.add(new JSONObject());
        }
        init(list);
        return  view;
    }

    private void init(ArrayList<JSONObject> dataAr)
    {

        RecyclerView event_history_recycler =view.findViewById(R.id.event_history_recycler);
        RelativeLayout relativeLayout =view.findViewById(R.id.rr_nodata_view);
        if(dataAr.size()==0)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            event_history_recycler.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
            event_history_recycler.setVisibility(View.VISIBLE);
        }
        EventHistoryAdapter mAdapter = new EventHistoryAdapter(dataAr,eventRequestActivity);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        event_history_recycler.setLayoutManager(horizontalLayoutManagaer);
        event_history_recycler.setItemAnimator(new DefaultItemAnimator());
        event_history_recycler.setAdapter(mAdapter);
    }

}