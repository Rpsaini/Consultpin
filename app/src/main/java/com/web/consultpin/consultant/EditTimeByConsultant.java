package com.web.consultpin.consultant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.RequestForAppobyUserAdapter;
import com.web.consultpin.adapter.TimeAndMoneyAdapter;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.usersection.SetAppointmentByUser;

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
import java.util.Set;

public class EditTimeByConsultant extends BaseActivity {
    private String date = "";
    public String timeDuration = "", timeSlot = "";
    Map<String, Boolean> reservedTimeMap = new HashMap<>();
    Map<String, Boolean> alreadySettedMap = new HashMap<>();
    TextView tv_saveAppointment;
    CalendarView date_cal_view;
    private ArrayList<String> times;
    private ImageView img_isweekdayopen;
    private String isWeekendoff="0";
    public Map<Integer,String> preTimeMapCustom=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_by_consultant);
        getSupportActionBar().hide();
        initiateObj();
        init();
        getFiledsData();
        getTimeInterval();

    }


    private void init() {

        date_cal_view = findViewById(R.id.date_cal_view);
        img_isweekdayopen = findViewById(R.id.img_isweekdayopen);
        tv_saveAppointment = findViewById(R.id.tv_saveAppointment);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(date_cal_view.getDate());

        ImageView toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.set_appointment));
        toolbar_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_isweekdayopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isWeekendoff.equalsIgnoreCase("0"))
                {
                    isWeekendoff="1";
                    img_isweekdayopen.setImageResource(R.drawable.ic_button);
                }
                else
                {
                    isWeekendoff="0";
                    img_isweekdayopen.setImageResource(R.drawable.ic_unselect_button);
                }
            }
        });

        getAlreadyAddedTime();

    }



    private void viewTimingGrid(ArrayList<String> timingAr, Map<String, Boolean> reservedTimeMap) {
        GridView grid_timing = findViewById(R.id.grid_timing);
        System.out.println("Reserve time map==="+reservedTimeMap);

        grid_timing.setAdapter(new EditTimeGridAdapter(this, timingAr, reservedTimeMap,alreadySettedMap));
    }


    private void getTimeInterval()
    {
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
            m.put("consultant_id", getRestParamsName(Utilclass.consultant_id));
            m.put("start_date", date);
            m.put("end_date", date);
            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));

            System.out.println("Appointment before===" + m);
            reservedTimeMap.clear();

            serverHandler.sendToServer(this, getApiUrl() + "get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data===" + dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                JSONObject dataObj = jsonObject.getJSONObject("data");

                                JSONArray appointmentArray = dataObj.getJSONArray("appointment_time");
                                if(appointmentArray.length() > 0)
                                   {
                                    JSONObject appointmentData = appointmentArray.getJSONObject(0);
                                       isWeekendoff = appointmentData.getString("include_weekend_n_holidays");


                                    JSONArray timeArray = new JSONArray(appointmentData.getString("timing"));
                                    JSONArray jsonArrayReservedTime = dataObj.getJSONArray("reserved_times");
                                    ArrayList<JSONObject> timeMainAr = new ArrayList<>();
                                    int counter = 0;
                                    for (int x = 0; x < timeArray.length(); x++) {
                                        String timeStr = timeArray.getString(x);
                                        String[] timeSplitAr = timeStr.split("-");

                                        for (int y = 0; y < timeSplitAr.length; y++) {

                                            String timeSlot = timeSplitAr[y];
                                            if (jsonArrayReservedTime.length() > 0) {
                                                JSONObject reservedTime = jsonArrayReservedTime.getJSONObject(0);
                                                String appointment_duration = reservedTime.getString("appointment_duration");
                                                String[] splittedAr = reservedTime.getString("appointment_time").split(",");

                                                for (int z = 0; z < splittedAr.length; z++)
                                                {
                                                    String reservedtime = splittedAr[z];
                                                    String reservetimeAfterAddingMinutes = addTime(reservedtime, Integer.parseInt(appointment_duration));
                                                    boolean bool = checkTimeIsExistBeetWeenTwoTimes(reservedtime, reservetimeAfterAddingMinutes, timeSlot);
                                                   // System.out.println("Reservetime====>" + reservedtime + "===" + reservetimeAfterAddingMinutes);

                                                    if(bool)
                                                    {
                                                        reservedTimeMap.put(timeSlot, bool);
                                                    }
                                                    else
                                                    {
                                                        alreadySettedMap.put(timeSlot,false);
                                                    }
                                                }
                                            }
                                            System.out.println("Reserve time map==="+reservedTimeMap);

                                        }

                                    }

                                       if(isWeekendoff.equalsIgnoreCase("1"))
                                       {
                                           img_isweekdayopen.setImageResource(R.drawable.ic_button);

                                       }
                                       else
                                       {
                                           img_isweekdayopen.setImageResource(R.drawable.ic_unselect_button);
                                       }


                                   }
                                viewTimingGrid(times,reservedTimeMap);
                               }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(EditTimeByConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


    private String addTime(String myTime, int interval)
      {
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
            midtime = midtime + ":10";

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
        date_cal_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day)
            {
                month=month+1;
                String newmonth=month+"";
                String newday=day+"";
                if(month<=9)
                {
                    newmonth="0"+month;
                }
                if(day<=9)
                {
                    newday="0"+day;
                }
                date = year + "-"+newmonth + "-" + newday;
                getAlreadyAddedTime();
            }
        });


        tv_saveAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (timeDuration.length() == 0) {
//                    alertDialogs.alertDialog(EditTimeByConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_time_inter), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }


                saveTiming();

            }
        });

    }


    private void saveTiming() {

        try {

            if(preTimeMapCustom.size() > 0 && preTimeMapCustom.size() % 2 != 0)
            {
                alertDialogs.alertDialog(EditTimeByConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.selectvalidTimepair), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                    }
                });
                return;
            }

            final Map<String, String> m = new HashMap<>();
            m.put("start_date", date);
            m.put("end_date", date);
            m.put("consultant_id", getRestParamsName(Utilclass.consultant_id));
            m.put("include_weekend_n_holidays", isWeekendoff);



            Set<Integer> keyset =preTimeMapCustom.keySet();
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
                pairStr = pairStr + "-" + preTimeMapCustom.get(intList.get(x));
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

            m.put("timing", SavedArray+"");

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));


            System.out.println("Before to save Consultant===" + m);


            if (SavedArray.length() == 0) {
                alertDialogs.alertDialog(EditTimeByConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.selecttime), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                    @Override
                    public void getDialogEvent(String buttonPressed) {
                    }
                });
                return;
            }

            serverHandler.sendToServer(this, getApiUrl() + "set-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                alertDialogs.alertDialog(EditTimeByConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                    @Override
                                    public void getDialogEvent(String buttonPressed)
                                    {
                                        finish();
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(EditTimeByConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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

