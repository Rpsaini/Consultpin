package com.web.consultpin.consultant;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.google.gson.JsonArray;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.AlreadyAddedTimeAdapter;
import com.web.consultpin.adapter.CategoryAdapter;
import com.web.consultpin.adapter.CustomTimingGrid;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SetTimeByConsultant extends BaseActivity {
    private TextView ed_datefrom, ed_date_end;
    public ArrayList<String> customTimeArray = new ArrayList<>();
    ImageView img_isweekdayopen;
    public String include_weekend_n_holidays = "1", timing_type = "1";//quicktime 1   custom 2
    public Map<Integer, String> preTimeMapCustom = new HashMap<>();
    public Map<Integer, String> preTimeMapAlready = new HashMap<>();
    public TextView tv_saveAppointment;
    ImageView toolbar_back_arrow, img_prev, img_next;
    AlreadyAddedTimeAdapter horizontalCategoriesAdapter;
    CustomTimingGrid customTimingGrid;
    private TextView txt_date;

    Map<String, JSONObject> datesMap = new HashMap<>();
    List<String> datesStringAr = new ArrayList<String>();

    public Map<String,ArrayList<JSONObject>> mainDataContainerMap=new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appoint_ment);
        initiateObj();
        getSupportActionBar().hide();
        init();
        getTodayTiming();
        getAlreadyAddedTime();
    }


    private void init() {

        toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        img_prev = findViewById(R.id.img_prev);
        txt_date = findViewById(R.id.txt_date);
        img_next = findViewById(R.id.img_next);

        toolbar_title.setText(getResources().getString(R.string.set_appointment));

        ed_datefrom = findViewById(R.id.ed_datefrom);
        ed_date_end = findViewById(R.id.ed_date_end);
        img_isweekdayopen = findViewById(R.id.img_isweekdayopen);
        tv_saveAppointment = findViewById(R.id.tv_saveAppointment);

        listeners();


    }

    int indexCount = 1;

    private void getTodayTiming() {

        try {

            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = formatter.format(todayDate);
            ed_datefrom.setText(todayString);
            ed_date_end.setText(todayString);
            txt_date.setText(ed_datefrom.getText().toString());
            getDatesBetweenTWoDates(todayString, todayString);

        } catch (Exception e) {
            System.out.println("Message====" + e.getMessage());
        }

    }


    private void listeners() {
        toolbar_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            public void onClick(View v)
            {
                if (include_weekend_n_holidays.equalsIgnoreCase("1")) {
                    include_weekend_n_holidays = "0";
                    img_isweekdayopen.setImageResource(R.drawable.ic_unselect_button);
                } else {
                    include_weekend_n_holidays = "1";
                    img_isweekdayopen.setImageResource(R.drawable.ic_button);
                }
            }
        });

        tv_saveAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                saveTiming("nothing");
            }
        });


        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (indexCount < datesStringAr.size() - 1)
                 {
                  if(preTimeMapAlready.size()==0&&preTimeMapCustom.size()==0)
                  {
                      alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.selecttime), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                          @Override
                          public void getDialogEvent(String buttonPressed)
                          {

                          }
                      });
                  }
                  else {

                      alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.wouldyouliketosave), getResources().getString(R.string.yes),  "", new DialogCallBacks() {
                          @Override
                          public void getDialogEvent(String buttonPressed)
                          {
//                              if(buttonPressed.equalsIgnoreCase("yes"))
//                              {
//                                  saveTiming("next");
//                              }
//                              else
//                              {
                                 if(customTimeArray.size()==0) {
                                     papulateDataMap(datesStringAr.get(indexCount));
                                     indexCount++;
                                     String nextDate = datesStringAr.get(indexCount).toString();
                                     JSONObject dataObj = datesMap.get(nextDate);
                                     txt_date.setText(nextDate + "");
                                     parseJsonArrayNSetGridData(dataObj, nextDate);

                              }

                          }
                      });


                  }
                }
            }
        });

        img_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(indexCount > 1)
                 {
//                    if(preTimeMapAlready.size()==0&&preTimeMapCustom.size()==0)
//                    {
//                        alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.selecttime), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                            @Override
//                            public void getDialogEvent(String buttonPressed)
//                            {
//
//                            }
//                        });
//                    }
//                    else
//                      {
//                        papulateDataMap(datesStringAr.get(indexCount));
                        indexCount--;
                        String prevDate = datesStringAr.get(indexCount).toString();
                        JSONObject dataObj = datesMap.get(prevDate);
                        txt_date.setText(prevDate);
                        parseJsonArrayNSetGridData(dataObj,prevDate);
                        getAlreadyAddedTime();
                //    }
                }
            }
        });
    }

    public void viewTimingGrid(ArrayList<String> dataAr, String isWeekOpen,String dateStr) {

        GridView grid_timing = findViewById(R.id.grid_timing);
        dataAr.add(null);
        customTimingGrid = new CustomTimingGrid(this, customTimeArray, isWeekOpen,dateStr);
        grid_timing.setAdapter(customTimingGrid);
    }

    private void startDate(final TextView textView) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        textView.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                        getDatesBetweenTWoDates(ed_datefrom.getText().toString(), ed_date_end.getText().toString());


                    }
                }, mYear, mMonth, mDay);
        dpd.show();


    }


    private void showAlreadyAddedTime(JSONArray commonTime) {
        RecyclerView recyclerview_alreadyAddedTime = findViewById(R.id.recyclerview_alreadyAddedTime);
        recyclerview_alreadyAddedTime.setNestedScrollingEnabled(false);
        recyclerview_alreadyAddedTime.setLayoutManager(new LinearLayoutManager(SetTimeByConsultant.this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerview_alreadyAddedTime.setHasFixedSize(true);
        recyclerview_alreadyAddedTime.setItemAnimator(new DefaultItemAnimator());
        horizontalCategoriesAdapter = new AlreadyAddedTimeAdapter(commonTime, this);
        recyclerview_alreadyAddedTime.setAdapter(horizontalCategoriesAdapter);
    }


    private void getAlreadyAddedTime() {
        try {
            mainDataContainerMap.clear();
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));
//            m.put("consultant_id", "46");
            m.put("start_date", ed_datefrom.getText().toString());
            m.put("end_date", ed_date_end.getText().toString());

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));

            System.out.println("before=====" + m);
            serverHandler.sendToServer(this, getApiUrl() + "get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data===" + dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            try {
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONArray quickAr=data.getJSONArray("quick_time_list");
                                showAlreadyAddedTime(quickAr);

                                JSONArray appointment_timeAr = data.getJSONArray("appointment_time");
                                JSONObject gdataOb = new JSONObject();
                                for(int x = 0; x < appointment_timeAr.length(); x++)
                                {
                                    JSONObject dataObj = appointment_timeAr.getJSONObject(x);
                                    gdataOb = dataObj;
                                    datesMap.put(dataObj.getString("start_date"), dataObj);


                                    String timingStr=dataObj.getString("timing");
                                    String timing_type=dataObj.getString("timing_type");
                                    String start_date=dataObj.getString("start_date");

                                    JSONArray timeAR=new JSONArray(timingStr);
                                    ArrayList<JSONObject> quickCustomJAr=new ArrayList<>();
                                    for(int y=0;y<timeAR.length();y++)
                                    {
                                        JSONObject jsonData = new JSONObject();
                                        jsonData.put("isQuick", timing_type);
                                        jsonData.put("time", timeAR.getString(y));
                                        quickCustomJAr.add(jsonData);
                                    }
                                    mainDataContainerMap.put(start_date,quickCustomJAr);

                                }




                                System.out.println("Map data===="+mainDataContainerMap);



                                parseJsonArrayNSetGridData(gdataOb,ed_datefrom.getText().toString());

                            } catch (Exception e)
                             {
                                e.printStackTrace();
                             }

                        } else {
                            alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


    private void saveTiming(final String typeStr) {

        try
            {
            final Map<String, String> m = new HashMap<>();
            m.put("start_date", txt_date.getText().toString());
            m.put("end_date", txt_date.getText().toString());
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));

            m.put("include_weekend_n_holidays", include_weekend_n_holidays);
            String timings = "";

            preTimeMapCustom.remove(-1);
            JSONArray SavedArray=new JSONArray();
            for (Map.Entry<Integer, String> map : preTimeMapCustom.entrySet()) {
                SavedArray.put(map.getValue());
                m.put("timing_type", "2");
            }

           for (Map.Entry<Integer, String> map : preTimeMapAlready.entrySet()) {
                    SavedArray.put(map.getValue());
               m.put("timing_type", "1");
                }
            m.put("timing", SavedArray+"");


            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));


            System.out.println("Before to save Consultant===" + m);

            serverHandler.sendToServer(this, getApiUrl() + "set-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try {

                                if(typeStr.equalsIgnoreCase("next"))
                                {
//                                    papulateDataMap(datesStringAr.get(indexCount));
//                                    indexCount++;
//                                    String nextDate = datesStringAr.get(indexCount).toString();
//                                    JSONObject dataObj = datesMap.get(nextDate);
//                                    txt_date.setText(nextDate+"");
//                                    parseJsonArrayNSetGridData(dataObj,nextDate);
                                    customTimeArray.clear();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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


    public void notifyMap(Map<Integer, String> map,String type) {
        map.clear();
        if(type.equalsIgnoreCase("custom"))
        {
            customTimingGrid.notifyDataSetChanged();
        }
        else if(type.equalsIgnoreCase("already"))
        {
            horizontalCategoriesAdapter.notifyDataSetChanged();
        }
    }


    private void getDatesBetweenTWoDates(String s, String e) {
        try {
            List<Date> dates = new ArrayList<Date>();
            datesMap.clear();
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = (Date) formatter.parse(s);
                endDate = (Date) formatter.parse(e);
            } catch (ParseException em) {
                em.printStackTrace();
            }

            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {

                dates.add(new Date(curTime));
                curTime += interval;
            }
            for (int i = 0; i < dates.size(); i++) {
                Date lDate = (Date) dates.get(i);
                String ds = formatter.format(lDate);
                System.out.println(" Date is ..." + ds);
                datesMap.put(ds, new JSONObject());
                datesStringAr.add(ds);
            }

            if (datesMap.size() > 0) {
                getAlreadyAddedTime();
            }
        } catch (Exception em) {
            em.printStackTrace();
        }
    }


    private void parseJsonArrayNSetGridData(JSONObject customObj,String dateStr)
    {
        try {
            customTimeArray.clear();
            preTimeMapCustom.clear();
            preTimeMapAlready.clear();
            if(horizontalCategoriesAdapter!=null) {
                horizontalCategoriesAdapter.notifyDataSetChanged();
            }
            if (customObj != null) {
                if (customObj.has("timing")) {
                    String timingStr = customObj.getString("timing");
                    JSONArray timingAr = new JSONArray(timingStr);
                    include_weekend_n_holidays = customObj.getString("include_weekend_n_holidays");

                    for (int x = 0; x < timingAr.length(); x++) {
                        customTimeArray.add(timingAr.getString(x));
                    }
                }
            }
            viewTimingGrid(customTimeArray, include_weekend_n_holidays,dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void papulateDataMap(String currentDat)
    {
        ArrayList<JSONObject> AlreadydataAr=new ArrayList<>();
        if(mainDataContainerMap.containsKey(currentDat))
        {
            AlreadydataAr=mainDataContainerMap.get(currentDat);
        }
        for(Map.Entry<Integer,String> preMap:preTimeMapAlready.entrySet())
        {
            try {
                JSONObject jsonData=new JSONObject();
                jsonData.put("isQuick","1");
                jsonData.put("time",preMap.getValue());
                AlreadydataAr.add(jsonData);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        mainDataContainerMap.put(currentDat,AlreadydataAr);

//==================================
        ArrayList<JSONObject> customDateAr=new ArrayList<>();
        if(mainDataContainerMap.containsKey(currentDat))
        {
            customDateAr=mainDataContainerMap.get(currentDat);
        }
        for(Map.Entry<Integer,String> customeMAp:preTimeMapCustom.entrySet())
        {
            if(customeMAp.getKey()>0) {
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("isQuick", "1");
                    jsonData.put("time", customeMAp.getValue());
                    customDateAr.add(jsonData);
                    System.out.println("customeMAp====" + customeMAp.getKey() + "===" + customeMAp.getValue());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        mainDataContainerMap.put(currentDat,customDateAr);



        for(Map.Entry<String,ArrayList<JSONObject>> mapData:mainDataContainerMap.entrySet())
        {

            System.out.println("Dates===="+mapData.getKey());
            ArrayList<JSONObject> dataObje=mapData.getValue();

            for(int x=0;x<dataObje.size();x++)
            {
                try {
                    JSONObject jObj = dataObje.get(x);
                    System.out.println("Daytes Key==="+jObj.getString("isQuick")+"==="+jObj.getString("time"));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }






}