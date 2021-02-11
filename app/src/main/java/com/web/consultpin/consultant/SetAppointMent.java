package com.web.consultpin.consultant;

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
import com.web.consultpin.MainActivity;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.adapter.AlreadyAddedTimeAdapter;
import com.web.consultpin.adapter.CustomTimingGrid;
import com.web.consultpin.main.BaseActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SetAppointMent extends BaseActivity {
    private TextView ed_datefrom, ed_date_end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appoint_ment);
        initiateObj();
        getSupportActionBar().hide();
        init();
    }
    private void init()
    {
        ImageView toolbar_back_arrow = findViewById(R.id.toolbar_back_arrow);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.set_appointment));

        ed_datefrom = findViewById(R.id.ed_datefrom);
        ed_date_end = findViewById(R.id.ed_date_end);
        toolbar_back_arrow.setOnClickListener(new View.OnClickListener()
        {
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
        final ArrayList<Integer> keysAr = new ArrayList<>();
        for (int x = 1; x <= 29; x++) {
            keysAr.add(x);
        }
//        keysAr.add(0);
        grid_timing.setAdapter(new CustomTimingGrid(this, keysAr));
    }

    private void startDate(final TextView textView) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                textView.setText(sdf.format(myCalendar.getTime()));
            }

        };
    }

    private void showAlreadyAddedTime() {
        RecyclerView recyclerview_alreadyAddedTime = findViewById(R.id.recyclerview_alreadyAddedTime);

        recyclerview_alreadyAddedTime.setNestedScrollingEnabled(false);
        recyclerview_alreadyAddedTime.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerview_alreadyAddedTime.setHasFixedSize(true);
        recyclerview_alreadyAddedTime.setItemAnimator(new DefaultItemAnimator());
        AlreadyAddedTimeAdapter horizontalCategoriesAdapter = new AlreadyAddedTimeAdapter(new ArrayList<>(), this);
        recyclerview_alreadyAddedTime.setAdapter(horizontalCategoriesAdapter);

    }


    private void getAlreadyAddedTime() {


        try {
            final Map<String, String> m = new HashMap<>();
            m.put("consultant_id", getIntent().getStringExtra(Utilclass.consultant_id));

            m.put("device_type", "android");
            m.put("device_token", getDeviceToken() + "");

            final Map<String, String> obj = new HashMap<>();
            obj.put("token", getRestParamsName(Utilclass.token));
            System.out.println("Appointment data===" + m);

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
                            alertDialogs.alertDialog(SetAppointMent.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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