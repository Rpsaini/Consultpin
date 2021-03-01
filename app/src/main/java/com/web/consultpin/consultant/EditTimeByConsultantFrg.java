package com.web.consultpin.consultant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;

import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.Set;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class EditTimeByConsultantFrg extends Fragment {
    private View view;
    private TimeManagement editTimeByConsultant;
    private String date = "";
    public String timeDuration = "", timeSlot = "";
    Map<String, Boolean> reservedTimeMap = new HashMap<>();
    Map<String, Boolean> alreadySettedMap = new HashMap<>();
    TextView tv_saveAppointment;
    MCalendarView date_cal_view;
    private ArrayList<String> times;
    private ImageView img_isweekdayopen;
    private String isWeekendoff = "0";

    ArrayList<JSONObject> allTimeIntervalAr = new ArrayList<>();
    Map<String,String> dateContinerMap=new HashMap<>();

    public EditTimeByConsultantFrg() {
        // Required empty public constructor
    }

    public static EditTimeByConsultantFrg newInstance(String param1, String param2) {
        EditTimeByConsultantFrg fragment = new EditTimeByConsultantFrg();
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

        view = inflater.inflate(R.layout.fragment_edit_time_by_consultant_frg, container, false);
        editTimeByConsultant = (TimeManagement) getActivity();

        init();
        getFiledsData();
        getTimeInterval();
        return view;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {

            getAllAppointmentDates();
        }
    }


    private void init() {

        try {

            date_cal_view = view.findViewById(R.id.date_cal_view);


            if (editTimeByConsultant.getIntent().hasExtra(Utilclass.appointment_date)) {
                String appointmentDate = editTimeByConsultant.getIntent().getStringExtra(Utilclass.appointment_date);
                String[] appointStr = appointmentDate.split(" ");
                date = appointStr[0];

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = (Date) formatter.parse(appointmentDate);
                long mills = date.getTime();

                //   date_cal_view.setDate(mills);
            } else {
                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date = formatter.format(todayDate);
                // date_cal_view.setDate(System.currentTimeMillis());
            }


            img_isweekdayopen = view.findViewById(R.id.img_isweekdayopen);
            tv_saveAppointment = view.findViewById(R.id.tv_saveAppointment);


            img_isweekdayopen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isWeekendoff.equalsIgnoreCase("0")) {
                        isWeekendoff = "1";
                        img_isweekdayopen.setImageResource(R.drawable.ic_button);
                    } else {
                        isWeekendoff = "0";
                        img_isweekdayopen.setImageResource(R.drawable.ic_unselect_button);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewTimingGrid(ArrayList<JSONObject> timingAr, Map<String, Boolean> reservedTimeMap) {
        GridView grid_timing = view.findViewById(R.id.grid_timing);
        RelativeLayout relativeLayout = view.findViewById(R.id.rr_nodata_view);


        if (timingAr.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
            grid_timing.setVisibility(View.GONE);
            tv_saveAppointment.setAlpha(.5f);
            tv_saveAppointment.setEnabled(false);
        } else {
            relativeLayout.setVisibility(View.GONE);
            grid_timing.setVisibility(View.VISIBLE);
            tv_saveAppointment.setAlpha(1f);
            tv_saveAppointment.setEnabled(true);
        }

        grid_timing.setAdapter(new EditTimeGridAdapter(editTimeByConsultant, timingAr, reservedTimeMap, alreadySettedMap));
    }


    private void getTimeInterval() {
        String[] quarterHours = {"00", "15", "30", "45"};
        times = new ArrayList<String>(); // <-- List instead of array
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 4; j++) {
                String time = i + ":" + quarterHours[j];
                if (i < 10) {
                    time = "0" + time;
                }
                times.add(time); // <-- no need to care about indexes
            }

        }
    }

    private void getAlreadyAddedTime() {
        try {
            reservedTimeMap.clear();
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", editTimeByConsultant.getRestParamsName(Utilclass.consultant_id));
            m.put("start_date", date);
            m.put("end_date", date);
            m.put("device_type", "android");
            m.put("device_token", editTimeByConsultant.getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", editTimeByConsultant.getRestParamsName(Utilclass.token));

            System.out.println("Appointment before===" + m);

            editTimeByConsultant.serverHandler.sendToServer(editTimeByConsultant, editTimeByConsultant.getApiUrl() + "get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data===" + dta);
                        allTimeIntervalAr.clear();
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                JSONObject dataObj = jsonObject.getJSONObject("data");


                                JSONArray appointmentArray = dataObj.getJSONArray("appointment_time");
                                if (appointmentArray.length() > 0) {
                                    JSONObject appointmentData = appointmentArray.getJSONObject(0);
                                    String isWeekendoff = appointmentData.getString("include_weekend_n_holidays");

                                    JSONArray timeArray = new JSONArray(appointmentData.getString("timing"));
                                    JSONArray jsonArrayReservedTime = dataObj.getJSONArray("reserved_times");

                                    for (int x = 0; x < timeArray.length(); x++) {
                                        String timeStr = timeArray.getString(x);
                                        String[] timeSplitAr = timeStr.split("-");


                                        String startTime = date + " " + timeSplitAr[0];
                                        String endTime = date + " " + timeSplitAr[1];
                                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        getTimeIntervals(timeSplitAr[0], sim.parse(startTime), sim.parse(endTime), isWeekendoff);

                                    }


                                    if (jsonArrayReservedTime.length() > 0) {
                                        for (int x = 0; x < allTimeIntervalAr.size(); x++) {
                                            JSONObject allTimeAdded = allTimeIntervalAr.get(x);
                                            String time = allTimeAdded.getString("timing");
                                            JSONObject reservedTime = jsonArrayReservedTime.getJSONObject(0);
                                            String[] splittedAr = reservedTime.getString("appointment_time").split(",");
                                            for (int z = 0; z < splittedAr.length; z++) {
                                                String reservedtime = splittedAr[z];
                                                System.out.println("Reserve timee==x=" + reservedtime);
                                                String appointment_duration = reservedTime.getString("appointment_duration");
                                                String reservetimeAfterAddingMinutes = addTime(reservedtime, Integer.parseInt(appointment_duration));
                                                boolean bool = checkTimeIsExistBeetWeenTwoTimes(reservedtime, reservetimeAfterAddingMinutes, time);
                                                if (bool) {
                                                    reservedTimeMap.put(time, bool);
                                                }
                                            }
                                        }
                                    }

                                    System.out.println("Reserve time data===" + reservedTimeMap);


                                    viewTimingGrid(allTimeIntervalAr, reservedTimeMap);

                                } else {
                                    viewTimingGrid(new ArrayList<>(), reservedTimeMap);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                viewTimingGrid(new ArrayList<>(), reservedTimeMap);
                            }

                        } else {
                            viewTimingGrid(new ArrayList<>(), reservedTimeMap);
                            editTimeByConsultant.alertDialogs.alertDialog(editTimeByConsultant, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

    private String addTime(String myTime, int interval) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date d = df.parse(myTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, interval);
            return df.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean checkTimeIsExistBeetWeenTwoTimes(String startTime, String endTime, String midtime) {
        try {
            startTime = startTime + ":00";
            endTime = endTime + ":00";
            midtime = midtime + ":00";

            System.out.println("Start end mid time====" + startTime + "==" + endTime + "===" + midtime);
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);


            Date d = new SimpleDateFormat("HH:mm:ss").parse(midtime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                return true;

            }
            if (x.equals(calendar1.getTime()) || x.equals(calendar2.getTime())) {
                return true;

            } else {

                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    EditText ed_description;

    private void getFiledsData() {

        date_cal_view.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date1) {

                int month = date1.getMonth();
                String newmonth = month + "";
                String newday = date1.getDay() + "";
                if (month <= 9) {
                    newmonth = "0" + month;
                }
                if (date1.getDay() <= 9) {
                    newday = "0" + date1.getDay();
                }
                date = date1.getYear() + "-" + newmonth + "-" + newday;
                if(dateContinerMap.containsKey(date)) {
                    getAlreadyAddedTime();
                }

            }
        });

//        date_cal_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
//                month = month + 1;
//                String newmonth = month + "";
//                String newday = day + "";
//                if (month <= 9) {
//                    newmonth = "0" + month;
//                }
//                if (day <= 9) {
//                    newday = "0" + day;
//                }
//                date = year + "-" + newmonth + "-" + newday;
//                getAlreadyAddedTime();
//            }
//        });


        tv_saveAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveTiming();

            }
        });

    }


    private void saveTiming() {

        try {

            if (editTimeByConsultant.preTimeMapCustom.size() > 0 && editTimeByConsultant.preTimeMapCustom.size() % 2 != 0) {
                editTimeByConsultant.alertDialogs.alertDialog(editTimeByConsultant, getResources().getString(R.string.Required), getResources().getString(R.string.selectvalidTimepair), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                    }
                });
                return;
            }

            final Map<String, String> m = new HashMap<>();
            m.put("start_date", date);
            m.put("end_date", date);
            m.put("consultant_id", editTimeByConsultant.getRestParamsName(Utilclass.consultant_id));
            m.put("include_weekend_n_holidays", isWeekendoff);


            Set<Integer> keyset = editTimeByConsultant.preTimeMapCustom.keySet();
            Iterator<Integer> intItr = keyset.iterator();
            List<Integer> intList = new ArrayList<Integer>();
            while (intItr.hasNext()) {
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
            for (int x = 0; x < intList.size(); x++) {
                pairStr = pairStr + "-" + editTimeByConsultant.preTimeMapCustom.get(intList.get(x));
                if (paircount == 1) {
                    pairStr = pairStr.replaceFirst("-", "");
                    SavedArray.put(pairStr);
                    m.put("timing_type", "2");
                    paircount = -1;
                    pairStr = "";
                }
                paircount++;
            }

            System.out.println("Pre time map=="+editTimeByConsultant.preTimeMapCustom);

            //sort key

            m.put("timing", SavedArray + "");

            m.put("device_type", "android");
            m.put("device_token", editTimeByConsultant.getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", editTimeByConsultant.getRestParamsName(Utilclass.token));

            if (SavedArray.length() == 0) {
                editTimeByConsultant.alertDialogs.alertDialog(editTimeByConsultant, getResources().getString(R.string.Required), getResources().getString(R.string.selecttime), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                    }
                });
                return;
            }

            editTimeByConsultant.serverHandler.sendToServer(editTimeByConsultant, editTimeByConsultant.getApiUrl() + "set-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                editTimeByConsultant.alertDialogs.alertDialog(editTimeByConsultant, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                    @Override
                                    public void getDialogEvent(String buttonPressed) {
                                        editTimeByConsultant.finish();
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            editTimeByConsultant.alertDialogs.alertDialog(editTimeByConsultant, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

    private void getTimeIntervals(String startAdd, Date startTime, Date EndTime, String isWeekendoff) {
        System.out.println("Start dataaa===?" + startTime + "==" + EndTime);

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            if (BaseActivity.compareTwoDates(date + " " + startAdd + ":00", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()))) {
                JSONObject jsonPreobj = new JSONObject();
                jsonPreobj.put("timing", startAdd);
                jsonPreobj.put("include_weekend_n_holidays", isWeekendoff);
                allTimeIntervalAr.add(jsonPreobj);
            }

            while (cal.getTime().before(EndTime)) {
                cal.add(Calendar.MINUTE, 15);
                JSONObject jsonObject1 = new JSONObject();
                String time = new java.sql.Time(cal.getTimeInMillis()) + "";
                time = time.substring(0, time.lastIndexOf(":"));
                jsonObject1.put("timing", time);
                jsonObject1.put("include_weekend_n_holidays", isWeekendoff);

                if (BaseActivity.compareTwoDates(date + " " + time + ":00", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()))) {
                    System.out.println("Timmm===>>>>" + time);
                    allTimeIntervalAr.add(jsonObject1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void getAllAppointmentDates() {

        dateContinerMap.clear();
        final Map<String, String> m = new HashMap<>();
        m.put("consultant_id", editTimeByConsultant.getRestParamsName(Utilclass.consultant_id));
        m.put("device_type", "android");
        m.put("device_token", editTimeByConsultant.getDeviceToken() + "");

        final Map<String, String> obj = new HashMap<>();
        obj.put("token", editTimeByConsultant.getRestParamsName(Utilclass.token));

        System.out.println("Appointment alldates===" + m);

        editTimeByConsultant.serverHandler.sendToServer(editTimeByConsultant, editTimeByConsultant.getApiUrl() + "get-all-appointment-dates", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("all data===" + dta);
                    allTimeIntervalAr.clear();
                    JSONObject jsonObject = new JSONObject(dta);
                    if (jsonObject.getBoolean("status")) {
                        try {

                            JSONArray dataAr=jsonObject.getJSONArray("data");

                            for(int x=0;x<dataAr.length();x++)
                            {
                                String datestr=dataAr.getJSONObject(x).getString("appointment_date");
                                dateContinerMap.put(datestr,datestr);
                                date_cal_view.markDate(Integer.parseInt(datestr.split("-")[0]), Integer.parseInt(datestr.split("-")[1]), Integer.parseInt(datestr.split("-")[2]));//mark multiple dates with this code.
                            }
                            getAlreadyAddedTime();

                        } catch (Exception e) {
                            e.printStackTrace();
                            viewTimingGrid(new ArrayList<>(), reservedTimeMap);
                        }

                    } else {
                        viewTimingGrid(new ArrayList<>(), reservedTimeMap);
                        editTimeByConsultant.alertDialogs.alertDialog(editTimeByConsultant, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


    }


}