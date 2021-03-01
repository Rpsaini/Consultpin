package com.web.consultpin.consultant;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.AlreadyAddedTimeAdapter;
import com.web.consultpin.adapter.CustomTimingGrid;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AddTimeByConsultantFrg extends Fragment {
    private TextView ed_datefrom, ed_date_end;
//    public ArrayList<String> customTimeArray = new ArrayList<>();
    ImageView img_isweekdayopen;
    public String include_weekend_n_holidays = "1", timing_type = "1";//quicktime 1   custom 2
//    public LinkedHashMap<Integer, String> preTimeMapCustom = new LinkedHashMap<>();
//    public LinkedHashMap<Integer, String> preTimeMapAlready = new LinkedHashMap<>();
    public TextView tv_saveAppointment;

    AlreadyAddedTimeAdapter horizontalCategoriesAdapter;
    CustomTimingGrid customTimingGrid;
    Map<String, JSONObject> datesMap = new HashMap<>();
//    List<String> datesStringAr = new ArrayList<String>();


    public Map<String, ArrayList<JSONObject>> mainDataContainerMap = new HashMap<>();
    private View view;
    private TimeManagement timeManagement;

    public static AddTimeByConsultantFrg newInstance(String param1, String param2) {
        AddTimeByConsultantFrg fragment = new AddTimeByConsultantFrg();
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
        view = inflater.inflate(R.layout.activity_set_appoint_ment, container, false);
        timeManagement = (TimeManagement) getActivity();

        init();
        getTodayTiming();
        getAlreadyAddedTime();
        getTimeInterval();
        return view;
    }
    private void getTimeInterval() {
        String[] quarterHours = {"00", "15", "30", "45"};
        ArrayList<String> times = new ArrayList<String>(); // <-- List instead of array

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 4; j++) {
                String time = i + ":" + quarterHours[j];
                if (i < 10) {
                    time = "0" + time;
                }
                times.add(time); // <-- no need to care about indexes
                //System.out.println("Time interval====>"+time);
            }
            viewTimingGrid(times);
        }
    }

    private void init() {



        ed_datefrom = view.findViewById(R.id.ed_datefrom);
        ed_date_end = view.findViewById(R.id.ed_date_end);
        img_isweekdayopen = view.findViewById(R.id.img_isweekdayopen);
        tv_saveAppointment =view.findViewById(R.id.tv_saveAppointment);

        listeners();


    }



    private void getTodayTiming() {

        try {

            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = formatter.format(todayDate);
            ed_datefrom.setText(todayString);
            ed_date_end.setText(todayString);

        } catch (Exception e) {
            System.out.println("Message====" + e.getMessage());
        }

    }


    private void listeners() {

        ed_datefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate((TextView) v);
            }
        });

        ed_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate((TextView) v);
            }
        });

        img_isweekdayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (include_weekend_n_holidays.equalsIgnoreCase("1")) {
                    include_weekend_n_holidays = "0";
                    img_isweekdayopen.setImageResource(R.drawable.ic_unselect_button);
                } else {
                    include_weekend_n_holidays = "1";
                    img_isweekdayopen.setImageResource(R.drawable.ic_button);

                }

                tv_saveAppointment.setAlpha(1f);
                tv_saveAppointment.setEnabled(true);


            }
        });

        tv_saveAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTiming();
            }
        });


    }

    public void viewTimingGrid(ArrayList<String> dataAr) {

        GridView grid_timing = view.findViewById(R.id.grid_timing);
        customTimingGrid = new CustomTimingGrid(timeManagement, dataAr,this);
        grid_timing.setAdapter(customTimingGrid);
    }

    private void startDate(final TextView textView) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd = new DatePickerDialog(timeManagement,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        textView.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                        //getDatesBetweenTWoDates(ed_datefrom.getText().toString(), ed_date_end.getText().toString());


                    }
                }, mYear, mMonth, mDay);
        dpd.show();


    }


    private void showAlreadyAddedTime(JSONArray commonTime, String dateStr) {
        RecyclerView recyclerview_alreadyAddedTime = view.findViewById(R.id.recyclerview_alreadyAddedTime);
        recyclerview_alreadyAddedTime.setNestedScrollingEnabled(false);
        recyclerview_alreadyAddedTime.setLayoutManager(new LinearLayoutManager(timeManagement,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerview_alreadyAddedTime.setHasFixedSize(true);
        recyclerview_alreadyAddedTime.setItemAnimator(new DefaultItemAnimator());
        horizontalCategoriesAdapter = new AlreadyAddedTimeAdapter(commonTime, timeManagement, dateStr,this);
        recyclerview_alreadyAddedTime.setAdapter(horizontalCategoriesAdapter);
    }


    private void getAlreadyAddedTime() {
        try {
            mainDataContainerMap.clear();
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", timeManagement.getRestParamsName(Utilclass.consultant_id));
            m.put("start_date", ed_datefrom.getText().toString());
            m.put("end_date", ed_date_end.getText().toString());

            m.put("device_type", "android");
            m.put("device_token", timeManagement.getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", timeManagement.getRestParamsName(Utilclass.token));

            System.out.println("before=====" + m);
            timeManagement.serverHandler.sendToServer(timeManagement, timeManagement.getApiUrl() + "get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data===" + dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONArray quickAr = data.getJSONArray("quick_time_list");
                                showAlreadyAddedTime(quickAr, ed_datefrom.getText().toString());

                                JSONArray appointment_timeAr = data.getJSONArray("appointment_time");
//                                JSONObject gdataOb = new JSONObject();
                                for (int x = 0; x < appointment_timeAr.length(); x++) {
                                    JSONObject dataObj = appointment_timeAr.getJSONObject(x);
//                                    gdataOb = dataObj;
                                    datesMap.put(dataObj.getString("start_date"), dataObj);

                                    String timingStr = dataObj.getString("timing");
                                    String timing_type = dataObj.getString("timing_type");
                                    String include_weekend_n_holidays = dataObj.getString("include_weekend_n_holidays");
                                    String start_date = dataObj.getString("start_date");

                                    JSONArray timeAR = new JSONArray(timingStr);
                                    ArrayList<JSONObject> quickCustomJAr = new ArrayList<>();
                                    for (int y = 0; y < timeAR.length(); y++) {
                                        JSONObject jsonData = new JSONObject();
                                        jsonData.put("isQuick", timing_type);
                                        jsonData.put("time", timeAR.getString(y));
                                        jsonData.put("timing", timingStr);
                                        jsonData.put("include_weekend_n_holidays", include_weekend_n_holidays);
                                        quickCustomJAr.add(jsonData);
                                    }
                                    mainDataContainerMap.put(start_date, quickCustomJAr);

                                }
                                System.out.println("Map data====" + mainDataContainerMap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            timeManagement.alertDialogs.alertDialog(timeManagement, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


    private void saveTiming() {

        try {

            if (timeManagement.preTimeMapCustom.size() > 0 && timeManagement.preTimeMapCustom.size() % 2 != 0)
            {
                timeManagement.alertDialogs.alertDialog(timeManagement, getResources().getString(R.string.Required), getResources().getString(R.string.selectvalidTimepair), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                    }
                });
                return;
            }

            final Map<String, String> m = new HashMap<>();
            m.put("start_date", ed_datefrom.getText().toString());
            m.put("end_date", ed_date_end.getText().toString());
            m.put("consultant_id", timeManagement.getRestParamsName(Utilclass.consultant_id));
            m.put("include_weekend_n_holidays", include_weekend_n_holidays);

            Set<Integer> keyset =timeManagement.preTimeMapCustom.keySet();
            Iterator<Integer> intItr=keyset.iterator();
            List<Integer> intList = new ArrayList<Integer>();
            while (intItr.hasNext())
            {
                intList.add(intItr.next());
            }

            Collections.sort(intList, new Comparator<Integer>() {

                public int compare(Integer o1, Integer o2) {
                    // for Accending order
                    return o1 - o2;
                }
            });


            JSONArray SavedArray = new JSONArray();
            int paircount = 0;
            String pairStr = "";
            for (int x=0;x<intList.size();x++)
            {
                pairStr = pairStr + "-" + timeManagement.preTimeMapCustom.get(intList.get(x));
                if (paircount == 1)
                {
                    pairStr=pairStr.replaceFirst("-","");
                    SavedArray.put(pairStr);
                    m.put("timing_type", "2");
                    paircount = -1;
                    pairStr="";
                }
                paircount++;
            }


            //sort key


            for (Map.Entry<Integer, String> map : timeManagement.preTimeMapAlready.entrySet()) {
                SavedArray.put(map.getValue());
                m.put("timing_type", "1");
            }
            m.put("timing", SavedArray+"");


            m.put("device_type", "android");
            m.put("device_token", timeManagement.getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", timeManagement.getRestParamsName(Utilclass.token));


            System.out.println("Before to save Consultant===" + m);


            if (SavedArray.length() == 0) {
                timeManagement.alertDialogs.alertDialog(timeManagement, getResources().getString(R.string.Required), getResources().getString(R.string.selecttime), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                    }
                });
                return;
            }

            timeManagement.serverHandler.sendToServer(timeManagement, timeManagement.getApiUrl() + "set-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                timeManagement.alertDialogs.alertDialog(timeManagement, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                    @Override
                                    public void getDialogEvent(String buttonPressed)
                                    {
                                        timeManagement.finish();
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            timeManagement.alertDialogs.alertDialog(timeManagement, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


    public void notifyMap(Map<Integer, String> map, String type)
    {
        if (type.equalsIgnoreCase("custom")) {
            timeManagement.preTimeMapCustom.clear();
            customTimingGrid.notifyDataSetChanged();
           } else if (type.equalsIgnoreCase("already")) {
            timeManagement.preTimeMapAlready.clear();
            horizontalCategoriesAdapter.notifyDataSetChanged();
        }
    }
}