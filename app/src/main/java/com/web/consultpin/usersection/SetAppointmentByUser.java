package com.web.consultpin.usersection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
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
import com.web.consultpin.consultant.SetTimeByConsultant;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SetAppointmentByUser extends BaseActivity {



    private TextView ed_datefrom, ed_date_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_by_user);
        initiateObj();
        getSupportActionBar().hide();
        init();
        getTodayTiming();
    }


    private void init() {

        ImageView toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.set_appointment));

        ed_datefrom = findViewById(R.id.ed_datefrom);
        ed_date_end = findViewById(R.id.ed_date_end);
        toolbar_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewTimingGrid();
        listeners();
        getAlreadyAddedTime();

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
    }

    private void viewTimingGrid() {
        GridView grid_timing = findViewById(R.id.grid_timing);
        final ArrayList<String> keysAr = new ArrayList<>();

        int start = 12;
        for (int x = start; x <= 48; x++) {
            keysAr.add("");
        }
//        keysAr.add(0);
//        grid_timing.setAdapter(new CustomTimingGrid(this, Cu));
    }

    private void startDate(final TextView textView) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
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


    private void showAlreadyAddedTime() {
        RecyclerView recyclerview_alreadyAddedTime = findViewById(R.id.recyclerview_alreadyAddedTime);

        recyclerview_alreadyAddedTime.setNestedScrollingEnabled(false);
        recyclerview_alreadyAddedTime.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerview_alreadyAddedTime.setHasFixedSize(true);
        recyclerview_alreadyAddedTime.setItemAnimator(new DefaultItemAnimator());
        //AlreadyAddedTimeAdapter horizontalCategoriesAdapter = new AlreadyAddedTimeAdapter(new ArrayList<>(), SetAppointmentByUser.this);
        //recyclerview_alreadyAddedTime.setAdapter(horizontalCategoriesAdapter);

    }


    private void getAlreadyAddedTime() {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));


            serverHandler.sendToServer(this, getApiUrl() + "get-appointment-time", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        System.out.println("Appointment data===" + dta);
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {

                            try {


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
    }