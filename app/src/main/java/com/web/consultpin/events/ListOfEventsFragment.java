package com.web.consultpin.events;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.EventHistoryAdapter;
import com.web.consultpin.interfaces.ResponsInterface;
import com.web.consultpin.registration.LoginActivity;
import com.web.consultpin.usersection.UserEventHistory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListOfEventsFragment extends Fragment {
private View view;
//private EventRequestActivity eventRequestActivity;
private EventRequestActivity eventRequestActivity;



    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
            getEventList();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_list_of_events, container, false);
          eventRequestActivity= (EventRequestActivity) getActivity();



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

    private void getEventList()
    {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", eventRequestActivity.getRestParamsName(Utilclass.consultant_id));
            final Map<String, String> obj = new HashMap<>();
            obj.put("token",eventRequestActivity.getRestParamsName(Utilclass.token));




            System.out.println("before===="+m);

            eventRequestActivity.serverHandler.sendToServer(eventRequestActivity, eventRequestActivity.getApiUrl() + "event-list", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                JSONArray dataAr=jsonObject.getJSONArray("data");
                                ArrayList<JSONObject> dataObjAr=new ArrayList<>();
                                for(int x=0;x<dataAr.length();x++)
                                {
                                    dataObjAr.add(dataAr.getJSONObject(x));
                                }
                                init(dataObjAr);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            eventRequestActivity.alertDialogs.alertDialog(eventRequestActivity, eventRequestActivity.getResources().getString(R.string.Response), jsonObject.getString("msg"), eventRequestActivity.getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed) {
                                }
                            });

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}