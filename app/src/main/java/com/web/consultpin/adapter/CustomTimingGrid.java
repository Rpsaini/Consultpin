package com.web.consultpin.adapter;

import android.app.TimePickerDialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.web.consultpin.R;
import com.web.consultpin.consultant.SetTimeByConsultant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CustomTimingGrid extends BaseAdapter {
    private SetTimeByConsultant context;
    private final ArrayList<JSONObject> gridValues;
    private int count = 0;
    private String newTime = "";


    public CustomTimingGrid(SetTimeByConsultant context, ArrayList<JSONObject> artistData) {

        this.context = context;
        this.gridValues = artistData;
        count = 0;
        newTime = "";


    }

    @Override
    public int getCount() {
        return gridValues.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return gridValues.size();
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView = null;
        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.custom_timing_grid, null);
        } else {

            gridView = (View) convertView;
        }

        try {
            final TextView txt_select_time = gridView.findViewById(R.id.txt_select_time);
            JSONObject dataObj = gridValues.get(position);
            if (dataObj != null) {
                //String id = dataObj.getString("id");
                //String timingType = dataObj.getString("timing_type");
                String timing = dataObj.getString("timing");
                String isWeekend = dataObj.getString("include_weekend_n_holidays");

                System.out.println("Premap=====" + context.preTimeMapCustom);
                if (context.preTimeMapCustom.containsKey(position)) {
                    txt_select_time.setBackgroundResource(R.drawable.blue_drawable);
                    txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    txt_select_time.setBackgroundResource(R.drawable.roundcorner_drawable);
                    txt_select_time.setTextColor(context.getResources().getColor(R.color.black));
                }


                txt_select_time.setText(timing);
                if (isWeekend.equalsIgnoreCase("1")) {
                    txt_select_time.setAlpha(.5f);
                } else {
                    txt_select_time.setAlpha(1f);


                    txt_select_time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                          {
                            if(context.preTimeMapCustom.containsKey(position))
                            {
                                context.preTimeMapCustom.remove(position);
                                notifyDataSetChanged();
                            }
                            else
                            {
                                context.preTimeMapCustom.put(position, txt_select_time.getText().toString());
                                notifyDataSetChanged();
                            }
                          }
                    });
                }
            } else {
                txt_select_time.setText("Add New Time");
                txt_select_time.setTextSize(13);
                txt_select_time.setTextColor(context.getResources().getColor(R.color.buttonskyblue));
                txt_select_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_select_time.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                addOrEditTime(txt_select_time, "Start Time", true, -1);
                            }
                        });
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gridView;
    }


    private void addOrEditTime(TextView txt_select_time, String title, boolean isAddOrEdit, int position) {
        System.out.println("Clicked====>");

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                count++;
                txt_select_time.setText(selectedHour + ":" + selectedMinute);
                newTime = newTime + "-" + txt_select_time.getText().toString();

                if (count == 2) {
                    try {
                        if (isAddOrEdit) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("timing", newTime.substring(1, newTime.length()));
                            jsonObject.put("include_weekend_n_holidays", "0");
                            context.customTimeArray.remove(context.customTimeArray.size() - 1);
                            context.customTimeArray.add(jsonObject);
                            context.viewTimingGrid(context.customTimeArray);

                            context.preTimeMapCustom.put(position, newTime);


                        } else
                            {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("timing", newTime.substring(1, newTime.length()));
                            jsonObject.put("include_weekend_n_holidays", "0");
                            context.customTimeArray.remove(context.customTimeArray.size() - 1);
                            context.customTimeArray.set(position, jsonObject);
                            context.viewTimingGrid(context.customTimeArray);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (count == 1) {
                    addOrEditTime(txt_select_time, "End Time", isAddOrEdit, position);
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(title);
        mTimePicker.show();


    }

}

