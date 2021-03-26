package com.web.consultpin.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.AppointmentHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ConsultantHome extends Fragment {

    private MainActivity mainActivity;
    private View view;

    public ConsultantHome() {
        // Required empty public constructor
    }

    public static ConsultantHome newInstance(String param1, String param2) {
        ConsultantHome fragment = new ConsultantHome();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    TextView txt_upcomingappointment, txt_totalincom, txt_totalappointment, txt_cancelledappointment, txt_ongoingappointment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_consultant_home, container, false);
        mainActivity = (MainActivity) getActivity();

        txt_upcomingappointment = view.findViewById(R.id.txt_upcomingappointment);
        txt_totalincom = view.findViewById(R.id.txt_totalincom);
        txt_totalappointment = view.findViewById(R.id.txt_totalappointment);
        txt_cancelledappointment = view.findViewById(R.id.txt_cancelledappointment);
        txt_ongoingappointment = view.findViewById(R.id.txt_ongoingappointment);
        getAppointmentHistory();
        return view;
    }

    private void getAppointmentHistory() {
        try {

            String consultantId = mainActivity.getRestParamsName(Utilclass.consultant_id);
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", consultantId);
            m.put("device_token", mainActivity.getDeviceToken() + "");
            final Map<String, String> obj = new HashMap<>();
            obj.put("token", mainActivity.getRestParamsName(Utilclass.token) + "");


            mainActivity.serverHandler.sendToServer(mainActivity, mainActivity.getApiUrl() + "consultant-dashboard", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data===" + dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            try {
                                JSONArray dataAr = jsonObject.getJSONArray("upcoming_appointments_detail");
                                txt_upcomingappointment.setText(jsonObject.getString("upcoming_appointments"));
                                txt_totalincom.setText("0" + getResources().getString(R.string.lirasymbol));
                                txt_totalappointment.setText(jsonObject.getString("total_appointments"));
                                txt_cancelledappointment.setText(jsonObject.getString("cancelled_appointments"));
                                txt_ongoingappointment.setText(jsonObject.getString("ongoing_appointments"));

                                ArrayList<JSONObject> historyBookingAr = new ArrayList<>();
                                for (int x = 0; x < dataAr.length(); x++) {
                                    historyBookingAr.add(dataAr.getJSONObject(x));
                                }
                                showAppointmentData(historyBookingAr);
                            } catch (Exception e) {
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

    private void showAppointmentData(ArrayList<JSONObject> dataAr) {
        RecyclerView recyclerview_appointmenthistory = view.findViewById(R.id.upcomming_app_recycler);

        RelativeLayout relativeLayout = view.findViewById(R.id.rr_nodata_view);
        if (dataAr.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerview_appointmenthistory.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            recyclerview_appointmenthistory.setVisibility(View.VISIBLE);
        }

        recyclerview_appointmenthistory.setNestedScrollingEnabled(false);
        recyclerview_appointmenthistory.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        recyclerview_appointmenthistory.setHasFixedSize(true);
        recyclerview_appointmenthistory.setItemAnimator(new DefaultItemAnimator());
        AppointmentHistoryAdapter horizontalCategoriesAdapter = new AppointmentHistoryAdapter(dataAr, mainActivity, Utilclass.upcoming);
        recyclerview_appointmenthistory.setAdapter(horizontalCategoriesAdapter);
    }
}