package com.web.consultpin.consultant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.web.consultpin.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class EditTimeGridAdapter extends BaseAdapter {

    private TimeManagement context;
    private final ArrayList<JSONObject> gridValues;
    private Map<String, Boolean> reservedMap, alreadysettedMap;
    private ArrayList<String> times;


    public EditTimeGridAdapter(ArrayList<String> times,TimeManagement context, ArrayList<JSONObject> artistData, Map<String, Boolean> reservedMap, Map<String, Boolean> alreadysettedMap) {
        this.context = context;
        this.gridValues = artistData;
        this.reservedMap = reservedMap;
        this.alreadysettedMap = alreadysettedMap;
        this.times = times;
    }

    @Override
    public int getCount() {
        return times.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return times.size();
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
            String time = times.get(position);
            TextView txt_select_time = gridView.findViewById(R.id.txt_select_time);
            LinearLayout ll_timing = gridView.findViewById(R.id.ll_timing);
            //String time=timeObj.getString("timing");
            txt_select_time.setText(time);



            if (alreadysettedMap.containsKey(time))
            {

                ll_timing.setBackgroundResource(R.drawable.blue_drawable);
                txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
                context.preTimeMapCustom.put(position, time);

            }
            else
            {
                ll_timing.setBackgroundResource(R.drawable.roundcorner_drawable);
                txt_select_time.setTextColor(context.getResources().getColor(R.color.black));
                context.preTimeMapCustom.remove(position);
            }
            if (reservedMap.containsKey(time))
            {

                ll_timing.setBackgroundResource(R.drawable.red_border_drawable);
                txt_select_time.setTextColor(context.getResources().getColor(R.color.black));
            }


            ll_timing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!context.preTimeMapCustom.containsKey(position)) {
                        context.preTimeMapCustom.put(position, txt_select_time.getText() + "");
                        ll_timing.setBackgroundResource(R.drawable.blue_drawable);
                        txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
                    } else {
                        context.preTimeMapCustom.remove(position);
                        ll_timing.setBackgroundResource(R.drawable.roundcorner_drawable);
                        txt_select_time.setTextColor(context.getResources().getColor(R.color.black));
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return gridView;
    }


}

