package com.web.consultpin.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.web.consultpin.R;
import com.web.consultpin.consultant.SetTimeByConsultant;
import com.web.consultpin.usersection.SetAppointmentByUser;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RequestForAppobyUserAdapter extends BaseAdapter {
    private SetAppointmentByUser context;
    private final ArrayList<JSONObject> gridValues;
    private Map<String, Boolean> reservedMap;
    private LinearLayout ll_timing_common;
    private TextView commonTextView;


    public RequestForAppobyUserAdapter(SetAppointmentByUser context, ArrayList<JSONObject> artistData, Map<String, Boolean> reservedMap) {
        this.context = context;
        this.gridValues = artistData;
        this.reservedMap = reservedMap;
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
            gridView = inflater.inflate(R.layout.request_time_adapter_by_user, null);
        } else {

            gridView = (View) convertView;
        }

        try {
            JSONObject dataObj = gridValues.get(position);
            TextView txt_select_time = gridView.findViewById(R.id.txt_select_time);
            LinearLayout ll_timing = gridView.findViewById(R.id.ll_timing);
            txt_select_time.setText(dataObj.getString("timing"));





            if(reservedMap.containsKey(dataObj.getString("timing")))
            {
                if(reservedMap.get(dataObj.getString("timing")))
                {
                    ll_timing.setBackgroundResource(R.drawable.red_border_drawable);
                }
                else
                {
                    if(ll_timing_common!=null)
                    {
                        ll_timing_common.setBackgroundResource(R.drawable.blue_drawable);
                        commonTextView.setTextColor(context.getResources().getColor(R.color.white));
                    }

                    ll_timing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(ll_timing_common!=null)
                            {
                                ll_timing_common.setBackgroundResource(R.drawable.roundcorner_drawable);
                                commonTextView.setTextColor(context.getResources().getColor(R.color.black));
                            }

                            ll_timing.setBackgroundResource(R.drawable.blue_drawable);
                            txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
                            ll_timing_common=ll_timing;
                            commonTextView=txt_select_time;
                            context.timeSlot=txt_select_time.getText()+"";


                        }
                    });
                }

            }
            else
              {
                  if(ll_timing_common!=null)
                  {
                      ll_timing_common.setBackgroundResource(R.drawable.blue_drawable);
                      commonTextView.setTextColor(context.getResources().getColor(R.color.white));
                  }

                  ll_timing.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          if(ll_timing_common!=null)
                          {
                              ll_timing_common.setBackgroundResource(R.drawable.roundcorner_drawable);
                              commonTextView.setTextColor(context.getResources().getColor(R.color.black));
                          }

                          ll_timing.setBackgroundResource(R.drawable.blue_drawable);
                          txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
                          ll_timing_common=ll_timing;
                          commonTextView=txt_select_time;
                          context.timeSlot=txt_select_time.getText()+"";


                      }
                  });
             }



        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return gridView;
    }


}

