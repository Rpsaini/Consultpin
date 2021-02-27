package com.web.consultpin.usersection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IInterface;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.AlreadyAddedTimeAdapter;
import com.web.consultpin.adapter.CustomTimingGrid;
import com.web.consultpin.adapter.RequestForAppobyUserAdapter;
import com.web.consultpin.adapter.TimeAndMoneyAdapter;
import com.web.consultpin.consultant.SetTimeByConsultant;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SetAppointmentByUser extends BaseActivity {
    private String date = "";
    public String timeDuration = "", timeSlot = "";
    Map<String, Boolean> reservedTimeMap = new HashMap<>();

    TextView tv_saveAppointment;
    CalendarView date_cal_view;
    ArrayList<JSONObject> allTimeIntervalAr=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_by_user);
        getSupportActionBar().hide();
        initiateObj();
        init();
        getFiledsData();


    }


    private void init() {
        ed_description = findViewById(R.id.ed_description);
        date_cal_view = findViewById(R.id.date_cal_view);
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


        listeners();
        getAlreadyAddedTime();


    }


    private void listeners() {

    }

    GridView grid_timing;

    private void viewTimingGrid(ArrayList<JSONObject> timingAr, Map<String, Boolean> reservedTimeMap) {

        grid_timing = findViewById(R.id.grid_timing);
        grid_timing.setAdapter(new RequestForAppobyUserAdapter(this, timingAr, reservedTimeMap,date));
        showData(timingAr);

    }

    private void showAlreadyAddedTime(ArrayList<String> data) {
        RecyclerView recyclerview_timeslot = findViewById(R.id.recyclerview_timeslot);

        recyclerview_timeslot.setNestedScrollingEnabled(false);
        recyclerview_timeslot.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerview_timeslot.setHasFixedSize(true);
        recyclerview_timeslot.setItemAnimator(new DefaultItemAnimator());
        TimeAndMoneyAdapter horizontalCategoriesAdapter = new TimeAndMoneyAdapter(data, SetAppointmentByUser.this);
        recyclerview_timeslot.setAdapter(horizontalCategoriesAdapter);

    }


    private void getAlreadyAddedTime() {
        try {
            reservedTimeMap.clear();
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));

            m.put("start_date", date);
            m.put("end_date", date);

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));

            System.out.println("Appointment before===" + m);


            serverHandler.sendToServer(this, getApiUrl() + "get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data===" + dta);
                        allTimeIntervalAr.clear();
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                double oneHourfee = Double.parseDouble(dataObj.getString("consultant_fee"));
                                double halfHour = oneHourfee / 2;
                                double quaterFee = halfHour / 2;


                                ArrayList<String> data = new ArrayList<>();
                                data.add("15 min/" + (quaterFee) + " " + getResources().getString(R.string.lirasymbol));
                                data.add("30 min/" + (halfHour) + " " + getResources().getString(R.string.lirasymbol));
                                data.add("60 min/" + (oneHourfee) + " " + getResources().getString(R.string.lirasymbol));


                                showAlreadyAddedTime(data);


                                JSONArray appointmentArray = dataObj.getJSONArray("appointment_time");
                                if (appointmentArray.length() > 0) {
                                    JSONObject appointmentData = appointmentArray.getJSONObject(0);
                                    String isWeekendoff = appointmentData.getString("include_weekend_n_holidays");

                                    JSONArray timeArray = new JSONArray(appointmentData.getString("timing"));
                                    JSONArray jsonArrayReservedTime = dataObj.getJSONArray("reserved_times");
                                   // ArrayList<JSONObject> timeMainAr = new ArrayList<>();
                                    int counter = 0;
                                    for (int x = 0; x < timeArray.length(); x++) {
                                        String timeStr = timeArray.getString(x);
                                        String[] timeSplitAr = timeStr.split("-");


                                        String startTime = date+" "+timeSplitAr[0];
                                        String endTime = date+" "+timeSplitAr[1];
                                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        getTimeIntervals(timeSplitAr[0],sim.parse(startTime), sim.parse(endTime),isWeekendoff);

                                    }


                                    if (jsonArrayReservedTime.length() > 0)
                                     {
                                        for (int x = 0; x < allTimeIntervalAr.size(); x++) {
                                            JSONObject allTimeAdded = allTimeIntervalAr.get(x);
                                            String time = allTimeAdded.getString("timing");
                                            JSONObject reservedTime = jsonArrayReservedTime.getJSONObject(0);
                                            String[] splittedAr = reservedTime.getString("appointment_time").split(",");
                                            for (int z = 0; z < splittedAr.length; z++)
                                                 {
                                                    String reservedtime = splittedAr[z];
                                                     System.out.println("Reserve timee==x="+reservedtime);
                                                     String appointment_duration = reservedTime.getString("appointment_duration");
                                                    String reservetimeAfterAddingMinutes = addTime(reservedtime, Integer.parseInt(appointment_duration));
                                                    boolean bool = checkTimeIsExistBeetWeenTwoTimes(reservedtime, reservetimeAfterAddingMinutes, time);
                                                    if(bool)
                                                    {
                                                        reservedTimeMap.put(time, bool);
                                                    }
                                                }
                                        }
                                    }

                                         System.out.println("Reserve time data==="+reservedTimeMap);



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
                            alertDialogs.alertDialog(SetAppointmentByUser.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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



    private void showData(ArrayList<JSONObject> dataAr)
    {
        RelativeLayout relativeLayout = findViewById(R.id.rr_nodata_view);
        if(dataAr.size()==0)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            grid_timing.setVisibility(View.GONE);
            tv_saveAppointment.setVisibility(View.GONE);
        }
        else
        {
            relativeLayout.setVisibility(View.GONE);
            grid_timing.setVisibility(View.VISIBLE);
            tv_saveAppointment.setVisibility(View.VISIBLE);
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


            System.out.println("Start==end--mid===="+startTime+"=="+endTime+"=="+midtime);
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(startTime);
            Calendar calendar1 = Calendar.getInstance();
//          calendar1.add(Calendar.MINUTE, -1);
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


//            String string2 = "14:49:00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);


            Date d = new SimpleDateFormat("HH:mm:ss").parse(midtime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime()))
            {

                return true;

            }
             if (x.equals(calendar1.getTime()) || x.equals(calendar2.getTime()))
            {
                return true;
            }

            else {
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
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                month = month + 1;
                String newMonth = month + "", newDaye = day + "";
                if (month <= 9) {
                    newMonth = "0" + month;
                }
                if (day <= 9) {
                    newDaye = "0" + day;
                }
                date = year + "-" + newMonth + "-" + newDaye;
                getAlreadyAddedTime();
            }
        });


        tv_saveAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                if (validationRule.checkEmptyString(ed_description) == 0) {
                    alertDialogs.alertDialog(SetAppointmentByUser.this, getResources().getString(R.string.Required), getResources().getString(R.string.add_comment_msg), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (timeDuration.length() == 0) {
                    alertDialogs.alertDialog(SetAppointmentByUser.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_time_inter), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }
                if (timeSlot.length() == 0) {
                    alertDialogs.alertDialog(SetAppointmentByUser.this, getResources().getString(R.string.Required), getResources().getString(R.string.select_time), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                        @Override
                        public void getDialogEvent(String buttonPressed) {

                        }
                    });
                    return;
                }

                saveAppointment();

            }
        });

    }


    private void saveAppointment()
    {


        try {
            String[] slotAr = timeDuration.split("/");
            String duration = slotAr[0].split(" ")[0];
            String fee = slotAr[1].split(" ")[0];

            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));
            m.put("user_id", getRestParamsName(Utilclass.user_id));
            m.put("description", ed_description.getText().toString());
            m.put("appointment_duration", duration);
            m.put("appointment_date", date);
            m.put("appointment_time", timeSlot + ":00");
            m.put("appointment_fee", fee + "");

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));

            System.out.println("Before to save appointment===" + m);

            serverHandler.sendToServer(this, getApiUrl() + "create-appointment-request", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try
                             {
                                alertDialogs.alertDialog(SetAppointmentByUser.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                    @Override
                                    public void getDialogEvent(String buttonPressed) {
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(SetAppointmentByUser.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


    private void getTimeIntervals(String startAdd,Date startTime,Date EndTime,String isWeekendoff)
    {
        System.out.println("Start dataaa===?"+startTime+"=="+EndTime);

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            if(BaseActivity.compareTwoDates(date+" "+startAdd+":00",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())))
            {
                JSONObject jsonPreobj = new JSONObject();
                jsonPreobj.put("timing", startAdd);
                jsonPreobj.put("include_weekend_n_holidays", isWeekendoff);
                allTimeIntervalAr.add(jsonPreobj);
            }

            while (cal.getTime().before(EndTime))
            {
                cal.add(Calendar.MINUTE, 15);
                JSONObject jsonObject1 = new JSONObject();
                String time=new java.sql.Time(cal.getTimeInMillis())+"";
                time=time.substring(0,time.lastIndexOf(":"));
                jsonObject1.put("timing", time);
                jsonObject1.put("include_weekend_n_holidays", isWeekendoff);

                if(BaseActivity.compareTwoDates(date+" "+time+":00",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())))
                {
                    System.out.println("Timmm===>>>>"+time);
                    allTimeIntervalAr.add(jsonObject1);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }


}