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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SetTimeByConsultant extends BaseActivity {
    private TextView ed_datefrom, ed_date_end;
    public ArrayList<JSONObject> customTimeArray = new ArrayList<>();
    ImageView img_isweekdayopen;
    private String include_weekend_n_holidays="1",timing_type="1";
    public Map<Integer, String> preTimeMapCustom = new HashMap<>();
    public TextView tv_saveAppointment;
    ImageView toolbar_back_arrow;

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
        toolbar_title.setText(getResources().getString(R.string.set_appointment));

        ed_datefrom = findViewById(R.id.ed_datefrom);
        ed_date_end = findViewById(R.id.ed_date_end);
        img_isweekdayopen = findViewById(R.id.img_isweekdayopen);
        tv_saveAppointment = findViewById(R.id.tv_saveAppointment);

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
            public void onClick(View v) {
             if(include_weekend_n_holidays.equalsIgnoreCase("1"))
             {
                 include_weekend_n_holidays="0";
                 img_isweekdayopen.setImageResource(R.drawable.ic_unselect_button);
             }
             else
             {
                 include_weekend_n_holidays="1";
                 img_isweekdayopen.setImageResource(R.drawable.ic_button);
             }
            }
        });

        tv_saveAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTiming();
            }
        });
    }

    public void viewTimingGrid(ArrayList<JSONObject> dataAr)
    {

        GridView grid_timing = findViewById(R.id.grid_timing);
        dataAr.add(null);


        CustomTimingGrid customTimingGrid= new CustomTimingGrid(this, customTimeArray);
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
                        // Display Selected date in textbox
                        textView.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();


    }


    private void showAlreadyAddedTime(JSONArray commonTime)
     {
        RecyclerView recyclerview_alreadyAddedTime = findViewById(R.id.recyclerview_alreadyAddedTime);

        recyclerview_alreadyAddedTime.setNestedScrollingEnabled(false);
        recyclerview_alreadyAddedTime.setLayoutManager(new LinearLayoutManager(SetTimeByConsultant.this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerview_alreadyAddedTime.setHasFixedSize(true);
        recyclerview_alreadyAddedTime.setItemAnimator(new DefaultItemAnimator());
        AlreadyAddedTimeAdapter horizontalCategoriesAdapter = new AlreadyAddedTimeAdapter(commonTime, this);
        recyclerview_alreadyAddedTime.setAdapter(horizontalCategoriesAdapter);


    }


    private void getAlreadyAddedTime()
      {
        try
        {
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));
            m.put("start_date",ed_datefrom.getText().toString());
            m.put("end_date", ed_date_end.getText().toString());

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));

            System.out.println("before====="+m);
            serverHandler.sendToServer(this, getApiUrl()+"get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data===" + dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            try {
                                    JSONObject data=jsonObject.getJSONObject("data");
                                    showAlreadyAddedTime(data.getJSONArray("quick_time_list"));

                                JSONArray appointment_timeAr=data.getJSONArray("appointment_time");

                                  for(int x=0;x<appointment_timeAr.length();x++)
                                  {
                                      customTimeArray.add(appointment_timeAr.getJSONObject(x));

                                  }

                                viewTimingGrid(customTimeArray);


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




    private void saveTiming() {

        try {

            final Map<String, String> m = new HashMap<>();
            m.put("start_date", ed_datefrom.getText().toString());
            m.put("end_date", ed_date_end.getText().toString());
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));
            m.put("timing_type", timing_type);
            m.put("include_weekend_n_holidays", include_weekend_n_holidays);

            System.out.println("Timings===="+preTimeMapCustom);
            String timings="";
            for(Map.Entry<Integer,String> map:preTimeMapCustom.entrySet())
            {
                timings=timings+","+map.getValue();
            }
            m.put("timing", timings);



            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));


            System.out.println("Before to save Consultant==="+m);

            serverHandler.sendToServer(this, getApiUrl() + "set-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            try
                            {
//                                String baseUrl = jsonObject.getString("icon_base_url");
//                                JSONArray categories = jsonObject.getJSONArray("categories");
//                                ArrayList<JSONObject> catAr = new ArrayList<>();
//                                for (int x = 0; x < categories.length(); x++) {
//                                    catAr.add(categories.getJSONObject(x));
//                                }
//                                initHomeCategory(baseUrl,catAr);


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


}