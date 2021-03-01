package com.web.consultpin.adapter;
import android.app.TimePickerDialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.web.consultpin.R;
import com.web.consultpin.consultant.AddTimeByConsultantFrg;

import com.web.consultpin.consultant.TimeManagement;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
public class CustomTimingGrid extends BaseAdapter {
    private TimeManagement timeManagement;
    private final ArrayList<String> gridValues;
    private int count = 0;
    private String newTime ="";
    private AddTimeByConsultantFrg addTimeByConsultantFrg;

    public CustomTimingGrid(TimeManagement context, ArrayList<String> artistData, AddTimeByConsultantFrg addTimeByConsultantFrg) {
        this.timeManagement = context;
        this.gridValues = artistData;
        count = 0;
        newTime = "";
        this.addTimeByConsultantFrg=addTimeByConsultantFrg;

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


    public View getView(int position, View convertView, ViewGroup parent)
      {
          LayoutInflater inflater = (LayoutInflater) timeManagement
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView = null;
        if (convertView == null) {
            gridView = new View(timeManagement);
            gridView = inflater.inflate(R.layout.custom_timing_grid, null);
        } else {

            gridView = (View) convertView;
        }

        try {
            String time = gridValues.get(position);
            final TextView txt_select_time = gridView.findViewById(R.id.txt_select_time);
            final LinearLayout ll_timingrow = gridView.findViewById(R.id.ll_timingrow);
            txt_select_time.setText(time);


              if(timeManagement.preTimeMapCustom.containsKey(position))
                    {
                        ll_timingrow.setBackgroundResource(R.drawable.blue_drawable);
                       txt_select_time.setTextColor(timeManagement.getResources().getColor(R.color.white));

                    }
                    else
                    {
                        ll_timingrow.setBackgroundResource(R.drawable.roundcorner_drawable);
                       txt_select_time.setTextColor(timeManagement.getResources().getColor(R.color.black));

                    }

            txt_select_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(timeManagement.preTimeMapCustom.containsKey(txt_select_time.getText().toString()))
                    {
                        ll_timingrow.setBackgroundResource(R.drawable.roundcorner_drawable);
                        txt_select_time.setTextColor(timeManagement.getResources().getColor(R.color.black));
                        timeManagement.preTimeMapCustom.remove(position);
                    }
                    else
                    {
                        ll_timingrow.setBackgroundResource(R.drawable.blue_drawable);
                        txt_select_time.setTextColor(timeManagement.getResources().getColor(R.color.white));
                        timeManagement.preTimeMapCustom.put(position,txt_select_time.getText().toString());
                    }

                    System.out.println("premap==="+timeManagement.preTimeMapCustom);
                    addTimeByConsultantFrg.notifyMap(timeManagement.preTimeMapAlready,"already");
                    notifyDataSetChanged();
                }
            });



//            if(time != null)
//            {
//
//                if (context.preTimeMapCustom.containsKey(position)) {
//                    txt_select_time.setBackgroundResource(R.drawable.blue_drawable);
//                    txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
//                } else {
//                    txt_select_time.setBackgroundResource(R.drawable.roundcorner_drawable);
//                    txt_select_time.setTextColor(context.getResources().getColor(R.color.black));
//                }
//
//
//                txt_select_time.setText(time);
//                System.out.println("Is week open==="+context.include_weekend_n_holidays);
//                if(context.include_weekend_n_holidays.equalsIgnoreCase("0"))
//                {
//                    txt_select_time.setAlpha(.5f);
//                }
//                else {
//                    txt_select_time.setAlpha(1f);
//                    txt_select_time.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v)
//                          {
//                            System.out.println("Map data==="+context.preTimeMapCustom);
//                            if(context.preTimeMapCustom.containsKey(position))
//                            {
//                                context.preTimeMapCustom.remove(position);
//                            }
//                            else
//                            {
//                              context.preTimeMapCustom.put(position, txt_select_time.getText().toString());
//                            }
//                           //  context.notifyMap(context.preTimeMapAlready,"already");
//                             notifyDataSetChanged();
//                          }
//                    });
//                }
//            } else {
//                txt_select_time.setText("Add New Time");
//                txt_select_time.setTextSize(13);
//                txt_select_time.setTextColor(context.getResources().getColor(R.color.buttonskyblue));
//                txt_select_time.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        addOrEditTime(txt_select_time, "Start Time", true, -1);
//
//                    }
//                });
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gridView;
    }


//    private void addOrEditTime(TextView txt_select_time, String title, boolean isAddOrEdit, int position) {
//        System.out.println("Clicked====>");
//
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);
//        TimePickerDialog mTimePicker;
//        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//                count++;
//                txt_select_time.setText(selectedHour + ":" + selectedMinute);
//                newTime = newTime + "-" + txt_select_time.getText().toString();
//
//                if (count == 2) {
//                    try {
//                        if (isAddOrEdit) {
//                            context.customTimeArray.remove(context.customTimeArray.size() - 1);
//                            context.customTimeArray.add(newTime.substring(1, newTime.length()));
//                            context.preTimeMapCustom.put(position, newTime);
//
//                            context.viewTimingGrid(context.customTimeArray);
//
//                          }
//                         else
//                            {
//                            context.customTimeArray.remove(context.customTimeArray.size() - 1);
//                            context.customTimeArray.set(position, newTime.substring(1, newTime.length()));
//                            context.viewTimingGrid(context.customTimeArray);
//
//                        }
//                         System.out.println("Isedit or not==="+isAddOrEdit);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                if (count == 1) {
//                    addOrEditTime(txt_select_time, "End Time", isAddOrEdit, position);
//                }
//
//            }
//        }, hour, minute, true);//Yes 24 hour time
//        mTimePicker.setTitle(title);
//        mTimePicker.show();
//
//
//    }

}

