//package com.web.consultpin.consultant;
//
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.DatePickerDialog;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.media.Image;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.DatePicker;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.app.dialogsnpickers.DialogCallBacks;
//import com.app.vollycommunicationlib.CallBack;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.web.consultpin.R;
//import com.web.consultpin.Utilclass;
//import com.web.consultpin.adapter.AlreadyAddedTimeAdapter;
//import com.web.consultpin.adapter.CategoryAdapter;
//import com.web.consultpin.adapter.CustomTimingGrid;
//import com.web.consultpin.main.BaseActivity;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Set;
//
//public class SetTimeByConsultant extends BaseActivity {
//    private TextView ed_datefrom, ed_date_end;
//    public ArrayList<String> customTimeArray = new ArrayList<>();
//    ImageView img_isweekdayopen;
//    public String include_weekend_n_holidays = "1", timing_type = "1";//quicktime 1   custom 2
//    public LinkedHashMap<Integer, String> preTimeMapCustom = new LinkedHashMap<>();
//    public LinkedHashMap<Integer, String> preTimeMapAlready = new LinkedHashMap<>();
//    public TextView tv_saveAppointment;
//    ImageView toolbar_back_arrow;
//    AlreadyAddedTimeAdapter horizontalCategoriesAdapter;
//    CustomTimingGrid customTimingGrid;
////    public TextView txt_date;
//
//    Map<String, JSONObject> datesMap = new HashMap<>();
//    List<String> datesStringAr = new ArrayList<String>();
//
//
//    public Map<String, ArrayList<JSONObject>> mainDataContainerMap = new HashMap<>();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_set_appoint_ment);
//        initiateObj();
//        getSupportActionBar().hide();
//        init();
//        getTodayTiming();
//        getAlreadyAddedTime();
//        getTimeInterval();
//    }
//
//    private void getTimeInterval() {
//        String[] quarterHours = {"00", "15", "30", "45"};
//        ArrayList<String> times = new ArrayList<String>(); // <-- List instead of array
//
//        for (int i = 0; i < 24; i++) {
//            for (int j = 0; j < 4; j++) {
//                String time = i + ":" + quarterHours[j];
//                if (i < 10) {
//                    time = "0" + time;
//                }
//                times.add(time); // <-- no need to care about indexes
//                //System.out.println("Time interval====>"+time);
//            }
//            viewTimingGrid(times);
//        }
//    }
//
//    private void init() {
//
//        toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
//        TextView toolbar_title = findViewById(R.id.toolbar_title);
//
//        toolbar_title.setText(getResources().getString(R.string.set_appointment_time));
//
//        ed_datefrom = findViewById(R.id.ed_datefrom);
//        ed_date_end = findViewById(R.id.ed_date_end);
//        img_isweekdayopen = findViewById(R.id.img_isweekdayopen);
//        tv_saveAppointment = findViewById(R.id.tv_saveAppointment);
//
//        listeners();
//
//
//    }
//
//
//
//    private void getTodayTiming() {
//
//        try {
//
//            Date todayDate = Calendar.getInstance().getTime();
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String todayString = formatter.format(todayDate);
//            ed_datefrom.setText(todayString);
//            ed_date_end.setText(todayString);
//
//        } catch (Exception e) {
//            System.out.println("Message====" + e.getMessage());
//        }
//
//    }
//
//
//    private void listeners() {
//        toolbar_back_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        ed_datefrom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startDate((TextView) v);
//            }
//        });
//
//        ed_date_end.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startDate((TextView) v);
//            }
//        });
//
//        img_isweekdayopen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (include_weekend_n_holidays.equalsIgnoreCase("1")) {
//                    include_weekend_n_holidays = "0";
//                    img_isweekdayopen.setImageResource(R.drawable.ic_unselect_button);
//                } else {
//                    include_weekend_n_holidays = "1";
//                    img_isweekdayopen.setImageResource(R.drawable.ic_button);
//
//                }
//
//                tv_saveAppointment.setAlpha(1f);
//                tv_saveAppointment.setEnabled(true);
//
//
//            }
//        });
//
//        tv_saveAppointment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveTiming();
//            }
//        });
//
//
//    }
//
//    public void viewTimingGrid(ArrayList<String> dataAr) {
//
//        GridView grid_timing = findViewById(R.id.grid_timing);
//        customTimingGrid = new CustomTimingGrid(this, dataAr);
//        grid_timing.setAdapter(customTimingGrid);
//    }
//
//    private void startDate(final TextView textView) {
//
//        final Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//        DatePickerDialog dpd = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        textView.setText(year + "-"
//                                + (monthOfYear + 1) + "-" + dayOfMonth);
//
//                        //getDatesBetweenTWoDates(ed_datefrom.getText().toString(), ed_date_end.getText().toString());
//
//
//                    }
//                }, mYear, mMonth, mDay);
//        dpd.show();
//
//
//    }
//
//
//    private void showAlreadyAddedTime(JSONArray commonTime, String dateStr) {
//        RecyclerView recyclerview_alreadyAddedTime = findViewById(R.id.recyclerview_alreadyAddedTime);
//        recyclerview_alreadyAddedTime.setNestedScrollingEnabled(false);
//        recyclerview_alreadyAddedTime.setLayoutManager(new LinearLayoutManager(SetTimeByConsultant.this,
//                LinearLayoutManager.HORIZONTAL, false));
//        recyclerview_alreadyAddedTime.setHasFixedSize(true);
//        recyclerview_alreadyAddedTime.setItemAnimator(new DefaultItemAnimator());
//        horizontalCategoriesAdapter = new AlreadyAddedTimeAdapter(commonTime, this, dateStr);
//        recyclerview_alreadyAddedTime.setAdapter(horizontalCategoriesAdapter);
//    }
//
//
//    private void getAlreadyAddedTime() {
//        try {
//            mainDataContainerMap.clear();
//            final Map<String, String> m = new HashMap<>();
//            m.put("consultant_id", getRestParamsName(Utilclass.consultant_id));
//            m.put("start_date", ed_datefrom.getText().toString());
//            m.put("end_date", ed_date_end.getText().toString());
//
//            m.put("device_type", "android");
//            m.put("device_token", getDeviceToken() + "");
//
//            final Map<String, String> obj = new HashMap<>();
//            obj.put("token", getRestParamsName(Utilclass.token));
//
//            System.out.println("before=====" + m);
//            serverHandler.sendToServer(this, getApiUrl() + "get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
//                @Override
//                public void getRespone(String dta, ArrayList<Object> respons) {
//                    try {
//                        System.out.println("Appointment data===" + dta);
//                        JSONObject jsonObject = new JSONObject(dta);
//                        if (jsonObject.getBoolean("status")) {
//                            try {
//                                JSONObject data = jsonObject.getJSONObject("data");
//                                JSONArray quickAr = data.getJSONArray("quick_time_list");
//                                showAlreadyAddedTime(quickAr, ed_datefrom.getText().toString());
//
//                                JSONArray appointment_timeAr = data.getJSONArray("appointment_time");
////                                JSONObject gdataOb = new JSONObject();
//                                for (int x = 0; x < appointment_timeAr.length(); x++) {
//                                    JSONObject dataObj = appointment_timeAr.getJSONObject(x);
////                                    gdataOb = dataObj;
//                                    datesMap.put(dataObj.getString("start_date"), dataObj);
//
//                                    String timingStr = dataObj.getString("timing");
//                                    String timing_type = dataObj.getString("timing_type");
//                                    String include_weekend_n_holidays = dataObj.getString("include_weekend_n_holidays");
//                                    String start_date = dataObj.getString("start_date");
//
//                                    JSONArray timeAR = new JSONArray(timingStr);
//                                    ArrayList<JSONObject> quickCustomJAr = new ArrayList<>();
//                                    for (int y = 0; y < timeAR.length(); y++) {
//                                        JSONObject jsonData = new JSONObject();
//                                        jsonData.put("isQuick", timing_type);
//                                        jsonData.put("time", timeAR.getString(y));
//                                        jsonData.put("timing", timingStr);
//                                        jsonData.put("include_weekend_n_holidays", include_weekend_n_holidays);
//                                        quickCustomJAr.add(jsonData);
//                                    }
//                                    mainDataContainerMap.put(start_date, quickCustomJAr);
//
//                                }
//                                System.out.println("Map data====" + mainDataContainerMap);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                                @Override
//                                public void getDialogEvent(String buttonPressed) {
//                                }
//                            });
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//    private void saveTiming() {
//
//        try {
//
//            if (preTimeMapCustom.size() > 0 && preTimeMapCustom.size() % 2 != 0)
//            {
//                alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.selectvalidTimepair), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                    @Override
//                    public void getDialogEvent(String buttonPressed) {
//                    }
//                });
//                return;
//            }
//
//            final Map<String, String> m = new HashMap<>();
//            m.put("start_date", ed_datefrom.getText().toString());
//            m.put("end_date", ed_date_end.getText().toString());
//            m.put("consultant_id", getRestParamsName(Utilclass.consultant_id));
//            m.put("include_weekend_n_holidays", include_weekend_n_holidays);
//
//            Set<Integer> keyset =preTimeMapCustom.keySet();
//            Iterator<Integer> intItr=keyset.iterator();
//            List<Integer> intList = new ArrayList<Integer>();
//            while (intItr.hasNext())
//            {
//                intList.add(intItr.next());
//            }
//
//            Collections.sort(intList, new Comparator<Integer>() {
//
//                public int compare(Integer o1, Integer o2) {
//                    // for Accending order
//                    return o1 - o2;
//                }
//            });
//
//
//            JSONArray SavedArray = new JSONArray();
//            int paircount = 0;
//            String pairStr = "";
//            for (int x=0;x<intList.size();x++)
//            {
//                pairStr = pairStr + "-" + preTimeMapCustom.get(intList.get(x));
//                if (paircount == 1)
//                {
//                    pairStr=pairStr.replaceFirst("-","");
//                    SavedArray.put(pairStr);
//                    m.put("timing_type", "2");
//                    paircount = -1;
//                    pairStr="";
//                }
//                paircount++;
//            }
//
//
//            //sort key
//
//
//            for (Map.Entry<Integer, String> map : preTimeMapAlready.entrySet()) {
//                SavedArray.put(map.getValue());
//                m.put("timing_type", "1");
//            }
//            m.put("timing", SavedArray+"");
//
//
//            m.put("device_type", "android");
//            m.put("device_token", getDeviceToken() + "");
//
//            final Map<String, String> obj = new HashMap<>();
//            obj.put("token", getRestParamsName(Utilclass.token));
//
//
//            System.out.println("Before to save Consultant===" + m);
//
//
//            if (SavedArray.length() == 0) {
//                alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Required), getResources().getString(R.string.selecttime), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                    @Override
//                    public void getDialogEvent(String buttonPressed) {
//                    }
//                });
//                return;
//            }
//
//            serverHandler.sendToServer(this, getApiUrl() + "set-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
//                @Override
//                public void getRespone(String dta, ArrayList<Object> respons) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(dta);
//                        if (jsonObject.getBoolean("status")) {
//                            try {
//
//                                alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                                    @Override
//                                    public void getDialogEvent(String buttonPressed)
//                                    {
//                                        finish();
//                                    }
//                                });
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//                            alertDialogs.alertDialog(SetTimeByConsultant.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                                @Override
//                                public void getDialogEvent(String buttonPressed) {
//                                }
//                            });
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public void notifyMap(Map<Integer, String> map, String type) {
//        if (type.equalsIgnoreCase("custom")) {
//            preTimeMapCustom.clear();
//            customTimingGrid.notifyDataSetChanged();
//        } else if (type.equalsIgnoreCase("already")) {
//            preTimeMapAlready.clear();
//            horizontalCategoriesAdapter.notifyDataSetChanged();
//        }
//    }
//
//
//
//}