package com.web.consultpin.consultant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.web.consultpin.R;
import com.web.consultpin.usersection.SetAppointmentByUser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditTimeGridAdapter extends BaseAdapter {

    private EditTimeByConsultant context;
    private final ArrayList<String> gridValues;
    private Map<String, Boolean> reservedMap,alreadysettedMap;





    public EditTimeGridAdapter(EditTimeByConsultant context, ArrayList<String> artistData, Map<String, Boolean> reservedMap,Map<String, Boolean> alreadysettedMap) {
        this.context = context;
        this.gridValues = artistData;
        this.reservedMap = reservedMap;
        this.alreadysettedMap=alreadysettedMap;
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
            String time = gridValues.get(position);
            TextView txt_select_time = gridView.findViewById(R.id.txt_select_time);
            LinearLayout ll_timing = gridView.findViewById(R.id.ll_timing);
            txt_select_time.setText(time);

            if(alreadysettedMap.containsKey(time))
            {

                System.out.println("Reserve time---"+reservedMap+"==="+time);
//                if(reservedMap.containsKey(time))
//                {
//                    ll_timing.setBackgroundResource(R.drawable.red_border_drawable);
//                }
//                else
//                {
                    ll_timing.setBackgroundResource(R.drawable.blue_drawable);
                    txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
                    context.preTimeMapCustom.put(position,time);
               // }

            }
            else
            {
                ll_timing.setBackgroundResource(R.drawable.roundcorner_drawable);
                txt_select_time.setTextColor(context.getResources().getColor(R.color.black));
            }

//            if(reservedMap.containsKey(time))
//            {
//                if(reservedMap.get(time))
//                {
//                    ll_timing.setBackgroundResource(R.drawable.red_border_drawable);
//                }
//                else
//                {
//                    ll_timing.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(!context.preTimeMapCustom.containsKey(position))
//                            {
//                                context.preTimeMapCustom.put(position,txt_select_time.getText()+"");
//                                ll_timing.setBackgroundResource(R.drawable.blue_drawable);
//                                txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
//                            }
//                            else
//                            {
//                                context.preTimeMapCustom.remove(position);
//                                ll_timing_common.setBackgroundResource(R.drawable.roundcorner_drawable);
//                                txt_select_time.setTextColor(context.getResources().getColor(R.color.black));
//                            }
//
//
//                            ll_timing_common=ll_timing;
//                            context.timeSlot=txt_select_time.getText()+"";
//                        }
//                    });
//                }
//
//            }
//            else
             {

                ll_timing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!context.preTimeMapCustom.containsKey(position))
                        {
                            context.preTimeMapCustom.put(position,txt_select_time.getText()+"");
                            ll_timing.setBackgroundResource(R.drawable.blue_drawable);
                            txt_select_time.setTextColor(context.getResources().getColor(R.color.white));
                        }
                        else
                        {
                            context.preTimeMapCustom.remove(position);
                            ll_timing.setBackgroundResource(R.drawable.roundcorner_drawable);
                            txt_select_time.setTextColor(context.getResources().getColor(R.color.black));
                        }
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

