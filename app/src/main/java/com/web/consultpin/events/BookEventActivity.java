package com.web.consultpin.events;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.dialogsnpickers.DialogCallBacks;
import com.app.vollycommunicationlib.CallBack;
import com.web.consultpin.R;
import com.web.consultpin.Utilclass;
import com.web.consultpin.main.BaseActivity;
import com.web.consultpin.usersection.UserEventHistory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookEventActivity extends BaseActivity {
    private TextView tv_save;
    private EditText ed_fullname;
    private EditText ed_email;
    private EditText ed_provience,ed_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_event);
        initiateObj();
        getSupportActionBar().hide();
        init();
    }

    private void init() {
        ed_fullname = findViewById(R.id.ed_fullname);
        ed_email = findViewById(R.id.ed_email);
        ed_provience = findViewById(R.id.ed_provience);

        ed_phone = findViewById(R.id.ed_phone);
        tv_save = findViewById(R.id.tv_save);



        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.bookevent));
        findViewById(R.id.toolbar_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBooking();
            }
        });
    }

    public void showHourPicker(TextView textview) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);

                }
                textview.setText(hourOfDay + ":" + minute + ":00");
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(BookEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle(getResources().getString(R.string.chooseimage));
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    private void startDate(final TextView textView) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd = new DatePickerDialog(BookEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        textView.setText(year + "-"
                                + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();

    }

    private void saveBooking()
    {

       if(validationRule.checkEmptyString(ed_fullname)==0) {
           alertDialogs.alertDialog(BookEventActivity.this, getResources().getString(R.string.Response), getResources().getString(R.string.enter_fullname), getResources().getString(R.string.ok), "", new DialogCallBacks() {
               @Override
               public void getDialogEvent(String buttonPressed) {

               }
           });
           return;
       }
        if(validationRule.checkEmptyString(ed_email)==0) {
            alertDialogs.alertDialog(BookEventActivity.this, getResources().getString(R.string.Response), getResources().getString(R.string.enter_email), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                @Override
                public void getDialogEvent(String buttonPressed) {

                }
            });
            return;
        }

        if(validationRule.checkEmail(ed_email)==0)
        {
            alertDialogs.alertDialog(BookEventActivity.this, getResources().getString(R.string.Response), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                @Override
                public void getDialogEvent(String buttonPressed) {

                }
            });
            return;
        }
        if(validationRule.checkEmptyString(ed_phone)==0) {
            alertDialogs.alertDialog(BookEventActivity.this, getResources().getString(R.string.Response), getResources().getString(R.string.enter_phone), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                @Override
                public void getDialogEvent(String buttonPressed) {

                }
            });
            return;
        }

        if(validationRule.checkEmptyString(ed_provience)==0) {
            alertDialogs.alertDialog(BookEventActivity.this, getResources().getString(R.string.Response), getResources().getString(R.string.enter_provience), getResources().getString(R.string.ok), "", new DialogCallBacks() {
                @Override
                public void getDialogEvent(String buttonPressed) {

                }
            });
            return;
        }

        bookEvent();

    }

    public void bookEvent() {


        try {
            final Map<String, String> m = new HashMap<>();
            m.put("fullname", ed_fullname.getText()+"");
            m.put("email", ed_email.getText().toString());
            m.put("phone", ed_phone.getText().toString());
            m.put("provience", ed_provience.getText().toString());
            m.put("event_fee", getIntent().getStringExtra(Utilclass.eventFee));
            m.put("event_id", getIntent().getStringExtra(Utilclass.event_id));

            final Map<String, String> obj = new HashMap<>();
            obj.put("token",getRestParamsName(Utilclass.token));


            System.out.println("Before to book event=-==="+m);
            System.out.println("Before token=-==="+obj);

            serverHandler.sendToServer(BookEventActivity.this, getApiUrl() + "book-an-event", m, 0, obj, 20000, R.layout.progressbar, new CallBack() {
                @Override
                public void getRespone(String dta, ArrayList<Object> respons) {
                    try {
                        JSONObject jsonObject = new JSONObject(dta);
                        if (jsonObject.getBoolean("status")) {
                            alertDialogs.alertDialog(BookEventActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"),getResources().getString(R.string.ok), "", new DialogCallBacks() {
                                @Override
                                public void getDialogEvent(String buttonPressed)
                                {
                                    Intent intent=new Intent();
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            });
                        } else {
                            alertDialogs.alertDialog(BookEventActivity.this, getResources().getString(R.string.Response), jsonObject.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
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