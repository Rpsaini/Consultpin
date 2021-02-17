package com.web.consultpin.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.AlreadyAddedTimeAdapter;
import com.web.consultpin.adapter.AppointmentHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AppointMentHistoryFrg extends Fragment {

   private MainActivity mainActivity;
   private View view;
    public AppointMentHistoryFrg() {
        // Required empty public constructor
    }

    public static AppointMentHistoryFrg newInstance(String param1, String param2) {
        AppointMentHistoryFrg fragment = new AppointMentHistoryFrg();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_appoint_ment_history_frg, container, false);
        mainActivity=(MainActivity) getActivity();
        getAppointmentHistory();
        return view;
    }


    private  void getAppointmentHistory()
    {


        try {
            String id="";
            String consultantId=mainActivity.getRestParamsName(Utilclass.consultant_id);
            String userid=mainActivity.getRestParamsName(Utilclass.user_id);
            String commonId="";
            final Map<String, String> m = new HashMap<>();
            String apiname="get-user-appointment-history";
            if(consultantId.equalsIgnoreCase("0"))
            {
                 commonId=userid;
                 apiname="get-user-appointment-history";
                 m.put("user_id", commonId);

            }
            else
            {
                 commonId=consultantId;
                 apiname="get-consultant-appointment-requests";
                 m.put("consultant_id", commonId);
            }

           m.put("device_type", "android");
            m.put("device_token", mainActivity.getDeviceToken()+"");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", mainActivity.getRestParamsName(Utilclass.token)+"");

            System.out.println("Consultant id==="+m);


            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + apiname, m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data==="+dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            try {
                             JSONArray dataAr=jsonObject.getJSONArray("data");
                             ArrayList<JSONObject> historyBookingAr=new ArrayList<>();
                             for(int x=0;x<dataAr.length();x++)
                             {
                                 historyBookingAr.add(dataAr.getJSONObject(x));
                             }
                                showAppointmentData(historyBookingAr);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        } else {
                            mainActivity.alertDialogs.alertDialog(mainActivity, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

    private void showAppointmentData(ArrayList<JSONObject> dataAr)
    {
        RecyclerView recyclerview_appointmenthistory =view.findViewById(R.id.recyclerview_appointmenthistory);
        recyclerview_appointmenthistory.setNestedScrollingEnabled(false);
        recyclerview_appointmenthistory.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        recyclerview_appointmenthistory.setHasFixedSize(true);
        recyclerview_appointmenthistory.setItemAnimator(new DefaultItemAnimator());
        AppointmentHistoryAdapter horizontalCategoriesAdapter = new AppointmentHistoryAdapter(dataAr,mainActivity,"");
        recyclerview_appointmenthistory.setAdapter(horizontalCategoriesAdapter);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Appointment request====>"+requestCode);
    }


}