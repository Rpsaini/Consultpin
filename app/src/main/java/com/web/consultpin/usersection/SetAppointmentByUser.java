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
import com.web.consultpin.adapter.TimeAndMoneyAdapter;
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
    private String date="2021-02-16";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_by_user);
        initiateObj();
        getSupportActionBar().hide();
        init();

    }


    private void init() {
        ImageView toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.set_appointment));

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




    private void listeners() {

    }

    private void viewTimingGrid()
    {

        GridView grid_timing = findViewById(R.id.grid_timing);
        final ArrayList<String> keysAr = new ArrayList<>();


//        keysAr.add(0);
//        grid_timing.setAdapter(new CustomTimingGrid(this, Cu));
    }




    private void showAlreadyAddedTime(ArrayList<String> data) {
        RecyclerView recyclerview_timeslot = findViewById(R.id.recyclerview_timeslot);

        recyclerview_timeslot.setNestedScrollingEnabled(false);
        recyclerview_timeslot.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerview_timeslot.setHasFixedSize(true);
        recyclerview_timeslot.setItemAnimator(new DefaultItemAnimator());
        TimeAndMoneyAdapter horizontalCategoriesAdapter = new TimeAndMoneyAdapter(data, SetAppointmentByUser.this);
        recyclerview_timeslot.setAdapter(horizontalCategoriesAdapter);

    }


    private void getAlreadyAddedTime() {
        try {
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));

            m.put("start_date", date);
            m.put("end_date", date);

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
                        if (jsonObject.getBoolean("status"))
                        {
                            try {

                                JSONObject dataObj=jsonObject.getJSONObject("data");
                                double oneHourfee=Double.parseDouble(dataObj.getString("consultant_fee"));
                                double halfHour=oneHourfee/2;
                                double quaterFee=halfHour/2;



                                ArrayList<String> data=new ArrayList<>();
                                data.add("15 min/"+(quaterFee)+getResources().getString(R.string.lirasymbol));
                                data.add("30 min/"+(halfHour)+getResources().getString(R.string.lirasymbol));
                                data.add("60 min/"+(oneHourfee)+getResources().getString(R.string.lirasymbol));





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